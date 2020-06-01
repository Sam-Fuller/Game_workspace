package runnable;

import chunk.Chunk;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import state.GameState;
import state.World;

public class Physics implements Runnable {
	public static boolean interrupted = false;

	@Override
	public void run() {
		long now = 0;
		long then = 0;
		float frameSpeed;

		while (!interrupted) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			now = System.nanoTime();
			if (then == 0) then = now;
			frameSpeed = 60 * (now - then) / 1000000000.0f;

			if (frameSpeed == 0) continue;
			then = now;

			if (GameState.getState() != GameState.State.MENU) continue;

			if (Player.getPlayer().getChunkNo() > 0)
				for (Entity entity: World.getWorld().get(Player.getPlayer().getChunkNo()-1).getEntities())
					entity.action(frameSpeed);

			for (Entity entity: World.getWorld().get(Player.getPlayer().getChunkNo()).getEntities())
				entity.action(frameSpeed);

			for (Entity entity: World.getWorld().get(Player.getPlayer().getChunkNo()+1).getEntities())
				entity.action(frameSpeed);
		}
	}

}