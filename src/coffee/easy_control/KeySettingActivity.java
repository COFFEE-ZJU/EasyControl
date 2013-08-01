package coffee.easy_control;

import coffee.easy_control.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class KeySettingActivity extends Activity{
	private Spinner[] keySpinners=null;
	private byte[] keys=null;
	private TextView[] keyTVs=null;
	private ArrayAdapter<CharSequence> keySetAdapter=null;
	private SharedPreferences keyPreferences=null;
	private SharedPreferences.Editor keyEditor=null;
	
	private void getKeys(){
		keyPreferences = this.getSharedPreferences("keys", MODE_PRIVATE);
		keyEditor = keyPreferences.edit();
		keys = new byte[11];
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
	
	private int posToCode(int position){
		if(position == 0) return Constant.KEY_ENTER;
		else if(position >= 1 && position <=3) return (Constant.KEY_SHIFT + position - 1);
		else if(position == 4) return (Constant.KEY_ESC);
		else if(position >= 5 && position <= 12) return (Constant.KEY_PAGE_UP + position - 5);
		else if(position >= 13 && position <= 22) return (Constant.KEY_NUM_0 + position - 13);
		else if(position >= 23 && position <= 48) return (Constant.KEY_A + position - 23);
		else if(position >= 49 && position <= 60) return (Constant.KEY_F1 + position - 49);
		
		return Constant.KEY_ENTER;
	}
	
	private int codeToPos(int code){
		if(code == Constant.KEY_ENTER) return 0;
		else if(code >= Constant.KEY_SHIFT && code <= Constant.KEY_ALT)
			return (code - Constant.KEY_SHIFT + 1);
		else if(code == Constant.KEY_ESC) return 4;
		else if(code >= Constant.KEY_PAGE_UP && code <= Constant.KEY_DOWN)
			return (code - Constant.KEY_PAGE_UP + 5);
		else if(code >= Constant.KEY_NUM_0 && code <= Constant.KEY_NUM_9)
			return (code - Constant.KEY_NUM_0 + 13);
		else if(code >= Constant.KEY_A && code <= Constant.KEY_Z)
			return (code - Constant.KEY_A + 23);
		else if(code >= Constant.KEY_F1 && code <= Constant.KEY_F12)
			return (code - Constant.KEY_F1 + 49);
		
		return 0;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.key_setting);
		
		getKeys();
		
		keyTVs = new TextView[11];
		keyTVs[0] = (TextView)findViewById(R.id.accelerateTV);
		keyTVs[1] = (TextView)findViewById(R.id.brakeTV);
		keyTVs[2] = (TextView)findViewById(R.id.L1TV);
		keyTVs[3] = (TextView)findViewById(R.id.L2TV);
		keyTVs[4] = (TextView)findViewById(R.id.L3TV);
		keyTVs[5] = (TextView)findViewById(R.id.R1TV);
		keyTVs[6] = (TextView)findViewById(R.id.R2TV);
		keyTVs[7] = (TextView)findViewById(R.id.R3TV);
		keyTVs[8] = (TextView)findViewById(R.id.M1TV);
		keyTVs[9] = (TextView)findViewById(R.id.M2TV);
		keyTVs[10] = (TextView)findViewById(R.id.M3TV);
		keyTVs[0].setText(R.string.accelerate);
		keyTVs[1].setText(R.string.brake);
		keyTVs[2].setText("L1");
		keyTVs[3].setText("L2");
		keyTVs[4].setText("L3");
		keyTVs[5].setText("R1");
		keyTVs[6].setText("R2");
		keyTVs[7].setText("R3");
		keyTVs[8].setText("M1");
		keyTVs[9].setText("M2");
		keyTVs[10].setText("M3");
		
		keySpinners = new Spinner[11];
		keySpinners[0] = (Spinner)findViewById(R.id.accelerateSpinner);
		keySpinners[1] = (Spinner)findViewById(R.id.brakeSpinner);
		keySpinners[2] = (Spinner)findViewById(R.id.L1Spinner);
		keySpinners[3] = (Spinner)findViewById(R.id.L2Spinner);
		keySpinners[4] = (Spinner)findViewById(R.id.L3Spinner);
		keySpinners[5] = (Spinner)findViewById(R.id.R1Spinner);
		keySpinners[6] = (Spinner)findViewById(R.id.R2Spinner);
		keySpinners[7] = (Spinner)findViewById(R.id.R3Spinner);
		keySpinners[8] = (Spinner)findViewById(R.id.M1Spinner);
		keySpinners[9] = (Spinner)findViewById(R.id.M2Spinner);
		keySpinners[10] = (Spinner)findViewById(R.id.M3Spinner);
		
		
		keySetAdapter = ArrayAdapter.createFromResource(
				this, 
				R.array.key_set, 
				android.R.layout.simple_spinner_item);
		keySetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for(int i=0;i<11;i++){
			keySpinners[i].setAdapter(keySetAdapter);
			keySpinners[i].setPrompt("Ñ¡Ôñ°´¼ü");
			keySpinners[i].setOnItemSelectedListener(new KeySelectedListener(i));
			keySpinners[i].setSelection(codeToPos(keys[i]));
		}
		
	}
	
	class KeySelectedListener implements OnItemSelectedListener{
		private int offset;
		public KeySelectedListener(int off){offset = off;}
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long id) {
			keyEditor.putInt(Constant.KEYS[offset], posToCode(position));
			keyEditor.commit();
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
