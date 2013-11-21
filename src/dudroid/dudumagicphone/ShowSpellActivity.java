package dudroid.dudumagicphone;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;


public class ShowSpellActivity extends Activity {
	
	public static final String CHARM_NAME_CAST = "dudroid.dudumagicphone.CharmNameCast";

	TreeMap<String, Charm> availCharms;
	String charmName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_spell);
		setupActionBar();
		
		Intent intent = getIntent();
		String name = intent.getStringExtra(BookMenuActivity.SPELL_TO_SHOW);
		charmName = name;
		TextView textView = (TextView) findViewById(R.id.show_spell_name);
		textView.setText(name.toUpperCase());
		
		availCharms = ((MyApplication) getApplication()).availCharms;
		Charm charm = availCharms.get(name);
		
		textView = (TextView) findViewById(R.id.show_spell_type);
		textView.setText(charm.type.toString());
		
		if (charm.type == CharmType.DRAW) {
			LinearLayout lay = (LinearLayout) findViewById(R.id.move_or_draw_show);
			
			TextView txt = (TextView) findViewById(R.id.magic_symbol_txt);
			txt.setText("Magic Symbol of spell:");
			txt.setVisibility(View.VISIBLE);
			
			ImageView imv = new ImageView(this);
			imv.setLayoutParams(new LayoutParams(MyDrawView.width, MyDrawView.heihg));
			imv.setImageBitmap(charm.getBitmap((MyApplication) getApplication()));
			lay.addView(imv);
		} else if (charm.type == CharmType.MOVE) {
			
			TextView txt = (TextView) findViewById(R.id.magic_symbol_txt);
			txt.setText("List of Rotation:");
			txt.setVisibility(View.VISIBLE);
			
			LinearLayout lay = (LinearLayout) findViewById(R.id.lin_lay_rot2);
			ArrayList<Charm.MyRotation> list = charm.rotation;
			String[] Axs = new String[] {"x", "y", "z"};
			
			for (int i=0; i<list.size(); i++) {
				TextView line = new TextView(this);
				String str = list.get(i).degrees + " degrees around " + Axs[list.get(i).axis] + " axis";
				line.setText(str);
				line.setTextAppearance(this, android.R.style.TextAppearance_Medium);
				lay.addView(line);
			}
			
			ScrollView scroll = (ScrollView) findViewById(R.id.scroll_rot2);
			scroll.setVisibility(View.VISIBLE);
			
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
		Intent intent = new Intent(this, CastSpellActivity.class);
		intent.putExtra(CHARM_NAME_CAST, charmName);
		startActivity(intent);
	}
	
	private void yesDelete() {
		Charm charm = availCharms.get(charmName);
		if (charm.type == CharmType.DRAW) {
			File file = new File(this.getFilesDir(), charm.getMD5hash());
			if (file != null) {
				file.delete();
			}
		}
		availCharms.remove(charmName);
		((MyApplication) getApplication()).serializeCharms();
		
		Intent intent = new Intent(this, BookMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void deleteSpell(View view) {
		
		AlertDialog.Builder adBuild = new AlertDialog.Builder(this);
		adBuild.setCancelable(false);
		//adBuild.setTitle("Deleting a spell");
		adBuild.setMessage("Are you sure you want to delete this spell?");
		adBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ShowSpellActivity.this.yesDelete();
				//dialog.cancel();
			}
		});
		adBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog = adBuild.create();
		dialog.show();
		
	}

}
