package util;

import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LayoutConverter {
	
	public static Object convert(Object parent, Object constraint)
	{
		Object result = null;
		try {
			Method getLayout = parent.getClass().getMethod("getLayout", new Class<?>[] {});
			String name = getLayout.invoke(parent, new Object[] {}).getClass().getName();
			switch(name)
			{
				case "java.awt.BorderLayout":
					return convertBorderLayout(constraint);
				default:
					break;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return result;
	}
	
	public static Object convertBorderLayout(Object constraint)
	{
		if(constraint == null)
			return null;
		String value = constraint.toString();
	    Field[] fields = BorderLayout.class.getFields();
	    for(int i = 0; i < fields.length; i++) 
	    {
	    	if(value.endsWith( fields[i].getName() )) 
	    	{
	    		try {
					return fields[i].get( BorderLayout.class );
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		break;
	    	}
	    }
	    return null;
	}
}
