package dudroid.dudumagicphone;

import java.io.*;
import java.util.TreeSet;

import dudroid.dudumagicphone.Charm.CharmType;
import android.app.Application;

public class MyApplication extends Application {
	
	public TreeSet<Charm> availCharms;
	public Charm tmpCharm;
	
	public MyApplication() {
		availCharms = new TreeSet<Charm>();
		
		// for debug only!!!
		Charm debug = new Charm("debug", CharmType.PLAIN);
		debug.setResultFunction("torch", new Integer[]{3,500,100});
		availCharms.add(debug);
		
		try {
			FileInputStream fis = openFileInput("FileForCharms");
			ObjectInputStream is = new ObjectInputStream(fis);
			Object read;
			do {
				read = is.readObject();
				if (read != null && read instanceof Charm) {
					availCharms.add((Charm) read);
				}
			}while (read != null);
			is.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
