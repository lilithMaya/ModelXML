package sample.model;

import java.util.HashMap;
import java.util.Map;

public class ExtensionMapper {
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	private void insertTag(String ext, String type) {
		map.put(ext, type);
	}
	
	public String getType(String ext) {
		if(map.containsKey(ext)) {
			return map.get(ext);
		}
		else
			return "Unknown";
	}
	
	public ExtensionMapper() {
		this.insertTag("java", "Java");
		this.insertTag("cpp", "C++");
		this.insertTag("py", "Python");
	}

}
