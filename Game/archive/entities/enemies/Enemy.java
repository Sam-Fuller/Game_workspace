package entities.enemies;

import entities.Entity;
import world.World;

public abstract class Enemy extends Entity{

	public Enemy(int height, int width, int chunkNo, float posX, float posY) {
		super(height, width, chunkNo, posX, posY);
	}

	@Override
	public void onDestruction() {
		World.world.get(getChunkNo()).getEntities().remove(this);
	}
}
