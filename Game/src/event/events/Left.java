package event.events;

import entity.Player;

public class Left implements KeyEvent {

	@Override
	public void onPress() {
		Player.getPlayer().setRunLeft(true);
	}

	@Override
	public void onRelease() {
		Player.getPlayer().setRunLeft(false);
	}
}