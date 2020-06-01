package world;

public class RunWorldGenerator implements Runnable{

	public static final int bufferChunks = 5;
	public static boolean interrupted = false;

	@Override
	public void run() {
		while (!interrupted) {
			try {
				Thread.sleep(500);
				
				if (World.world.size() < World.level + bufferChunks) {
					World.generateMap(World.level);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
