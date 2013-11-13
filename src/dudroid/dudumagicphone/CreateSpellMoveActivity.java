package dudroid.dudumagicphone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class CreateSpellMoveActivity extends Activity implements OnItemSelectedListener  {

	Charm charm;
	String Axs[] = new String[] {"x", "y", "z"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_spell_move);
		
		Spinner spinner = (Spinner) findViewById(R.id.axis_spinner);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.axis_mnu, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		charm = ((MyApplication) getApplication()).tmpCharm;

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_spell_move, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addRotation (View view) {
		EditText edTxt = (EditText) findViewById(R.id.degrees);
		int degrees = 360;
		String txt = edTxt.getText().toString();
		if (!txt.equals("")) {
			degrees = Integer.parseInt(txt);
		}
		int axis;
		Spinner spinner = (Spinner) findViewById(R.id.axis_spinner);
		if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please choose the axis", Toast.LENGTH_SHORT).show();
			return;
		} 
		axis = spinner.getSelectedItemPosition();
		Charm.MyRotation rot = new Charm.MyRotation(degrees, axis);
		charm.rotation.add(rot);
		
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.lin_lay_rot);
		TextView line = new TextView(this);
		String str = degrees + " degrees around " + Axs[axis] + " axis";
		//Toast.makeText(this, str, Toast.LENGTH_SHORT);
		line.setText(str);
		line.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		layout.addView(line);
		
	}
	
	public void removeLastRotation (View view) {
		int size = charm.rotation.size()-1;
		LinearLayout layout = (LinearLayout) findViewById(R.id.lin_lay_rot);
		layout.removeViewAt(size);
		charm.rotation.remove(size);
	}
	
	public void goNext (View view) {
		Intent intent = new Intent(this, CreateSpellResActivity.class);
		startActivity(intent);
	}
	

}
