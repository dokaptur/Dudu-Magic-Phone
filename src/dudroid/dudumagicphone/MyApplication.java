package dudroid.dudumagicphone;

import java.io.*;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.app.Application;

public class MyApplication extends Application {
	
	public TreeMap<String, Charm> availCharms;
	public Charm tmpCharm;
	
	public MyApplication() {
		availCharms = new TreeMap<String,Charm>();
		
		// for debug only!!!
		Charm debug = new Charm("debug", CharmType.PLAIN);
		debug.setResultFunction("torch", new Integer[]{3,500,100});
		availCharms.put("debug",debug);
		
		try {
			FileInputStream fis = openFileInput("FileForCharms");
			ObjectInputStream is = new ObjectInputStream(fis);
			Object read;
			do {
				read = is.readObject();
				if (read != null && read instanceof Charm) {
					Charm toPut = (Charm) read;
					availCharms.put(toPut.spell, toPut);
				}
			}while (read != null);
			is.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
