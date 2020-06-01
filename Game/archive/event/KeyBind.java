package event;

import event.events.Event;

public class KeyBind {
	public int key;
	public Event action;
	
	public KeyBind(int key, Event action) {
		this.key = key;
		this.action = action;
	}
}
