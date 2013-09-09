package dudroid.dudumagicphone;

import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.speech.RecognitionListener;
import android.support.v4.app.NavUtils;

public class CastSpellActivity extends Activity {

	Charm correct;
	CharmType type;
	TreeMap <String, Charm> availCharms;
	
	class MyListener implements RecognitionListener {

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
		public void onResults(Bundle results) {}

		@Override
		public void onRmsChanged(float rmsdB) {}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cast_spell);
		// Show the Up button in the action bar.
		setupActionBar();
		
		availCharms = ((MyApplication) getApplication()).availCharms;
		
		Intent intent = getIntent();
		if (intent.hasExtra(ShowSpellActivity.CHARM_TYPE_CAST)) { //activity opened from ShowSpellActivity
			type = CharmType.values()[intent.getIntExtra(ShowSpellActivity.CHARM_TYPE_CAST, 0)];
			if (intent.hasExtra(ShowSpellActivity.CHARM_NAME_CAST)) {
				correct = availCharms.get(intent.getStringExtra(ShowSpellActivity.CHARM_NAME_CAST));
			} else {
				correct = null;
			}
		} else {
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
		
	}

}
