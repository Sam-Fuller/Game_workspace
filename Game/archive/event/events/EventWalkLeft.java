package event.events;

import entities.Player;

public class EventWalkLeft implements Event {

	@Override
	public void onPress() {
		Player.runL = true;
	}

	@Override
	public void onRelease() {
		Player.runL = false;
	}

}
