package sample.view;

import javax.swing.JDialog;

import engine.Engine;

public class Search extends Engine {
	public void show() {
		this.setLayout(About.class.getResourceAsStream("resource/search.xml"));
		this.render(JDialog.DISPOSE_ON_CLOSE);
	}

}
