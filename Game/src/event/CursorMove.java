package event;

import runnable.Graphics;
import state.GameState;

public class CursorMove {
	public static void onMove(double xpos, double ypos) {
		GameState.cursorMove(Graphics.resToGridx((float)xpos), Graphics.resToGridy((float)ypos));
	}
}
