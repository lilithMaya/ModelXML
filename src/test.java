import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class test {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		Engine e = new Engine();
		e.setLayout("foo.xml");
//		Parser p = new Parser();
//		p.parse("foo.xml");
		e.render();
//		JButton jb = (JButton) e.findViewById("red");
//		jb.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent event) {
//		    	JButton button = (JButton) event.getSource();
//		        button.setBackground(new Color(Integer.parseInt("FF0000",16)));
//		  }
//		});
		
		JComboBox<String> cb = (JComboBox<String>) e.findViewById("cb");
		String [] items = {"pig", "bird", "fox", "wolf"};
		for(String s:items)
		{
			cb.addItem(s);
		}
		String [] seasons = {"spring", "summer", "autumn", "winter"};
		SpinnerListModel model = new SpinnerListModel(seasons);
		JSpinner spinner = (JSpinner) e.findViewById("sp");
		spinner.setModel(model);
		
		JList<String> list = (JList<String>) e.findViewById("l");
		list.setListData(new String[] {"hot", "cold"});
	}
}
