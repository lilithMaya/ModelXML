package editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import engine.Engine;

public class MockApp extends Engine {
	
	public  MockApp(File file) {
		try {
			this.setLayout(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MockApp.this.render(JFrame.DISPOSE_ON_CLOSE);
            }
		});
	}

}
