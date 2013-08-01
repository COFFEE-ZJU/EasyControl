package coffee.easy_control;

class MyAction {
	public byte act;
	public byte btn;
	public short distX,distY;
	//public boolean isMouse;
	public String toString(){
		if(act == Constant.ACTION_HEARTBEAT || 
				act == Constant.ACTION_ROLL_UP || 
				act == Constant.ACTION_ROLL_DOWN) return (""+act);
		else if(act != Constant.ACTION_MOVE) return ("action:"+act+" button:"+btn);
		else return ("action:"+act+" distX:"+distX+" distY:"+distY);
	}
	public MyAction(byte[] bytes, int len){
		if(len == 1){
			act = bytes[0];
		}
		else if(len == 2){
			act = bytes[0];
			btn = bytes[1];
		}
		else if(len == 5){
			act = bytes[0];
			distX = bytes[1];
			distX *= 128;
			distX = (short)(distX + bytes[2]);
			distY = bytes[3];
			distY *= 128;
			distY = (short)(distY + bytes[4]);
		}
	}
	public MyAction(byte action){act=action;}
	public MyAction(byte action, byte button){act = action; btn =button;}
	public MyAction(short x, short y){
		act = Constant.ACTION_MOVE;
		distX = x;
		distY = y;
	}
	public byte[] getBytes(){
		if(act == Constant.ACTION_MOVE){
			byte[] bytes = new byte[5];
			bytes[0] = act;
			bytes[1] = (byte)(distX / 128);
		    bytes[2] = (byte)(distX % 128);
		    bytes[3] = (byte)(distY / 128);
		    bytes[4] = (byte)(distY % 128);
			return bytes;
		}
		else if(act == Constant.ACTION_HEARTBEAT || 
				act == Constant.ACTION_ROLL_DOWN ||
				act == Constant.ACTION_ROLL_UP ){
			byte[] bytes = new byte[1];
			bytes[0] = act;
			return bytes;
		}
		else{
			byte[] bytes = new byte[2];
			bytes[0] = act;
			bytes[1] = btn;
			return bytes;
		}
	}
}

