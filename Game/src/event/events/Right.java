package event.events;

import entity.Player;

public class Right implements KeyEvent {

	@Override
	public void onPress() {
		Player.getPlayer().setRunRight(true);
	}

	@Override
	public void onRelease() {
		Player.getPlayer().setRunRight(false);
	}
}