package sample.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import sample.controller.CodeFileController;
import sample.model.CodeFile;

import engine.Engine;

public class Launcher extends Engine {
	
	private JFrame frame = null;
	private CodeFileController controller = new CodeFileController();
	
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
		JMenuItem search = (JMenuItem) this.findViewById("search");
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Search searchDialog = new Search();
				searchDialog.show();
			}			
		});
		JMenuItem about = (JMenuItem) this.findViewById("about");
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				About aboutDialog = new About();
				aboutDialog.show();
			}			
		});
		JMenuItem exit = (JMenuItem) this.findViewById("exit");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.this.killLayout();
			}
			
		});
		
	}
	
	class AddFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(Launcher.this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				Launcher.this.updateUI(file);
			}
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
	
	private void updateUI(File file) {
		CodeFile code = controller.readCodeFile(file);
		JTable table = (JTable) findViewById("tb");
		DefaultTableModel tbmodel = (DefaultTableModel) table.getModel();
		tbmodel.addRow(new Object[]{code.getName(), code.getPath(), code.getSize(), code.getDateModified()});
		
		@SuppressWarnings("unchecked")
		JList<String> list = (JList<String>) findViewById("l");
		ListModel<String> model = (ListModel<String>) list.getModel();
		DefaultListModel<String> newModel = new DefaultListModel<String>();
		boolean hasType = false;
		for(int i = 0; i < model.getSize(); i++) {
			String element = model.getElementAt(i);
			if(element.equals(code.getType())) {
				hasType = true;
			}
			newModel.addElement(element);
		}
		if(!hasType) {
			newModel.addElement(code.getType());
		}
		list.setModel(newModel);
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
