package runnable;

import event.events.Fire;
import state.GameState;

public class WeaponFire implements Runnable {
	public static boolean interrupted = false;
	
	@Override
	public void run() {
//		long now = 0;
//		long then = 0;
//		float frameSpeed;
		
		while (!interrupted) {	
//			now = System.nanoTime();
//			if (then == 0) then = now;
//			frameSpeed = 60 * (now - then) / 1000000000.0f;
//
//			if (frameSpeed == 0) continue;
//			then = now;
			
			if (Fire.isFiring()) GameState.fire();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}