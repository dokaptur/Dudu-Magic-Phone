package dudroid.dudumagicphone;

import java.util.ArrayList;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.NavUtils;

public class CastSpellActivity extends Activity implements RecognitionListener, SensorEventListener {

	Charm correct;
	Charm casted;
	CharmType type;
	TreeMap <String, Charm> availCharms;
	boolean fromBook;
	SpeechRecognizer reco;
	MyDrawView drawView;
	Drawable background;
	int colorPaint;
	
	boolean rotationEnabled;
	Charm.MyRotation currentRotation;
	int rotationIndex;
	Sensor gyro;
	SensorManager sManage;
	float way [][] = new float[3][2];
	
	static final float NS2S = 1.0f / 1000000000.0f;
	static final float D2R = (float)Math.PI / 180f;
    float timestamp;
    float err = (float) 45 * D2R;
    
	
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

	@SuppressWarnings("deprecation")
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
			casted = availCharms.get(match);
			if (type == CharmType.PLAIN){
				boolean res = casted.cast();
				if (res) {
					Toast.makeText(this, "Great! You did it! You are a wizard!", Toast.LENGTH_SHORT);
				} else {
					Toast.makeText(this, "Sorry, unexpected error", Toast.LENGTH_SHORT);
				}
			} else if (type == CharmType.DRAW) {
				String textToDraw = "Great! Now draw a magic symbol of " + casted.spell.toUpperCase();
				Toast.makeText(this, textToDraw, Toast.LENGTH_SHORT).show();
				background = new BitmapDrawable(getResources(), casted.getBitmap((MyApplication) getApplication()));
				drawView.setBackgroundDrawable(background);
				drawView.enabled = true;
				Button isd = (Button) findViewById(R.id.isdone_btn_casting);
				isd.setEnabled(true);
			} else {
				String textToDraw = "Great! Now rotate our phone!";
				Toast.makeText(this, textToDraw, Toast.LENGTH_SHORT).show();
				LinearLayout lay = (LinearLayout) findViewById(R.id.lin_lay_rot3);
				ArrayList<Charm.MyRotation> list = casted.rotation;
				String[] Axs = new String[] {"x", "y", "z"};
				
				for (int i=0; i<list.size(); i++) {
					TextView line = new TextView(this);
					String str = list.get(i).degrees + " degrees around " + Axs[list.get(i).axis] + " axis";
					line.setText(str);
					line.setTextAppearance(this, android.R.style.TextAppearance_Medium);
					if (i == 0) {
						line.setTextColor(Color.RED);
					}
					lay.addView(line);
				}
				
				ScrollView scroll = (ScrollView) findViewById(R.id.scroll_rot3);
				scroll.setVisibility(View.VISIBLE);
				
				Button isd = (Button) findViewById(R.id.isdone_btn_casting);
				isd.setText("Try again \nred rotation");
				isd.setVisibility(View.VISIBLE);
				isd.setEnabled(true);
			
				rotationEnabled = true;
				timestamp = 0;
				currentRotation = list.get(0);
				rotationIndex = 0;
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
		colorPaint = Color.rgb(245,188, 169);
		rotationEnabled = false;
		
		sManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		gyro = sManage.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
		Intent intent = getIntent();
		if (intent.hasExtra(ShowSpellActivity.CHARM_NAME_CAST)) { //activity opened from ShowSpellActivity
			fromBook = true;
			correct = availCharms.get(intent.getStringExtra(ShowSpellActivity.CHARM_NAME_CAST));
			type = correct.type;
		} else {  //activity opened from "Do Magic!"
			correct = null;
			fromBook = false;
			CharmType[] typeTable = CharmType.values();
			int index = intent.getIntExtra(DoMagicMenuActivity.CHARM_TYPE, 0);
			type = typeTable[index];
		}
		//TODO: if type != PLAIN ...
		if (type == CharmType.DRAW) {
			drawView = new MyDrawView(this);
			LinearLayout lay = (LinearLayout) findViewById(R.id.casting_lay);
			drawView.setLayoutParams(new LinearLayout.LayoutParams(MyDrawView.heihg, MyDrawView.width));
			drawView.setBackgroundColor(MyDrawView.color);
			lay.addView(drawView);
			drawView.enabled = false;
			drawView.setPaintColor(colorPaint);
			
			Button isd = (Button) findViewById(R.id.isdone_btn_casting);
			isd.setText("Drawing is done!");
			isd.setVisibility(View.VISIBLE);
			isd.setEnabled(false);
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    sManage.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    sManage.unregisterListener(this);
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
		if (type == CharmType.DRAW) {
			drawView.resetPaths();
			drawView.setBackgroundColor(MyDrawView.color);
		}
		
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
			intent = new Intent(this, DoMagicMenuActivity.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		
	}
	
	public void isDone(View view) {
		if (type == CharmType.DRAW) {
			drawView.enabled = false;
			((Button) findViewById(R.id.isdone_btn_casting)).setEnabled(false);
			boolean ok = checkBitmaps();
			if (ok) {
				boolean res = casted.cast();
				if (res) {
					Toast.makeText(this, "Great! You did it! You are a wizard!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Sorry, unexpected error", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Sorry, your symbol is wrong. Try again!", Toast.LENGTH_SHORT).show();
			}
		} else if (type == CharmType.MOVE) { // we want to try "red rotation" again 
			timestamp = 0;
		}
	}
	
	private boolean checkBitmaps() {
		float offX = casted.symbolStartX - drawView.symbolStartX;
		float offY = casted.symbolStartY - drawView.symbolStartY;
		
		ArrayList<Path> original = drawView.getPathList();
		
		Bitmap okBmp = casted.getBitmap((MyApplication) getApplication());
		Bitmap drawnBmp = Bitmap.createBitmap(MyDrawView.width, MyDrawView.heihg, Bitmap.Config.ARGB_8888);
		
		drawnBmp.eraseColor(MyDrawView.color); 
		Canvas canvas = new Canvas(drawnBmp);
		Paint paint = new Paint(drawView.getPaint());
		paint.setColor(Color.WHITE);
		
		for (Path path: original) {
			Path npath = new Path(path); //!!!
			path.offset(offX, offY, npath);
			canvas.drawPath(npath, paint);
		}
		
		int white = Color.WHITE;
		int blue = MyDrawView.color;
		int whiteCount = 0;
		int aNb = 0;
		int bNa = 0;
		
		for (int i=0; i<MyDrawView.width; i+=5) {
			for (int j=0; j<MyDrawView.heihg; j+=5) {
				int pxa = okBmp.getPixel(i, j);
				int pxb = drawnBmp.getPixel(i, j);
				
				if (pxa == white) {
					whiteCount ++;
					if (pxb == blue) {
						aNb ++;
					}
				} else {
					if (pxb == white) {
						bNa ++;
					}
				}
			}
		}
		if (((float)aNb)/((float) whiteCount) < 0.5 && ((float)bNa)/((float) whiteCount) < 0.5) {
			return true;
		}
		return false;
		
		//return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void nextRotation() {
		LinearLayout lay = (LinearLayout) findViewById(R.id.lin_lay_rot3);
		TextView rot = (TextView) lay.getChildAt(rotationIndex);
		rot.setTextColor(Color.GREEN);
		if (rotationIndex == lay.getChildCount()-1) {
			rotationEnabled = false;
			boolean res = casted.cast();
			if (res) {
				Toast.makeText(this, "Great! You did it! You are a wizard!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Sorry, unexpected error", Toast.LENGTH_SHORT).show();
			}
		} else {
			rotationIndex++;
			currentRotation = casted.rotation.get(rotationIndex);
			((TextView) lay.getChildAt(rotationIndex)).setTextColor(Color.RED);
			Toast.makeText(this, "Roatation accepted! Do next one!", Toast.LENGTH_SHORT).show();
			timestamp = 0;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (!rotationEnabled) return;
		if (timestamp == 0) {
			for (int i=0; i<3; i++) {
				way[i][0] = 0; // way during rotation
				way[i][1] = currentRotation.axis == i ? (float) currentRotation.degrees * D2R : 0; // way I have to reach to "pass" rotation
			}
			timestamp = event.timestamp;
		} else {
			final float dT = (event.timestamp - timestamp) * NS2S;
			boolean finished = true;
			
			for (int i=0; i<3; i++) {
				way[i][0] += event.values[i] * dT;
				finished = finished && Math.abs(way[i][0] - way[i][1]) < err;
			}
			if (finished) {
				nextRotation();
			} else {
				timestamp = event.timestamp;
			}
		}
		
	}

}
