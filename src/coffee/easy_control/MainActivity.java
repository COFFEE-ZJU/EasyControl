package coffee.easy_control;

import java.util.Timer;
import java.util.TimerTask;

import coffee.easy_control.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private enum PANEL{STEERINGWHEEL,MOUSE,PPTCONTROL};
	private MySender sender;
	private ClientThread clientThread;
	
	private Timer myTimer;
	private StatusTask statusTask;
	private Handler myHandler;
	private StatusUpdater statusUpdater;
	
	private SharedPreferences keyPreferences=null;
	private SharedPreferences.Editor keyEditor=null;
	private EditText ipET=null;
	private TextView ipTV=null;
	private TextView resTV=null;
	private TextView res=null;
	private Button connect=null;
	private Button steeringWheel=null;
	private Button mouse=null;
	private Button pptControl=null;
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==2) System.exit(0);
    	else if(item.getItemId()==1){
    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    		alertDialog.setTitle(R.string.about);
    		alertDialog.setMessage("\n\n" +
    				"Easy Control    version 1.0" +
    				"\n\n");
    		alertDialog.show();
    	}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,1,1,R.string.about);
    	menu.add(0,2,2,R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initPreference();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
        .detectDiskReads()       
        .detectDiskWrites()       
        .detectNetwork()   // or .detectAll() for all detectable problems       
        .penaltyLog()       
        .build());       
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
        .detectLeakedSqlLiteObjects()    
        .penaltyLog()       
        .penaltyDeath()       
        .build());    
 
        sender = (MySender)getApplication();
        
        connect = (Button)findViewById(R.id.Connect);
        steeringWheel = (Button)findViewById(R.id.SteeringWheel);
        mouse = (Button)findViewById(R.id.Mouse);
        pptControl = (Button)findViewById(R.id.PPTControl);
        ipET= (EditText)findViewById(R.id.InputIP_ET);
        ipTV=(TextView)findViewById(R.id.InputIP_TV);
        resTV=(TextView)findViewById(R.id.ConnectResult1);
        res=(TextView)findViewById(R.id.ConnectResult2);
        connect.setText(R.string.connect);
        steeringWheel.setText(R.string.steeringWheel);
        mouse.setText(R.string.mouse);
        pptControl.setText(R.string.PPTControl);
        ipTV.setText(R.string.input_ip);
        resTV.setText(R.string.connectionStatus);
        
        ipET.setText("192.168.162.1");
        res.setText(R.string.unconnected);
        myHandler = new Handler();
        statusUpdater = new StatusUpdater();
        myTimer = new Timer();
        statusTask = new StatusTask();
        myTimer.schedule(statusTask, 500, 500);
        
        steeringWheel.setOnClickListener(new MyButtonListener(PANEL.STEERINGWHEEL));
        mouse.setOnClickListener(new MyButtonListener(PANEL.MOUSE));
        pptControl.setOnClickListener(new MyButtonListener(PANEL.PPTCONTROL));
        connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clientThread = new ClientThread();
				clientThread.start();
			}
		});
    }
    
    private void initPreference(){
    	keyPreferences = this.getSharedPreferences("keys", MODE_PRIVATE);
    	if(! keyPreferences.contains("init")){
    		keyEditor = keyPreferences.edit();
    		keyEditor.putInt("init", 1);
    		keyEditor.putInt(Constant.KEYS[0], Constant.KEY_A);
    		keyEditor.putInt(Constant.KEYS[1], Constant.KEY_DOWN);
    		keyEditor.putInt(Constant.KEYS[2], Constant.KEY_F1);
    		keyEditor.putInt(Constant.KEYS[3], Constant.KEY_F2);
    		keyEditor.putInt(Constant.KEYS[4], Constant.KEY_F3);
    		keyEditor.putInt(Constant.KEYS[5], Constant.KEY_F1);
    		keyEditor.putInt(Constant.KEYS[6], Constant.KEY_F2);
    		keyEditor.putInt(Constant.KEYS[7], Constant.KEY_F3);
    		keyEditor.putInt(Constant.KEYS[8], Constant.KEY_F1);
    		keyEditor.putInt(Constant.KEYS[9], Constant.KEY_F2);
    		keyEditor.putInt(Constant.KEYS[10], Constant.KEY_F3);
    		keyEditor.commit();
    	}
    }
    
    class MyButtonListener implements OnClickListener{
    	private PANEL panel;
    	public MyButtonListener(PANEL selectPanel){
    		panel = selectPanel;
    	}
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			if(panel == PANEL.STEERINGWHEEL) intent.setClass(MainActivity.this, SteeringWheelActivity.class);
			else if(panel == PANEL.MOUSE) intent.setClass(MainActivity.this, MouseActivity.class);
			else if(panel == PANEL.PPTCONTROL) intent.setClass(MainActivity.this, PPTControlActivity.class);
			MainActivity.this.startActivity(intent);
		}
    }
    
    class StatusTask extends TimerTask{
		@Override
		public void run() {
			myHandler.post(statusUpdater);
		}
    }
    
    class StatusUpdater implements Runnable{
    	private boolean flag = false;
		public void run() {
			if(sender.connecting){
				flag = true;
				res.setTextColor(Color.CYAN);
				res.setText(R.string.connecting);
			}
			else if(sender.available){
				flag = true;
				res.setTextColor(Color.CYAN);
				res.setText(R.string.connected);
			}
			else{
				res.setTextColor(Color.GRAY);
				res.setText(R.string.unconnected);
				if(flag){
					Toast.makeText(getApplicationContext(), "无法连接或连接已断开", Toast.LENGTH_SHORT).show();
					flag = false;
				}
				
			}
		}
    }
    
	class ClientThread extends Thread{
		public void run(){
			String ip= ipET.getText().toString();
			sender.setSocket(ip,Constant.PORT_NUM);
		}
	}
}