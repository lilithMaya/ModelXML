package util;

import java.io.InputStream;
import java.util.Map;

public interface Parser {
	
	public Object parse(InputStream inputStream);
	public Map<String, Object> getObjects();

}
