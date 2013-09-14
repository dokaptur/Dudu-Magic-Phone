package dudroid.dudumagicphone;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View {
	
	public static int heihg = 1000;
	public static int width = 1000;
	public static int color = Color.rgb(128, 0, 255);
	
	public float symbolStartX;
	public float symbolStartY;
	
	public boolean enabled = true;
	
	ArrayList<Path> pathList;
	Path path;
	Paint paint;
	boolean firstTap = true;
	
	public MyDrawView(Context context) {
		super(context);
		pathList = new ArrayList<Path>();
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(40);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (enabled) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN :
				path = new Path();
				if (firstTap) {
					firstTap = false;
					symbolStartX = event.getX();
					symbolStartY = event.getY();
				}
				path.moveTo(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_MOVE :
				path.lineTo(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP :
				pathList.add(path);
				break;
			}
			invalidate();
		}
		return true;
	}
	
	@Override
	protected void onDraw (Canvas can) {
		//canvas = can;
		for (Path p : pathList) {
			can.drawPath(p, paint);
		}
		if (path != null) {
			can.drawPath(path, paint);
		}
	}
	
	public void resetPaths() {
		pathList = new ArrayList<Path>();
		path = null;
		firstTap = true;
		invalidate();
	}
	
	public ArrayList<Path> getPathList() {
		return pathList;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaintColor (int color) {
		paint.setColor(color);
	}

}
