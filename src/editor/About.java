package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import engine.Engine;

public class About extends Engine {
	public void show() {
		this.setLayout(About.class.getResourceAsStream("resources/about.xml"));
		this.render(JDialog.DISPOSE_ON_CLOSE);
		JButton button = (JButton) this.findViewById("ok");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				About.this.killLayout();			
			}			
		});
	}

}
