package dudroid.dudumagicphone;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class BookMenuActivity extends Activity implements OnClickListener {
	
	public static final String SPELL_TO_SHOW = "dudroid.dudumagicphone.SpellToShow";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_menu);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TreeMap<String, Charm> availCharms = ((MyApplication) getApplication()).availCharms;
		List<String> plainNames = new ArrayList<String>();
		List<String> drawNames = new ArrayList<String>();
		List<String> moveNames = new ArrayList<String>();
		
		for (String name: availCharms.keySet()) {
			Charm charm = availCharms.get(name);
			if (charm.type == CharmType.PLAIN) {
				plainNames.add(charm.spell);
			} else if (charm.type == CharmType.DRAW){
				drawNames.add(charm.spell);
			} else {
				moveNames.add(charm.spell);
			}
		}
		
		fillLayout(R.id.speaking_only_list, plainNames);
		fillLayout(R.id.speaking_drawing_list, drawNames);
		fillLayout(R.id.speaking_waving_list, moveNames);
	}
	
	private void fillLayout (int id, List<String> names) {
		
		LinearLayout layout = (LinearLayout) findViewById(id);
		for (String spell : names) {
			Button button = new Button(this);
			button.setText(spell);
			button.setOnClickListener(this);
			//button.setBackgroundColor(Color.parseColor("#A9F5D0"));
			//button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			layout.addView(button);
			
		}
		layout.setVisibility(View.GONE);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.book_menu, menu);
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
	
	
	public void showSpells (View view) {
		SparseIntArray map = new SparseIntArray();
		map.put(R.id.speaking_drawing_book, R.id.speaking_drawing_list);
		map.put(R.id.speaking_only_book, R.id.speaking_only_list);
		map.put(R.id.speaking_waving_book, R.id.speaking_waving_list);
		
		int clickedId = ((Button) view).getId();
		LinearLayout toshow = (LinearLayout) findViewById(map.get(clickedId));
		if (toshow.getVisibility() == View.VISIBLE) {
			toshow.setVisibility(View.GONE);
		} else {
			toshow.setVisibility(View.VISIBLE);
		}
		
		for (int i=0; i<map.size(); i++) {
			int key = map.keyAt(i);
			if (key != clickedId) {
				LinearLayout tohide = (LinearLayout) findViewById(map.get(key));
				tohide.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View view) {
		Intent intent = new Intent(this, ShowSpellActivity.class);
		intent.putExtra(SPELL_TO_SHOW, ((Button) view).getText().toString());
		startActivity(intent);
	}

}
