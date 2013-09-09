package dudroid.dudumagicphone;

import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ShowSpellActivity extends Activity {
	
	public static final String CHARM_TYPE_CAST = "dudroid.dudumagicphone.CharmTypeCast";
	public static final String CHARM_NAME_CAST = "dudroid.dudumagicphone.CharmNameCast";

	TreeMap<String, Charm> availCharms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_spell);
		setupActionBar();
		
		Intent intent = getIntent();
		String name = intent.getStringExtra(BookMenuActivity.SPELL_TO_SHOW);
		TextView textView = (TextView) findViewById(R.id.show_spell_name);
		textView.setText(name.toUpperCase());
		
		availCharms = ((MyApplication) getApplication()).availCharms;
		Charm charm = availCharms.get(name);
		
		textView = (TextView) findViewById(R.id.show_spell_type);
		textView.setText(charm.type.toString());
		
		if (charm.type == CharmType.DRAW) {
			//set in Relative Layout
		} else if (charm.type == CharmType.MOVE) {
			// set in Relative Layout
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
		getMenuInflater().inflate(R.menu.show_spell, menu);
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
	
	public void tryToCast(View view) {
		Button btb = (Button) view;
		String name = btb.getText().toString();
		Charm charm = availCharms.get(name);
		int typeNr = charm.type.ordinal();
		
		Intent intent = new Intent(this, CastSpellActivity.class);
		intent.putExtra(CHARM_NAME_CAST, name);
		intent.putExtra(CHARM_TYPE_CAST, typeNr);
		startActivity(intent);
	}
	
	public void returnToMenu(View view) {
		Intent intent = new Intent(this, BookMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
