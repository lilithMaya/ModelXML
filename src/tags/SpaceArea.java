package tags;

import java.awt.Dimension;

import javax.swing.Box;

public class SpaceArea implements AbstractTag {
	
	Dimension size;
	
	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	@Override
	public Object[] getComponent()
	{
		Object obj = Box.createRigidArea(size);
		return new Object[] {obj};
	}

}
