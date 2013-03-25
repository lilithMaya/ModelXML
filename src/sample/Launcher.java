package sample;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import engine.Engine;

public class Launcher extends Engine {
	
	private JFrame frame = null;
	
	@SuppressWarnings("unchecked")
	public Launcher() {	
		this.setLayout(Launcher.class.getResourceAsStream("resource/sample.xml"));
		this.frame = (JFrame) this.findViewById("main");
		JComboBox<String> cb = (JComboBox<String>) this.findViewById("type");
		String [] items = {"Language", "Project"};
		for(String s:items)
		{
			cb.addItem(s);
		}
		JMenuItem addCode = (JMenuItem) this.findViewById("addCode");
		addCode.addActionListener(new AddFile());
		JMenuItem addFolder = (JMenuItem) this.findViewById("addFolder");
		addFolder.addActionListener(new AddFolder());
		JMenuItem about = (JMenuItem) this.findViewById("about");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				About about = new About();
				about.show();
			}
			
		});
		
	}
	
	class AddFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(Launcher.this.frame);
		}
		
	}
	
	class AddFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(Launcher.this.frame);
		}
		
	}
		
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Launcher l = new Launcher();
            	l.render(JFrame.DISPOSE_ON_CLOSE);
            }
		});
		
//		JTable table = (JTable) e.findViewById("tb");
//		DefaultTableModel tbmodel = (DefaultTableModel) table.getModel();
//		tbmodel.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
		
	}
}
