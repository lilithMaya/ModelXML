package tags;

import java.awt.Dimension;

import javax.swing.Box;

public class SpaceArea {
	
	Dimension size;
	
	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public Object getSpaceArea()
	{
		Object obj = Box.createRigidArea(size);
		return obj;
	}

}
