package coffee.easy_control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;

public class MouseTextView extends TextView {
	private short oldX,oldY;
	private MySender sender=null;
	private GestureDetector gDetector=null;

	private float sensi = 1;
	
	public void setSensitivity(float sensitivity){sensi = sensitivity;}
	public void setSender(MySender sender){this.sender = sender;}
	
	public void enableSensor(){
		if(gDetector==null) gDetector = new GestureDetector(new MouseGesture());
		this.setGestureDetector(gDetector);
	}
	
	public MouseTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setGestureDetector(GestureDetector gd){
		gDetector = gd;
	}
	
//	@Override
//	public  boolean  onInterceptTouchEvent(MotionEvent event){
//		return true;
//	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(gDetector != null) gDetector.onTouchEvent(event);
		return true;
	}

	class MouseGesture implements OnGestureListener,OnDoubleTapListener{
		//private boolean doubleTapping = false;
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//thisView.setText("UP now x = "+oldX+"\ty = "+oldY);
		 	//Toast.makeText(getApplicationContext(),"onSingleTapUp", Toast.LENGTH_SHORT).show();
		    return false;
		}
		@Override
		public void onLongPress(MotionEvent e) {
//			if(doubleTapping) return;
//			thisView.setText("ononLongPress");
//			sender.sendAction(new MyAction(true, Constant.ACTION_DOWN, Constant.RIGHT_CLICK));
//			sender.sendAction(new MyAction(true, Constant.ACTION_UP, Constant.RIGHT_CLICK));
//			Toast.makeText(getApplicationContext(),"onLongPress", Toast.LENGTH_SHORT).show();
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			short x,y;
			x = (short)(distanceX * sensi);
			y = (short)(distanceY * sensi);
			//thisView.setText("MOVE dist x = "+x+"\ty = "+y);
			sender.sendAction(new MyAction((short)(-x), (short)(-y)));
			//Toast.makeText(getApplicationContext(),"onScroll", Toast.LENGTH_SHORT).show();
			return false;
		}
		@Override
		public boolean onDown(MotionEvent e) {
			oldX = (short)e.getX();
			oldY = (short)e.getY();
			//thisView.setText("DOWN now x = "+oldX+"\ty = "+oldY);
			//Toast.makeText(getApplicationContext(),"onDown", Toast.LENGTH_SHORT).show();
		    return false;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}
		@Override
		public void onShowPress(MotionEvent e) {
			
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			//thisView.setText("onDoubleTap");
			//Toast.makeText(getApplicationContext(),"onDoubleTap", Toast.LENGTH_SHORT).show();
		    return false;
		}
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			short oldX = 0 , oldY = 0, x, y;
			//Toast.makeText(getApplicationContext(),"onDoubleTapEvent", Toast.LENGTH_SHORT).show();
			int action = e.getAction();
//			if(doubleTapping == false){
//				doubleTapping = true;
//				oldX = (short)e.getX();
//				oldY = (short)e.getY();
//			}
			
			switch(action){
			case (MotionEvent.ACTION_DOWN):
				oldX = (short)e.getX();
				oldY = (short)e.getY();
				//thisView.setText("secondTap down");
				sender.sendAction(new MyAction(Constant.ACTION_MOUSE_DOWN, Constant.LEFT_CLICK));
				break;
			case (MotionEvent.ACTION_MOVE):
				x = (short)(e.getX() - oldX);
				y = (short)(e.getY() - oldY);
				//thisView.setText("secondTap moving");
				sender.sendAction(new MyAction(x, y));
			case (MotionEvent.ACTION_UP):
				//doubleTapping = false;
				sender.sendAction(new MyAction(Constant.ACTION_MOUSE_UP, Constant.LEFT_CLICK));
				break;
			}
			
			oldX = (short)e.getX();
			oldY = (short)e.getY();
		    return false;
		}
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			sender.sendAction(new MyAction(Constant.ACTION_MOUSE_DOWN, Constant.LEFT_CLICK));
			sender.sendAction(new MyAction(Constant.ACTION_MOUSE_UP, Constant.LEFT_CLICK));
			//Toast.makeText(getApplicationContext(),"onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
		    return false;
		}
	}
}


