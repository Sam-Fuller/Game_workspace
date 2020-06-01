package world;

import entities.Entity;
import entities.Projectile;

public class RunPhysics implements Runnable{
	public static boolean interrupted = false;
	public static boolean paused = false;

	@Override
	public void run() {
		long now = 0;
		long then = 0;
		float frameSpeed;
		
		while(!interrupted) {
			try {
				Thread.sleep(1);
				
				if (!paused) {
					now = System.nanoTime();
					if (then == 0) then = now;
					frameSpeed = 60 * (now - then) / 1000000000.0f;
					then = now;
					
					updateAllEnemy(frameSpeed);
					
					//System.out.println("phys:" + World.projectiles.size());
					for (Projectile projectile: World.projectiles) {
						projectile.update(frameSpeed);
						projectile.action(frameSpeed);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void updateAllEnemy(float frameSpeed) {
		if (World.level > 0) {
			if (World.world.get(World.level).getEntrance() == 0) 		updateChunkEnemy(World.world.get(World.level-1), frameSpeed);
			else if (World.world.get(World.level).getEntrance() == 1) 	updateChunkEnemy(World.world.get(World.level-1), frameSpeed);
			else if (World.world.get(World.level).getEntrance() == 3) 	updateChunkEnemy(World.world.get(World.level-1), frameSpeed);
		}
		
		//draw current chunk
		updateChunkEnemy(World.world.get(World.level), frameSpeed);

		//draw next chunk
		if (World.world.get(World.level).getExit() == 1) 		updateChunkEnemy(World.world.get(World.level+1), frameSpeed);
		else if (World.world.get(World.level).getExit() == 2) 	updateChunkEnemy(World.world.get(World.level+1), frameSpeed);
		else if (World.world.get(World.level).getExit() == 3) 	updateChunkEnemy(World.world.get(World.level+1), frameSpeed);
	
	}
	
	public static void updateChunkEnemy(Chunk chunk, float frameSpeed) {	
		for (Entity enemy: chunk.getEntities()) {
			enemy.update(frameSpeed);
			enemy.action(frameSpeed);
		}
	}

}
