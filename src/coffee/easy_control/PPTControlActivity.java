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
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PPTControlActivity extends Activity{
	private MouseTextView touchView=null;
	private MySender sender=null;
	private Button leftClickButton=null;
	private Button rightClickButton=null;
	private Button pageUpButton=null;
	private Button pageDownButton=null;
	private Button playButton=null;
	private TextView sensitivityTV=null;
	private SeekBar sensitivityBar=null;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==1){
    		Intent intent = new Intent();
    		intent.setClass(PPTControlActivity.this, MainActivity.class);
    		PPTControlActivity.this.startActivity(intent);
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
		setContentView(R.layout.ppt_control);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		sender = (MySender)getApplication();
		
		touchView = (MouseTextView)findViewById(R.id.touchPanel);
		leftClickButton = (Button)findViewById(R.id.leftClickButton);
		rightClickButton = (Button)findViewById(R.id.rightClickButton);
		pageUpButton = (Button)findViewById(R.id.pageUpButton);
		pageDownButton = (Button)findViewById(R.id.pageDownButton);
		playButton = (Button)findViewById(R.id.playButton);
		sensitivityTV = (TextView)findViewById(R.id.sensitivityTV);
		sensitivityBar = (SeekBar)findViewById(R.id.sensitivityBar);
		
		leftClickButton.setText(R.string.left_click);
		rightClickButton.setText(R.string.right_click);
		pageUpButton.setText(R.string.pageUp);
		pageDownButton.setText(R.string.pageDown);
		sensitivityTV.setText(R.string.sensitivity);
		playButton.setText(R.string.play);
		
		touchView.setBackgroundColor(Color.GRAY);
		touchView.setSender(sender);
		touchView.enableSensor();
		
		leftClickButton.setOnTouchListener(new MouseButtonTouchListener(Constant.LEFT_CLICK));
		rightClickButton.setOnTouchListener(new MouseButtonTouchListener(Constant.RIGHT_CLICK));
		pageUpButton.setOnTouchListener(new KeyTouchListener(Constant.KEY_PAGE_UP));
		pageDownButton.setOnTouchListener(new KeyTouchListener(Constant.KEY_PAGE_DOWN));
		playButton.setOnTouchListener(new OnTouchListener() {
			private boolean isPlaying = false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int act = event.getAction();
				if(act == MotionEvent.ACTION_UP){
					if(! isPlaying){
						isPlaying = true;
						playButton.setText(R.string.over);
						sender.sendAction(new MyAction(false, Constant.ACTION_DOWN, Constant.KEY_SHIFT));
						sender.sendAction(new MyAction(false, Constant.ACTION_DOWN, Constant.KEY_F5));
						sender.sendAction(new MyAction(false, Constant.ACTION_UP, Constant.KEY_SHIFT));
						sender.sendAction(new MyAction(false, Constant.ACTION_UP, Constant.KEY_F5));
					}
					else{
						isPlaying = false;
						playButton.setText(R.string.play);
						sender.sendAction(new MyAction(false, Constant.ACTION_DOWN, Constant.KEY_ESC));
						sender.sendAction(new MyAction(false, Constant.ACTION_UP, Constant.KEY_ESC));
					}
				}
				return false;
			}
		});
		
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
	
	class KeyTouchListener implements OnTouchListener{
		private byte btn;
		public KeyTouchListener(byte button){btn = button;}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int act = event.getAction();
			if(act == MotionEvent.ACTION_DOWN) sender.sendAction(new MyAction (false, Constant.ACTION_DOWN, btn));
			else if(act == MotionEvent.ACTION_UP) sender.sendAction(new MyAction(false, Constant.ACTION_UP, btn));	
			return false;	// return false表示系统会继续处理
		}
	}
}
