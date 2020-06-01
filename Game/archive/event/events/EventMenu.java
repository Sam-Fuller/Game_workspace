package event.events;

import renderLayers.AlertLayer;
import renderLayers.EscapeMenuLayer;
import world.RunPhysics;

public class EventMenu implements Event{

	@Override
	public void onPress() {
		AlertLayer.display = EscapeMenuLayer.display;
		EscapeMenuLayer.display = !EscapeMenuLayer.display;
		RunPhysics.paused = EscapeMenuLayer.display;
	}

	@Override
	public void onRelease() {}

}
  