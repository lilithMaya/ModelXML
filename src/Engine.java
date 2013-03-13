import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import util.Parser;

public class Engine {
	
	private Parser parser;
	private Object layout;
	private Map<String, Object> objects = new HashMap<String, Object>();
	
	public Engine()
	{
		parser = new Parser();
	}
	
	public void setLayout(InputStream inputStream) {
		this.layout = this.parser.parse(inputStream);
		this.objects = this.parser.getObjects();	
	}
	
	public void render()
	{
		try 
		{
			Method setMethod = this.layout.getClass().getMethod("setVisible", boolean.class);
			setMethod.invoke(this.layout, true);
		} catch (NoSuchMethodException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public Object findViewById(String id)
	{
		return this.objects.get(id);
	}
	
	public void killLayout()
	{
		try 
		{
			Method setMethod = this.layout.getClass().getMethod("dispose", new Class<?>[] {});
			setMethod.invoke(this.layout, new Object[] {});
		} catch (NoSuchMethodException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
