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
	}
}
