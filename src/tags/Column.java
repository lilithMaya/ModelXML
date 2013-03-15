package tags;

import javax.swing.table.TableColumn;

public class Column {
	
	private TableColumn column = new TableColumn();
	private String name;
	private int columnWidth;
	
	public TableColumn getColumn() {
		return column;
	}
	
	public void setName(String name) {
		this.column.setHeaderValue(name);
	}
	
	public void setColumnWidth(int width) {
		this.column.setPreferredWidth(width);
	}

	public String getName() {
		return name;
	}

	public int getColumnWidth() {
		return columnWidth;
	}
}
