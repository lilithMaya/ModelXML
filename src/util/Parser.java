package util;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
 
import javax.swing.JTable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tags.Column;
import tags.Tab;

public class Parser {
	
	private Document doc;
	private Map<String, Object> tempData = new HashMap<String, Object>();
	private Stack<ObjectState> ancestors = new Stack<ObjectState>();
	private Map<String, Object> objects = new HashMap<String, Object>();
	private List<Object> buttongroup = new ArrayList<Object>();
			
	private Method findSetMethod(Object target, String field)
	{
		Method setMethod = null;
		Method targetGetMethod = null;
		try
		{
			if(field.equals("Height") || field.equals("Width"))
			{
				targetGetMethod = target.getClass().getMethod("getSize",new Class<?>[] {});
				field = "Size";
			}
			else
				targetGetMethod = target.getClass().getMethod("get" + field,new Class<?>[] {});
			Class<?> returnType = targetGetMethod.getReturnType();
			setMethod = target.getClass().getMethod("set" + field, returnType);
		} catch (NoSuchMethodException e) {
			try 
			{
				targetGetMethod = target.getClass().getMethod("is" + field,new Class<?>[] {});
				Class<?> returnType = targetGetMethod.getReturnType();
				setMethod = target.getClass().getMethod("set" + field, returnType);
				return setMethod;
			} catch (NoSuchMethodException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
		return setMethod;
	}
		
	private Object invokeSetMethod(Object target, Method setMethod, Object value, String field)
	{
		try 
		{
			switch(field)
			{
				case "Background":
					setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {Integer.parseInt(value.toString(),16)}, new Class<?>[] {int.class}));
					break;
				case "Font":
					setMethod.invoke(target,Font.decode((String) value));
					break;
				case "Height":
					if(this.tempData.containsKey("width"))
						setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {Integer.parseInt(this.tempData.get("width").toString()), 
							Integer.parseInt(value.toString())}, new Class<?>[] {int.class, int.class}));
					else
						this.tempData.put("height", value);
					break;
				case "Icon":
					setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {value}, new Class<?>[] {String.class}));
					break;
				case "Location":
					String [] data = ((String) value).split(",");
					setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {Integer.parseInt(data[0].trim()),Integer.parseInt(data[1].trim())}, 
							new Class<?>[] {int.class, int.class}));
					break;
				case "Layout":
					String className = "java.awt." + value;
					setMethod.invoke(target, Class.forName(className).newInstance());
					break;
				case "Width":
					if(this.tempData.containsKey("height"))
						setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
								new Object[] {Integer.parseInt(value.toString()), 
							Integer.parseInt(this.tempData.get("height").toString())}, 
							new Class<?>[] {int.class, int.class}));
					else
						this.tempData.put("width", value);
					break;
				default:
					setMethod.invoke(target, Converter.convert(setMethod.getParameterTypes()[0], value));
					break;				
			}
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException 
				| InstantiationException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return target;
	}
	
	private Object invokeAddMethod(Object parent, Object child) 
	{
		try {
			Method addMethod = null;
					
			//get appropriate add method
			if(child.getClass().getName().equals("javax.swing.JMenuBar"))
			{
				addMethod = parent.getClass().getMethod("setJMenuBar", child.getClass());
			}
			else if(parent.getClass().getName().equals("javax.swing.JScrollPane"))
			{
				addMethod = parent.getClass().getMethod("setViewportView",java.awt.Component.class);
			}
			else if(parent.getClass().getName().equals("javax.swing.JTabbedPane"))
			{
				addMethod = parent.getClass().getMethod("addTab",String.class, javax.swing.Icon.class, 
						java.awt.Component.class, String.class);
			}
			else if(parent.getClass().getName().equals("javax.swing.JTable"))
			{
				addMethod = ((JTable) parent).getModel().getClass().getMethod("addColumn", Object.class);
				//addMethod = ((JTable) parent).getTableHeader().getColumnModel().getClass().
						//getMethod("addColumn", ((Column) child).getColumn().getClass());
			}
			else if(parent.getClass().getName().equals("javax.swing.ButtonGroup"))
			{
				addMethod = parent.getClass().getMethod("add",javax.swing.AbstractButton.class);
				this.buttongroup.add(child);
			}
			else
			{
				addMethod = parent.getClass().getMethod("add",java.awt.Component.class);
			}
			
			//invoke add method
			if(child.getClass().getName().equals("javax.swing.ButtonGroup"))
			{
				while(!this.buttongroup.isEmpty())
					addMethod.invoke(parent, buttongroup.remove(0));
			}
			else if(child.getClass().getName().equals("tags.Tab"))
			{
				Tab tab = (Tab) child;
				addMethod.invoke(parent, tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getToolTipText());
			}
			else if(child.getClass().getName().equals("tags.Column"))
			{
				addMethod.invoke(((JTable) parent).getModel(), ((Column) child).getName());
				//addMethod.invoke(((JTable) parent).getTableHeader().getColumnModel(), ((Column) child).getColumn());
			}
			else
			{
				addMethod.invoke(parent, child);
			}
			
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;
	}
	
	private void read(InputStream inputStream) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(inputStream);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Parser()
	{
		
	}
		
	public Object parse(InputStream inputStream) {
		Object targetObject = null;
		try
		{
			this.read(inputStream);
			SwingTagLibrary libraries = new SwingTagLibrary();
			Element currentNode = this.doc.getDocumentElement();		
			int index = 0;
			String id = null;
			while(currentNode != null)
			{
				boolean suspend = false;
				
				//initialize attributes
				if(index == 0)
				{
					targetObject = libraries.getLibrary(currentNode.getNodeName()).newInstance();
					id = null;
					NamedNodeMap attributes = currentNode.getAttributes();
					for(int i = 0; i < attributes.getLength(); i++)
					{
						Attr attr = (Attr)attributes.item(i);
						String attrName = attr.getNodeName();
						String field = attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
						String value = attr.getNodeValue();
						if(field.equals("Id"))
							id = value;
						else
						{
							Method setMethod = this.findSetMethod(targetObject, field);
							if(setMethod != null)
								this.invokeSetMethod(targetObject, setMethod, value, field);
						}
					}
				}
				
				//initialize child elements
				NodeList children = currentNode.getChildNodes();
				for(int i = index; i < children.getLength(); i++)
				{
					if(libraries.getLibrary(children.item(i).getNodeName()) != null)
					{
						this.ancestors.push(new ObjectState(currentNode, i + 1, targetObject, id));
						suspend = true;
						index = 0;
						currentNode = (Element) children.item(i);
						break;
					}
				}

				if(!suspend)
				{
					if(id != null)
						this.objects.put(id, targetObject);
					if(!this.ancestors.isEmpty())
					{
						ObjectState ancestor = this.ancestors.pop();
						Object parent = ancestor.getTargetObject();
						targetObject = this.invokeAddMethod(parent, targetObject);
						currentNode = (Element) ancestor.getObject();
						index = ancestor.getCurrentIndex();
						id = ancestor.getId();
					}
					else
					{
						currentNode = null;
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InstantiationException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetObject;
	}
	
	public Map<String, Object> getObjects()
	{
		return this.objects;
	}
		
}