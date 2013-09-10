package dudroid.dudumagicphone;

import java.util.ArrayList;
import java.util.TreeMap;
import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NavUtils;

public class CastSpellActivity extends Activity implements RecognitionListener {
	
	public static final String RETURN_MESSAGE = "dudroid.dudumagicphone.returnFromCastMessage";
	public static final String RETURN_STATE =  "dudroid.dudumagicphone.returnFromCastState";

	Charm correct;
	CharmType type;
	TreeMap <String, Charm> availCharms;
	boolean fromBook;
	SpeechRecognizer reco;
	
	//<SpeechRecognizer
	@Override
	public void onBeginningOfSpeech() {}

	@Override
	public void onBufferReceived(byte[] buffer) {}

	@Override
	public void onEndOfSpeech() {}

	@Override
	public void onError(int error) {}

	@Override
	public void onEvent(int eventType, Bundle params) {}

	@Override
	public void onPartialResults(Bundle partialResults) {}

	@Override
	public void onReadyForSpeech(Bundle params) {}

	@Override
	public void onResults(Bundle data) {
		ArrayList<String> matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		String match = matches.get(0);
		TextView text = (TextView) findViewById(R.id.speech_result);
		text.setText(match);
		
		if (availCharms.containsKey(match) && availCharms.get(match).type.equals(type)) {
			if (correct != null && !correct.spell.equals(match)) {
				Toast.makeText(this, "It's not the spell you wanted to cast! Try again!", Toast.LENGTH_SHORT).show();
				return;
			}
			Charm charm = availCharms.get(match);
			Object[] params = charm.getResultParams();
			String resultName = charm.getResultName();
			
			Class<?>[] paramTypes = new Class<?>[params.length];
			for (int i=0; i<params.length; i++) {
				paramTypes[i] = params[i].getClass();
			}
			
			Class<?> charmResClass = CharmResult.class;
			try {
				charmResClass.getMethod(resultName, paramTypes).invoke(null, params);
				Toast.makeText(this, "Great! You did it! You are a wizard!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(this, "Sorry, unexpected error", Toast.LENGTH_SHORT);
			}
		} else {
			Toast.makeText(this, "There is no such a spell! Try again!", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public void onRmsChanged(float rmsdB) {}
	// SpeechRecognizer />	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cast_spell);
		// Show the Up button in the action bar.
		setupActionBar();
		
		availCharms = ((MyApplication) getApplication()).availCharms;
		
		Intent intent = getIntent();
		if (intent.hasExtra(ShowSpellActivity.CHARM_TYPE_CAST)) { //activity opened from ShowSpellActivity
			fromBook = true;
			//type = CharmType.values()[intent.getIntExtra(ShowSpellActivity.CHARM_TYPE_CAST, 0)];
			correct = availCharms.get(intent.getStringExtra(ShowSpellActivity.CHARM_NAME_CAST));
			type = correct.type;
		} else {
			correct = null;
			fromBook = false;
			//activity opened from "Do Magic!"
		}
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
		getMenuInflater().inflate(R.menu.cast_spell, menu);
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
	
	public void castSpell (View view) {
		reco = SpeechRecognizer.createSpeechRecognizer(this);
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		//intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		reco.setRecognitionListener(this);
		reco.startListening(intent);
	}
	
	public void goBackDD (View view) {
		
		Intent intent = null;
		if (fromBook) {
			intent = new Intent(this, ShowSpellActivity.class);
			intent.putExtra(BookMenuActivity.SPELL_TO_SHOW, correct.spell);
		} else {
			//TODO: DoMagicMenu.class
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}

}
