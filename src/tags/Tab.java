package tags;

import java.awt.Component;
import javax.swing.ImageIcon;

public class Tab {
	private String title;
	private ImageIcon icon;
	private Component component;
	private String toolTipText;
	
	public Component getComponent() {
		return component;
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

}
