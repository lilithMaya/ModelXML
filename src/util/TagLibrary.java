package util;

import java.util.HashMap;
import java.util.Map;

public abstract class TagLibrary {
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public void insertTag(String tag, String library) {
		this.map.put(tag, library);
	}
	
	public Class<?> getLibrary(String tag) {
		if(!this.map.containsKey(tag))
			return null;
		try {
			return Class.forName(this.map.get(tag));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
