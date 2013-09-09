package dudroid.dudumagicphone;

import java.io.Serializable;


public class Charm implements Serializable, Comparable<Charm> {

	private static final long serialVersionUID = 1L;
	public String spell;
	public CharmType type;
	
	private String result;
	private Object[] params;
	
	enum CharmType {PLAIN, DRAW, MOVE}
	
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

	@Override
	public int compareTo(Charm another) {
		return this.spell.compareTo(another.spell);
	}
	
	@Override
	public boolean equals (Object another) {
		if (!(another instanceof Charm)) {
			return false;
		}
		Charm anth = (Charm) another;
		if (this.compareTo(anth)==0) return true;
		return false;
	}
	

}
