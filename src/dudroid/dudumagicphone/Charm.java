package dudroid.dudumagicphone;

import java.io.Serializable;


public class Charm implements Serializable {

	private static final long serialVersionUID = 1L;
	public String spell;
	public CharmType type;
	
	private String result;
	private Object[] params;
	
	enum CharmType {
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
	}
	
	public void setResultFunction (String name, Object[] params) {
		result = name;
		if (params != null) {
			int len = params.length;
			this.params = new Object[len];
			
			for (int i=0; i<len; i++) {
				this.params[i] = params[i];
			}
		}
	}
	
	public String getResultName() {
		return result;
	}
	public Object[] getResultParams() {
		return params;
	}

	
	

}
