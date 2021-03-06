package tags;

import javax.swing.table.DefaultTableModel;

public class Column implements AbstractTag {
	
	private static DefaultTableModel model = new DefaultTableModel() {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column)
	    {
	      return false;
	    }
	};
	private static int index = -1;
	private String name;
	
	public Column() {
		index++;
	}
	
	@Override
	public Object[] getComponent() {
		return new Object[] {model};
	}
		
	public void setName(String name) {
		if(model.getColumnCount() != index + 1)
			model.addColumn(name);
	}
	
	public String getName() {
		return name;
	}

}
