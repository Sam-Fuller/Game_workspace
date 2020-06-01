package event;

import renderLayers.EscapeMenuLayer;

public class CursorMoveEvent {
	public static void onMove(double xpos, double ypos) {
		if (EscapeMenuLayer.display) {
			EscapeMenuLayer.onMove(xpos, ypos);
			return;
		}
	}
}
