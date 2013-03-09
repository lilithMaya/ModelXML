package util;

public class SwingTagLibrary extends TagLibrary{
	
	public SwingTagLibrary()
	{
		this.insertTag("button", "javax.swing.JButton");
		this.insertTag("buttongroup", "javax.swing.ButtonGroup");
		this.insertTag("checkbox", "javax.swing.JCheckBox");
		this.insertTag("combobox", "javax.swing.JComboBox");
		this.insertTag("dialog", "javax.swing.JDialog");
		this.insertTag("frame", "javax.swing.JFrame");
		this.insertTag("label", "javax.swing.JLabel");
		this.insertTag("menu", "javax.swing.JMenu");
		this.insertTag("menubar", "javax.swing.JMenuBar");
		this.insertTag("menuitem", "javax.swing.JMenuItem");
		this.insertTag("panel", "javax.swing.JPanel");
		this.insertTag("progressbar", "javax.swing.JProgressBar");
		this.insertTag("radiobutton", "javax.swing.JRadioButton");
		this.insertTag("scrollpane", "javax.swing.JScrollPane");
		this.insertTag("slider", "javax.swing.JSlider");
		this.insertTag("spinner", "javax.swing.JSpinner");
		this.insertTag("table", "javax.swing.JTable");
		this.insertTag("textarea", "javax.swing.JTextArea");
		this.insertTag("textfield", "javax.swing.JTextField");
		this.insertTag("toolbar", "javax.swing.JToolBar");
		this.insertTag("tree", "javax.swing.JTree");
	}

}
