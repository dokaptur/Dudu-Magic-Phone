package dudroid.dudumagicphone;

import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class CreateSpellDrawActivity extends Activity implements OnTouchListener {
	
	MyDrawView drawView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_spell_draw);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		LinearLayout lay = (LinearLayout) findViewById(R.id.draw_field_view);
		MyDrawView view = new MyDrawView(this);
		view.setLayoutParams(new LinearLayout.LayoutParams(MyDrawView.heihg, MyDrawView.width));
		view.setBackgroundColor(MyDrawView.color);
		lay.addView(view);
		drawView = view;
		
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
		getMenuInflater().inflate(R.menu.create_spell_draw, menu);
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
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void resetPaths(View view) {
		drawView.resetPaths();
	}
	
	public void goNext(View view) {
		
		drawView.setDrawingCacheEnabled(true);
		Bitmap bmp = drawView.getDrawingCache().copy(Config.ARGB_8888, false);
		drawView.setDrawingCacheEnabled(false);
		
		Charm charm = ((MyApplication) getApplication()).tmpCharm;
		charm.symbolStartX = drawView.symbolStartX;
		charm.symbolStartY = drawView.symbolStartY;
		
		String filename = charm.getMD5hash();
		try {
			FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
		}
		Intent intent = new Intent(this, CreateSpellResActivity.class);
		startActivity(intent);
	}
	

}
