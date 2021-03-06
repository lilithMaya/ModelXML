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
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tags.AbstractTag;

public class SwingParser implements Parser{
	
	private Document doc;
	private Stack<ObjectState> ancestors = new Stack<ObjectState>();
	private Map<String, Object> objects = new HashMap<String, Object>();
	private List<Object> buttongroup = new ArrayList<Object>();
	private Stack<Object> constraints = new Stack<Object>();
	private ExtendTagLibrary extendTagLibrary = new ExtendTagLibrary();
	private SwingTagLibrary libraries = new SwingTagLibrary();
			
	private Method findSetMethod(Object target, String field)
	{
		Method setMethod = null;
		Method targetGetMethod = null;
		try
		{
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
				case "Icon":
					setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {value}, new Class<?>[] {String.class}));
					break;
				case "Location":case "Size":case "PreferredSize":case "MaximumSize":case "MinimumSize":
					String [] data = ((String) value).split(",");
					setMethod.invoke(target,Converter.convert(setMethod.getParameterTypes()[0],
							new Object[] {Integer.parseInt(data[0].trim()),Integer.parseInt(data[1].trim())}, 
							new Class<?>[] {int.class, int.class}));
					break;
				case "Layout":
					setMethod.invoke(target, LayoutConverter.createLayout(target, (String) value));
					break;
				default:
					setMethod.invoke(target, Converter.convert(setMethod.getParameterTypes()[0], value));
					break;				
			}
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return target;
	}
	
	private Object invokeAddMethod(Object parent, Object child, Object constraint) 
	{
		try {
			Method addMethod = null;
					
			//get appropriate add method
			if(child.getClass().getName().equals("javax.swing.JMenuBar"))
			{
				addMethod = parent.getClass().getMethod("setJMenuBar", child.getClass());
			}
			else if(child.getClass().getName().equals("tags.Border"))
			{
				addMethod = parent.getClass().getMethod("setBorder", javax.swing.border.Border.class);
			}
			else if(child.getClass().getName().equals("javax.swing.JSeparator"))
			{
				addMethod = parent.getClass().getMethod("addSeparator", new Class<?>[] {});
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
				addMethod = parent.getClass().getMethod("setModel", javax.swing.table.TableModel.class);
			}
			else if(parent.getClass().getName().equals("javax.swing.ButtonGroup"))
			{
				addMethod = parent.getClass().getMethod("add",javax.swing.AbstractButton.class);
				this.buttongroup.add(child);
			}
			else
			{
				if(constraint != null)
					addMethod = parent.getClass().getMethod("add",java.awt.Component.class, Object.class);
				else
					addMethod = parent.getClass().getMethod("add",java.awt.Component.class);
			}
			
			//invoke add method
			if(child.getClass().getName().equals("javax.swing.ButtonGroup"))
			{
				while(!this.buttongroup.isEmpty())
				{
					if(constraint != null)
						addMethod.invoke(parent, buttongroup.remove(0), constraint);
					else
						addMethod.invoke(parent, buttongroup.remove(0));
				}
			}
			else if(this.extendTagLibrary.contains(child.getClass().getName()))
			{
				addMethod.invoke(parent, ((AbstractTag) child).getComponent());
			}
			else if(child.getClass().getName().equals("javax.swing.JSeparator"))
			{
				addMethod.invoke(parent, new Object[] {});
			}
			else
			{
				if(addMethod.getParameterTypes().length == 1)
					addMethod.invoke(parent, child);
				else
					addMethod.invoke(parent, child, LayoutConverter.convert(parent, constraint));
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
	
	public SwingParser()
	{
		
	}
		
	public Object parse(InputStream inputStream) {
		Object targetObject = null;
		try
		{
			this.read(inputStream);
			Element currentNode = this.doc.getDocumentElement();		
			int index = 0;
			String id = null;
			while(currentNode != null)
			{
				boolean suspend = false;
				
				//initialize attributes
				if(index == 0)
				{
					Object constraint = null;
					String tag = currentNode.getNodeName();
					if(libraries.containsTag(tag))
						targetObject = libraries.getLibrary(tag).newInstance();
					else
						targetObject = extendTagLibrary.getLibrary(tag).newInstance();
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
						else if(field.equals("Constraint"))
							constraint = value;
						else
						{
							Method setMethod = this.findSetMethod(targetObject, field);
							if(setMethod != null)
								this.invokeSetMethod(targetObject, setMethod, value, field);
						}
					}
					this.constraints.push(constraint);
				}
				
				//initialize child elements
				NodeList children = currentNode.getChildNodes();
				for(int i = index; i < children.getLength(); i++)
				{
					if(libraries.containsTag(children.item(i).getNodeName()) 
							|| extendTagLibrary.containsTag(children.item(i).getNodeName()))
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
						targetObject = this.invokeAddMethod(parent, targetObject, this.constraints.pop());
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