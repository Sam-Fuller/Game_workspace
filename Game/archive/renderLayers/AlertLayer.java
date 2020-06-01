package renderLayers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.Alert;

public class AlertLayer {
	public static boolean display = true;
	public static List<Alert> alerts = new CopyOnWriteArrayList<Alert>();
	
	public static void draw(float frameSpeed) {
		if (!display) return;
				
		if (alerts.size() == 0) return;
		
		for (Alert alert : alerts) {
			alert.draw(frameSpeed);
		}
	}
	
}
