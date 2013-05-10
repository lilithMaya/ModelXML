package tags;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class Border {
	private int top = 1;
	private int left = 1;
	private int right = 1;
	private int bottom = 1;
	private int thickness = 1;
	private String color = "000000";
	private String highlightColor = "#highlight";
	private String shadowColor = "#shadow";
	private boolean rounded = false;
	private String title;
	private String borderName = "EmptyBorder";
	private String icon;
	private String type;
	private String titleJustification = "DEFAULT_JUSTIFICATION";
	private String titlePosition = "DEFAULT_POSITION";
	private String titleFont;
	private String titleColor = "000000";
	
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isRounded() {
		return rounded;
	}
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBorderName() {
		return borderName;
	}
	public void setBorderName(String borderName) {
		this.borderName = borderName.substring(0 ,1).toUpperCase() + borderName.substring(1);
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getHighlightColor() {
		return highlightColor;
	}
	public void setHighlightColor(String highlightColor) {
		this.highlightColor = highlightColor;
	}
	public String getShadowColor() {
		return shadowColor;
	}
	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitleJustification() {
		return titleJustification;
	}
	public void setTitleJustification(String titleJustification) {
		this.titleJustification = titleJustification;
	}
	public String getTitlePosition() {
		return titlePosition;
	}
	public void setTitlePosition(String titlePosition) {
		this.titlePosition = titlePosition;
	}
	public String getTitleFont() {
		return titleFont;
	}
	public void setTitleFont(String titleFont) {
		this.titleFont = titleFont;
	}
	public String getTitleColor() {
		return titleColor;
	}
	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}
	
	public javax.swing.border.Border getBorder() {
		javax.swing.border.Border border = null;
		switch(this.borderName)
		{
			case "LineBorder":
				border = BorderFactory.createLineBorder(new Color(Integer.parseInt(this.color.toString(),16)), 
						this.thickness, this.rounded);
				break;
			case "MatteBorder":
				if(this.icon == null)
					border = BorderFactory.createMatteBorder(this.top, this.left, this.bottom, this.right, 
							new Color(Integer.parseInt(this.color.toString(),16)));
				else
					border = BorderFactory.createMatteBorder(this.top, this.left, this.bottom, this.right, 
							new ImageIcon(this.icon));
				break;
			case "EtchedBorder":
				border = BorderFactory.createEtchedBorder(this.convertType(this.borderName, this.type), 
						this.convertColor(this.borderName, this.highlightColor), 
						this.convertColor(this.borderName, this.shadowColor));
				break;
			case "BevelBorder":
				border = BorderFactory.createBevelBorder(this.convertType(this.borderName, this.type), 
						this.convertColor(this.borderName, this.highlightColor), 
						this.convertColor(this.borderName, this.shadowColor));
				break;
			default:
				border = BorderFactory.createEmptyBorder(this.top, this.left, this.bottom, this.right);
				break;
		}
		if((border != null) && (this.title != null))
		{
			if(this.titleFont == null)
				return BorderFactory.createTitledBorder(border, this.title, 
						this.convertType("TitledBorder",this.titleJustification), 
						this.convertType("TitledBorder",this.titlePosition), 
						UIManager.getDefaults().getFont("TitledBorder.font"), 
						this.convertColor("TitledBorder", this.titleColor));
			else
				return BorderFactory.createTitledBorder(border, this.title, 
						this.convertType("TitledBorder",this.titleJustification), 
						this.convertType("TitledBorder",this.titlePosition), Font.decode(this.titleFont), 
						this.convertColor("TitledBorder", this.titleColor));
		}
		return border;
	}
	
	private int convertType(String tagborderName, String field)
	{
		String classborderName = "javax.swing.border." + tagborderName;
		try {
			Field[] fields = Class.forName(classborderName).getFields();
			for(int i = 0; i < fields.length; i++)
			{
				if(fields[i].getName().equals(field.toUpperCase()))
					return (int) fields[i].get(Class.forName(classborderName));
			}
		} catch (SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private Color convertColor(String tagborderName, String field)
	{
		if(field.startsWith("#"))
			return UIManager.getColor(tagborderName + "." + field.substring(1));
		else
			return new Color(Integer.parseInt(field,16));
	}

}
