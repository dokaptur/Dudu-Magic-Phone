package dudroid.dudumagicphone;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

import dudroid.dudumagicphone.Charm.CharmType;
import android.app.Application;

public class MyApplication extends Application {
	
	public TreeMap<String, Charm> availCharms;
	public Charm tmpCharm;
	
	public MyApplication() {
		availCharms = new TreeMap<String,Charm>();
		
		// for debug only!!!
		Charm debug = new Charm("go", CharmType.PLAIN);
		ArrayList<Serializable> paramList = new ArrayList<Serializable>();
		paramList.add((Serializable) Integer.valueOf(3));
		paramList.add((Serializable) Integer.valueOf(500));
		paramList.add((Serializable) Integer.valueOf(100));
		debug.setResultFunction("torch", paramList);
		availCharms.put("go",debug);
		
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
			/*FileOutputStream fos = openFileOutput("FileForCharms", MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(debug);
			os.close();*/
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
