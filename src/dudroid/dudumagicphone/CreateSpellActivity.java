package dudroid.dudumagicphone;

import java.util.ArrayList;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;

public class CreateSpellActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_spell);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_spell, menu);
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
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	
	public void speak(View view) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
		intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
			if(resultCode == RESULT_OK) {
				ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Spinner spinner = (Spinner) findViewById(R.id.recognitions);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item , textMatchList);
				adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
				spinner.setAdapter(adapter);
			}
		}
	}
	
	public void goNext(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.recognitions);
		RadioGroup radiogr = (RadioGroup) findViewById(R.id.spell_types);
		if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please say your spell", Toast.LENGTH_SHORT).show();
			return;
		} 
		int type = radiogr.getCheckedRadioButtonId();
		if (type == -1) {
			Toast.makeText(this, "Please check a type of your spell", Toast.LENGTH_SHORT).show();
			return;
		} 
		
		String spell = spinner.getSelectedItem().toString();
		
		TreeMap<String, Charm> availCharms = ((MyApplication) getApplication()).availCharms;
		if (availCharms.containsKey(spell)) {
			Toast.makeText(this, "A spell with such incantation already exist. Please say different spell", Toast.LENGTH_SHORT).show();
			return;
		}
		Charm myCharm;
		if (type == R.id.speking_id) {
			myCharm = new Charm (spell, CharmType.PLAIN);
		} else if (type == R.id.speking_drawing_id) {
			myCharm = new Charm (spell, CharmType.DRAW);
		} else {
			myCharm = new Charm (spell, CharmType.MOVE);
		}
		((MyApplication) getApplication()).tmpCharm = myCharm;
		
		Intent intent;
		if (type == R.id.speking_id) {
			intent = new Intent (this, CreateSpellResActivity.class);
		} else if (type == R.id.speking_drawing_id) {
			intent = new Intent (this, CreateSpellDrawActivity.class);
		} else {
			intent = new Intent (this, CreateSpellMoveActivity.class);
		}
		startActivity(intent);
	}

	

}
