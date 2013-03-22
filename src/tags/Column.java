package tags;

import javax.swing.table.DefaultTableModel;

public class Column {
	
	private static DefaultTableModel model = new DefaultTableModel();
	private static int index = -1;
	private String name;
	
	public Column() {
		index++;
	}
	
	public DefaultTableModel getModel() {
		return model;
	}
		
	public void setName(String name) {
		if(model.getColumnCount() != index + 1)
			model.addColumn(name);
	}
	
	public String getName() {
		return name;
	}

}