package coffee.easy_control;

class MyAction {
	public byte act;
	public byte btn;
	public short distX,distY;
	public boolean isMouse;
	public String toString(){
		if(act == Constant.ACTION_HEARTBEAT) return (""+act);
		else if(act != Constant.ACTION_MOVE) return ("isMouse:"+isMouse+" action:"+act+" button:"+btn);
		else return ("isMouse:"+isMouse+" action:"+act+" distX:"+distX+" distY:"+distY);
	}
	public MyAction(byte[] bytes, int len){
		if(len == 1){
			act = bytes[0];
		}
		else if(len == 3){
			if(bytes[0] == 1) isMouse = true;
			else isMouse = false;
			
			act = bytes[1];
			btn = bytes[2];
		}
		else if(len == 6){
			if(bytes[0] == 1) isMouse = true;
			else isMouse = false;
			
			act = bytes[1];
			distX = bytes[2];
			distX *= 128;
			distX = (short)(distX + bytes[3]);
			distY = bytes[4];
			distY *= 128;
			distY = (short)(distY + bytes[5]);
		}
	}
	public MyAction(byte action){isMouse = false; act=action;}
	public MyAction(boolean mouse, byte a, byte b){isMouse = mouse; act = a; btn =b;}
	public MyAction(short x, short y){
		isMouse = true;
		act = Constant.ACTION_MOVE;
		distX = x;
		distY = y;
	}
	public byte[] getBytes(){
		if(act == Constant.ACTION_MOVE){
			byte[] bytes = new byte[6];
			bytes[0] = isMouse ? (byte)1 : (byte)0;
			bytes[1] = act;
			bytes[2] = (byte)(distX / 128);
		    bytes[3] = (byte)(distX % 128);
		    bytes[4] = (byte)(distY / 128);
		    bytes[5] = (byte)(distY % 128);
			return bytes;
		}
		else if(act == Constant.ACTION_HEARTBEAT){
			byte[] bytes = new byte[1];
			bytes[0] = Constant.ACTION_HEARTBEAT;
			return bytes;
		}
		else{
			byte[] bytes = new byte[3];
			bytes[0] = isMouse ? (byte)1 : (byte)0;
			bytes[1] = act;
			bytes[2] = btn;
			return bytes;
		}
	}
}

