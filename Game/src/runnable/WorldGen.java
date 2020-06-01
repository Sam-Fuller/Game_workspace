package runnable;

import state.World;

public class WorldGen implements Runnable{
	public static boolean interrupted = false;

	@Override
	public void run() {
		while (!interrupted) {
			World.generateMap();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
