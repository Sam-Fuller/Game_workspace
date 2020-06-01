package event;

import event.events.KeyEvent;

public class KeyBind {
	public int key;
	public KeyEvent action;
	
	public KeyBind(int key, KeyEvent action) {
		this.key = key;
		this.action = action;
	}
}