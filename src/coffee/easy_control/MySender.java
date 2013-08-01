package coffee.easy_control;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MySender extends Application{
	//private OutputStream outStream = null;
	public boolean available = false;
	public boolean connecting = false;
	private DatagramSocket socket=null;
	private int port=Constant.PORT_NUM;
	private InetAddress serverAddress=null;
	private DatagramPacket packet=null;
	private DatagramSocket receiver=null;
	private HeartBeatThread heartBeatThread=new HeartBeatThread();
	
	public MySender(){
		try {
			receiver = new DatagramSocket(port);
			receiver.setSoTimeout(Constant.HEARTBEAT_PERIOD);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	class HeartBeatThread extends Thread{
		int len;
		byte[] buf = new byte[10];
		DatagramPacket packet = new DatagramPacket(buf,buf.length);
		
		private boolean heartBeat(){
			forceSendAction(new MyAction(Constant.ACTION_HEARTBEAT));
			try {
				receiver.receive(packet);
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		
		public void run(){
			Looper.prepare();
			MyAction action;
			try {
				connecting = true;
				
				while(true){
					if(heartBeat() == false) break;
					len = packet.getLength();
					action = new MyAction(buf, len);
					if(action.act == Constant.ACTION_HEARTBEAT){
						connecting = false;
						available = true;
					}
					else break;
					Thread.sleep(Constant.HEARTBEAT_PERIOD);
				}
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			connecting = false;
			available = false;
//			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
//			alertDialog.setTitle(R.string.connection_failed);
//			alertDialog.setMessage("\n\n无法连接或连接已断开\n\n");
		}
	}
	
	
	public void setSocket(String ip,int port){
		try {
			this.port = port;
			socket = new DatagramSocket();
			serverAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if(!heartBeatThread.isAlive()){
			heartBeatThread = new HeartBeatThread();
			heartBeatThread.start();
		}
//		available = true;
//		connecting = true;
	}
	
	private void forceSendAction(MyAction action){
		boolean temp = available;
		available = true;
		sendAction(action);
		available=temp;
	}
	public synchronized void sendAction(MyAction action){
		if(! available) return;
		Log.d("coffee_debug", action.toString());
		try {
			byte[] data = action.getBytes();
			Log.d("coffee_debug", "data len: "+data.length);
			packet = new DatagramPacket(data,data.length,serverAddress,port);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
