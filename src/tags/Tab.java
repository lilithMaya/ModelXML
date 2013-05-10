package tags;

import java.awt.Component;
import javax.swing.ImageIcon;

public class Tab implements AbstractTag {
	private String title;
	private ImageIcon icon;
	private Component component;
	private String toolTipText;
	
	@Override
	public Object[] getComponent() {
		return new Object[] {title, icon, component, toolTipText};
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public String getToolTipText() {
		return toolTipText;
	}
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	
	public void add(Component component) {
		this.component = component;
	}
	
	public void add(Component component, Object constraint) {
		this.component = component;
	}

}
