package dudroid.dudumagicphone;

import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class Charm implements Serializable {

	private static final long serialVersionUID = -8514372400222397937L;
	public String spell;
	public CharmType type;
	
	private String result;
	//private Object[] params;
	private ArrayList<Serializable> paramList;
	
	public float symbolStartX;
	public float symbolStartY;
	
	public static int bitmapHeigh;
	public static int bitmapWidth;
	
	enum CharmType implements Serializable{
		PLAIN {
			@Override
			public String toString() {
				return "Speaking only";
			}
		}, 
		DRAW {
			@Override
			public String toString() {
				return "Speaking and drawing";
			}
		}, MOVE {
			@Override
			public String toString() {
				return "Speaking and waving";
			}
		}
	}
	
	public Charm (String spell, CharmType type) {
		this.spell = spell;
		this.type = type;
		paramList = new ArrayList<Serializable>();
	}
	
	public void setResultFunction (String name, ArrayList<Serializable> params) {
		result = name;
		paramList = params;
		/*if (params != null) {
			int len = params.length;
			this.params = new Object[len];
			
			for (int i=0; i<len; i++) {
				this.params[i] = params[i];
			}
		}*/
	}
	
	public String getResultName() {
		return result;
	}
	public Object[] getResultParams() {
		return (Object[])paramList.toArray();
	}
	
	public String getMD5hash() {
		String toCode = spell;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(toCode.getBytes());
			byte[] digest = md.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			return "Bitmap"+spell.hashCode();
		}
	}
	
	public Bitmap getBitmap(MyApplication app) {
		Bitmap bmp = null;
		try {
			FileInputStream fis =  app.openFileInput(getMD5hash());
			bmp = BitmapFactory.decodeStream(fis);
			return bmp;
		} catch (Exception e) {
		}
		return bmp;
	}
	
	public boolean cast() {
		boolean res;
		Object[] params = (Object[]) paramList.toArray(); //!
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i=0; i<params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}
		Class<?> charmResClass = CharmResult.class;
		try {
			charmResClass.getMethod(result, paramTypes).invoke(null, params);
			res = true;
		} catch (Exception e) {
			res = false;
		}
		return res;
	}
	

}
