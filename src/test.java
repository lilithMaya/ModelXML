import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.table.DefaultTableModel;

public class test {
		
	public static void main(String[] args)
	{
		Engine e = new Engine();
		e.setLayout(test.class.getResourceAsStream("sample.xml"));
		e.render();
//		JTable table = (JTable) e.findViewById("tb");
//		DefaultTableModel tbmodel = (DefaultTableModel) table.getModel();
//		tbmodel.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
		JComboBox<String> cb = (JComboBox<String>) e.findViewById("type");
		String [] items = {"Language", "Project"};
		for(String s:items)
		{
			cb.addItem(s);
		}
	}
}
