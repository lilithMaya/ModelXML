package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class Converter {
	public static Object convert(Class<?> target, final Object[] value, Class<?>[] parameterType)
	{
		Object targetData = null;
		try {
			Constructor<?> constructor = target.getConstructor(parameterType);			
			targetData = constructor.newInstance(value);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException 
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetData;
	}
	
	public static Object convert(Class<?> target, final Object value)
	{
		if(target == int.class)
			return Integer.parseInt(value.toString());
		else if(target == boolean.class)
			return Boolean.parseBoolean(value.toString());
		else if (target == float.class)
			return Float.parseFloat(value.toString());
		else
			return value;
	}

}
