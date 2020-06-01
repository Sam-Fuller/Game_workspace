package event.events;

import state.GameState;

public class Dash implements KeyEvent {
	@Override
	public void onPress() {
		GameState.dash();
	}

	@Override
	public void onRelease() {
		
	}

}
