package event.events;

import entity.Player;

public class Jump implements KeyEvent {

	@Override
	public void onPress() {
		Player.getPlayer().setVelocityY(-0.75f);
		Player.getPlayer().setVelocityX(0);
	}

	@Override
	public void onRelease() {
		//Player.jumped = false;
	}

}