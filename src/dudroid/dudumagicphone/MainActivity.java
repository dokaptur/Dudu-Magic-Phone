package dudroid.dudumagicphone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void createSpell(View view) {
		Intent intent = new Intent(this, CreateSpellActivity.class);
		startActivity(intent);
	}
	public void readBook(View view) {
		Intent intent = new Intent(this, BookMenuActivity.class);
		startActivity(intent);
	}
	public void doMagic(View view) {
		Intent intent = new Intent(this, DoMagicMenuActivity.class);
		startActivity(intent);
	}
	
}
