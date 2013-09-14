package dudroid.dudumagicphone;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class CreateSpellResActivity extends Activity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_spell_res);
		setupActionBar();
		
		Spinner spinner = (Spinner) findViewById(R.id.results);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.results_spinner, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		View flashOpt = findViewById(R.id.light_preferences);
		flashOpt.setVisibility(View.GONE);
	}

	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_spell_res, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		View flashOpt = findViewById(R.id.light_preferences);
		if (pos == 0) {
			flashOpt.setVisibility(View.VISIBLE);
		} else {
			flashOpt.setVisibility(View.GONE);
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void trySpell (View view) {
		Spinner spinner = (Spinner) findViewById(R.id.results);
		if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please choose the result of your spell", Toast.LENGTH_SHORT).show();
			return;
		} 
		if (spinner.getSelectedItemPosition() == 0) {
			Integer[] params = getTorchParameters();
			CharmResult.torch(params[0], params[1], params[2]);
		} 
	}
	
	private Integer[] getTorchParameters() {
		Integer[] params = new Integer[3];
		EditText edTxt = (EditText) findViewById(R.id.times);
		int times = 1;
		String txt = edTxt.getText().toString();
		if (!txt.equals("")) {
			times = Integer.parseInt(txt);
		}
		params[0] = times;
		edTxt = (EditText) findViewById(R.id.duration);
		int duration = 1000;
		txt = edTxt.getText().toString();
		if (!txt.equals("")) {
			duration = Integer.parseInt(txt);
		}
		params[1] = duration;
		edTxt = (EditText) findViewById(R.id.breakes);
		int breaks = 1000;
		txt = edTxt.getText().toString();
		if (!txt.equals("")) {
			breaks = Integer.parseInt(txt);
		}
		params[2] = breaks;
		return params;
	}
	
	public void spellDone(View view) {
		Charm myCharm = ((MyApplication) getApplication()).tmpCharm;
		Spinner spinner = (Spinner) findViewById(R.id.results);
		if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please choose the result of your spell", Toast.LENGTH_SHORT).show();
			return;
		} 
		if (spinner.getSelectedItemPosition() == 0) {
			Integer[] params = getTorchParameters();
			
			ArrayList<Serializable> paramList = new ArrayList<Serializable>();
			for (int i=0; i<params.length; i++) {
				paramList.add((Serializable) params[i]);
			}
			
			myCharm.setResultFunction("torch", paramList);
		}
		
		TreeMap<String, Charm> availCharms = ((MyApplication) getApplication()).availCharms;
		availCharms.put(myCharm.spell, myCharm);
		try {
			FileOutputStream fos = openFileOutput("FileForCharms", MODE_APPEND);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(myCharm);
			os.flush(); os.close();
			fos.flush(); fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
