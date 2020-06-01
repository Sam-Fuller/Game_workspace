package event.events;

import state.GameState;

public class Fire implements KeyEvent {
	static boolean firing = false;
	
	@Override
	public void onPress() {
		firing = true;
		
		GameState.click();
	}

	@Override
	public void onRelease() {
		firing = false;
	}

	public static boolean isFiring() {
		return firing;
	}
}