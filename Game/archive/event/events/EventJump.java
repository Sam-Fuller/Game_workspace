package event.events;

import entities.Player;
import world.RunPhysics;

public class EventJump implements Event {

	@Override
	public void onPress() {
		if (!RunPhysics.paused) Player.jump();
	}

	@Override
	public void onRelease() {
		if (!RunPhysics.paused) Player.jumped = false;
	}

}
