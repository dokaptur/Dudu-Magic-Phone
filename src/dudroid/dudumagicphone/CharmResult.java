package dudroid.dudumagicphone;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class CharmResult {
	
	public static void torch (Integer times, Integer duration, Integer breaks) {
		Camera camera = Camera.open();
		Parameters params = camera.getParameters();
		for (int i=0; i<times.intValue(); i++) {
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			
			try {
				Thread.sleep(duration.intValue());
			} catch (InterruptedException e) {
			}
			camera.stopPreview();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			if (i < times-1) {
				try {
					Thread.sleep(breaks.intValue());
				} catch (InterruptedException e) {
				}
			}
		}
		camera.release();
	}
	

}
