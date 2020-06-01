package event.events;

import entities.Player;

public class EventWalkRight implements Event {

	@Override
	public void onPress() {
		Player.runR = true;
	}

	@Override
	public void onRelease() {
		Player.runR = false;
	}

}
