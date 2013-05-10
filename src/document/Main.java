package document;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import engine.Engine;

public class Main extends Engine {
	
	public Main() {
		this.setLayout(Main.class.getResourceAsStream("document.xml"));
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Main m = new Main();
            	m.render(JFrame.DISPOSE_ON_CLOSE);
            }
		});
	}
}
