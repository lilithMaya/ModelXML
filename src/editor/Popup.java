package editor;

import engine.Engine;

public class Popup extends Engine {
	
	public Popup() {
		this.setLayout(Popup.class.getResourceAsStream("resources/popup.xml"));
	}
}
