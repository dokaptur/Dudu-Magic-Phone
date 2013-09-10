package dudroid.dudumagicphone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class DoMagicMenuActivity extends Activity {
	
	public static final String CHARM_TYPE = "dudroid.dudumagicphone.charmTypeFromDoMagic";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_do_magic_menu);
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
		getMenuInflater().inflate(R.menu.do_magic_menu, menu);
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
	
	public void castSpells(View view) {
		Intent intent = new Intent(this, CastSpellActivity.class);
		int id = view.getId();
		if (id == R.id.speaking_only_doMnu) {
			intent.putExtra(CHARM_TYPE, 0);
		} else if (id == R.id.speaking_drawing_doMnu) {
			intent.putExtra(CHARM_TYPE, 1);
		} else {
			intent.putExtra(CHARM_TYPE, 2);
		}
		startActivity(intent);
	}

}
