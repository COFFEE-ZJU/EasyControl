package coffee.easy_control;

import coffee.easy_control.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class SteeringWheelActivity extends Activity {
	private byte[] keys;
	private SharedPreferences keyPreferences=null;
	
	private TextView res=null;
	private Button L1Btn = null;
	private Button L2Btn = null;
	private Button L3Btn = null;
	private Button R1Btn = null;
	private Button R2Btn = null;
	private Button R3Btn = null;
	private Button M1Btn = null;
	private Button M2Btn = null;
	private Button M3Btn = null;
	private Button accelerateBtn = null;
	private Button brakeBtn = null;
	private MySender sender;
	
	private SensorManager sensorMgr;
	private Sensor sensor;
	SensorEventListener lsn;
	private int turn=0;
	
	private void getKeys(){
		keys[0] = (byte)keyPreferences.getInt(Constant.KEYS[0], Constant.KEY_A);
		keys[1] = (byte)keyPreferences.getInt(Constant.KEYS[1], Constant.KEY_DOWN);
		keys[2] = (byte)keyPreferences.getInt(Constant.KEYS[2], Constant.KEY_F1);
		keys[3] = (byte)keyPreferences.getInt(Constant.KEYS[3], Constant.KEY_F2);
		keys[4] = (byte)keyPreferences.getInt(Constant.KEYS[4], Constant.KEY_F3);
		keys[5] = (byte)keyPreferences.getInt(Constant.KEYS[5], Constant.KEY_F1);
		keys[6] = (byte)keyPreferences.getInt(Constant.KEYS[6], Constant.KEY_F2);
		keys[7] = (byte)keyPreferences.getInt(Constant.KEYS[7], Constant.KEY_F3);
		keys[8] = (byte)keyPreferences.getInt(Constant.KEYS[8], Constant.KEY_F1);
		keys[9] = (byte)keyPreferences.getInt(Constant.KEYS[9], Constant.KEY_F2);
		keys[10] = (byte)keyPreferences.getInt(Constant.KEYS[10], Constant.KEY_F3);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==1){
    		Intent intent = new Intent();
    		intent.setClass(SteeringWheelActivity.this, KeySettingActivity.class);
    		SteeringWheelActivity.this.startActivity(intent);
    	}
    	else if(item.getItemId()==2){
    		Intent intent = new Intent();
    		intent.setClass(SteeringWheelActivity.this, MainActivity.class);
    		SteeringWheelActivity.this.startActivity(intent);
    	}
    	else if(item.getItemId()==3){
    		Intent startMain = new Intent(Intent.ACTION_MAIN);
    		startMain.addCategory(Intent.CATEGORY_HOME);
    		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(startMain);
    		System.exit(0);
    	}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0,1,1,R.string.setting);
    	menu.add(0,2,2,R.string.back);
    	menu.add(0,3,3,R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	protected void onPause() {
		super.onPause();
		
		sensorMgr.unregisterListener(lsn);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getKeys();
		sensorMgr.registerListener(lsn, sensor,SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.steering_wheel);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		sender = (MySender)getApplication();
		keyPreferences = this.getSharedPreferences("keys", MODE_PRIVATE);
		keys = new byte[11];
		getKeys();
		sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		lsn = new SensorEventListener(){
			public void onAccuracyChanged(Sensor s, int accuracy) {
			}
			@Override
			public void onSensorChanged(SensorEvent e) {
				//sensorMgr.unregisterListener(this);
				int newTurn;
				//float x = e.values[SensorManager.DATA_X];
				Float y = e.values[SensorManager.DATA_Y];
				//float z = e.values[SensorManager.DATA_Z];
				if(y.intValue()<=-2) newTurn = -1;
				else if(y.intValue()>=2) newTurn = 1;
				else newTurn = 0;
				if(turn != newTurn){
					if(turn == -1) sender.sendAction(new MyAction(Constant.ACTION_KEY_UP, Constant.KEY_LEFT));
					else if(turn == 1) sender.sendAction(new MyAction(Constant.ACTION_KEY_UP, Constant.KEY_RIGHT));
					
					if(newTurn == -1) sender.sendAction(new MyAction(Constant.ACTION_KEY_DOWN, Constant.KEY_LEFT));
					else if(newTurn == 1) sender.sendAction(new MyAction(Constant.ACTION_KEY_DOWN, Constant.KEY_RIGHT));
					
					turn = newTurn;
					//res.setText("right level = "+turn);
				}
				//sensorMgr.registerListener(this, sensor,SensorManager.SENSOR_DELAY_UI);
			}
		};
		sensorMgr.registerListener(lsn, sensor,SensorManager.SENSOR_DELAY_UI);

		res = (TextView)findViewById(R.id.debugger);
		L1Btn = (Button)findViewById(R.id.L1Button);
		L2Btn = (Button)findViewById(R.id.L2Button);
		L3Btn = (Button)findViewById(R.id.L3Button);
		R1Btn = (Button)findViewById(R.id.R1Button);
		R2Btn = (Button)findViewById(R.id.R2Button);
		R3Btn = (Button)findViewById(R.id.R3Button);
		M1Btn = (Button)findViewById(R.id.M1Button);
		M2Btn = (Button)findViewById(R.id.M2Button);
		M3Btn = (Button)findViewById(R.id.M3Button);
		accelerateBtn = (Button)findViewById(R.id.accelerateButton);
		brakeBtn = (Button)findViewById(R.id.brakeButton);
		//new ClientThread().start();
		accelerateBtn.setText(R.string.accelerate);
		brakeBtn.setText(R.string.brake);
		L1Btn.setText("       L1       ");
		L2Btn.setText("L2");
		L3Btn.setText("L3");
		R1Btn.setText("       R1       ");
		R2Btn.setText("R2");
		R3Btn.setText("R3");
		M1Btn.setText("   M1   ");
		M2Btn.setText("M2");
		M3Btn.setText("M3");
		
		accelerateBtn.setOnTouchListener(new KeyTouchListener(0));
		brakeBtn.setOnTouchListener(new KeyTouchListener(1));
		L1Btn.setOnTouchListener(new KeyTouchListener(2));
		L2Btn.setOnTouchListener(new KeyTouchListener(3));
		L3Btn.setOnTouchListener(new KeyTouchListener(4));
		R1Btn.setOnTouchListener(new KeyTouchListener(5));
		R2Btn.setOnTouchListener(new KeyTouchListener(6));
		R3Btn.setOnTouchListener(new KeyTouchListener(7));
		M1Btn.setOnTouchListener(new KeyTouchListener(8));
		M2Btn.setOnTouchListener(new KeyTouchListener(9));
		M3Btn.setOnTouchListener(new KeyTouchListener(10));
	}
	
	class KeyTouchListener implements OnTouchListener{
		private int position;
		public KeyTouchListener(int pos){position = pos;}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int act = event.getAction();
			if(act == MotionEvent.ACTION_DOWN) sender.sendAction(new MyAction (Constant.ACTION_KEY_DOWN, keys[position]));
			else if(act == MotionEvent.ACTION_UP) sender.sendAction(new MyAction(Constant.ACTION_KEY_UP, keys[position]));	
			return false;	// return false表示系统会继续处理
		}
	}
}
