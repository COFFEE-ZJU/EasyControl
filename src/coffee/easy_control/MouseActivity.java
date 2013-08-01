package coffee.easy_control;

import coffee.easy_control.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MouseActivity extends Activity {
	
	private MouseTextView touchView=null;
	private Button leftClick=null;
	private Button middleClick=null;
	private Button rightClick=null;
	private Button rollUpButton=null;
	private Button rollDownButton=null;
	private TextView sensitivityTV=null;
	private SeekBar sensitivityBar=null;
	
	private MySender sender=null;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==1){
    		Intent intent = new Intent();
    		intent.setClass(MouseActivity.this, MainActivity.class);
    		MouseActivity.this.startActivity(intent);
    	}
    	else if(item.getItemId()==2){
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
    	menu.add(0,1,1,R.string.back);
    	menu.add(0,2,2,R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mouse);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		sender = (MySender)getApplication();
		touchView = (MouseTextView)findViewById(R.id.touchPanel);
		leftClick = (Button)findViewById(R.id.left_click);
		middleClick = (Button)findViewById(R.id.middle_click);
		rightClick = (Button)findViewById(R.id.right_click);
		rollUpButton = (Button)findViewById(R.id.rollUpButton);
		rollDownButton = (Button)findViewById(R.id.rollDownButton);
		sensitivityTV = (TextView)findViewById(R.id.sensitivityTV);
		sensitivityBar = (SeekBar)findViewById(R.id.sensitivityBar);
		
		sensitivityTV.setText(R.string.sensitivity);
		leftClick.setText(R.string.left_click);
		middleClick.setText(R.string.middle_click);
		rightClick.setText(R.string.right_click);
		rollUpButton.setText(R.string.rollUp);
		rollDownButton.setText(R.string.rollDown);
		//touchLayout.setOnTouchListener(new MyTouchListener("RIGHT"));
		
		touchView.setBackgroundColor(Color.GRAY);
		touchView.setSender(sender);
		touchView.enableSensor();
		
		leftClick.setOnTouchListener(new MouseButtonTouchListener(Constant.LEFT_CLICK));
		middleClick.setOnTouchListener(new MouseButtonTouchListener(Constant.MIDDLE_CLICK));
		rightClick.setOnTouchListener(new MouseButtonTouchListener(Constant.RIGHT_CLICK));
		
		rollUpButton.setOnTouchListener(new RollTouchListener(Constant.ROLL_UP));
		rollDownButton.setOnTouchListener(new RollTouchListener(Constant.ROLL_DOWN));
		
		sensitivityBar.setProgress(5);
		sensitivityBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			private int currentProgress = 5;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				touchView.setText("progress"+currentProgress);
				touchView.setSensitivity((float) ((float)currentProgress/5.0));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentProgress = progress;
			}
		});
	}
	
	class MouseButtonTouchListener implements OnTouchListener{
		private byte btn;
		public MouseButtonTouchListener(byte button){btn = button;}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int act = event.getAction();
			if(act == MotionEvent.ACTION_DOWN) sender.sendAction(new MyAction (true, Constant.ACTION_DOWN, btn));
			else if(act == MotionEvent.ACTION_UP) sender.sendAction(new MyAction(true, Constant.ACTION_UP, btn));	
			return false;
		}
	}
	
	class RollTouchListener implements OnTouchListener{
		private byte btn;
		public RollTouchListener(byte button){btn = button;}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int act = event.getAction();
			if(act == MotionEvent.ACTION_DOWN) sender.sendAction(new MyAction(true, Constant.ACTION_ROLL, btn));
			return false;
		}
	}

}
