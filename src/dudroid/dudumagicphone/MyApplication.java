package dudroid.dudumagicphone;

import java.io.*;
import java.util.TreeMap;

import android.app.Application;

public class MyApplication extends Application {
	
	public TreeMap<String, Charm> availCharms;
	public Charm tmpCharm;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		availCharms = new TreeMap<String,Charm>();
		
		try {
			FileInputStream fis = openFileInput("FileForCharms");
			ObjectInputStream is = new ObjectInputStream(fis);
			Object read;
			
			read = is.readObject();
			if (read != null ) { 
				availCharms = (TreeMap <String, Charm>) read;
			}
			
			is.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void serializeCharms() {
		try {
			FileOutputStream fos = openFileOutput("FileForCharms", MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(availCharms);
			os.flush(); os.close();
			fos.flush(); fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
