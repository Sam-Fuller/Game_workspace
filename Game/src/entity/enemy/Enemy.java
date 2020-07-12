package entity.enemy;

import entity.Entity;
import state.World;

public abstract class Enemy extends Entity{

	public Enemy(int height, int width, int chunkNo, float posX, float posY) {
		super(height, width, chunkNo, posX, posY);
	}
	
	@Override
	protected void incChunkNo(int inc) {
		World.getWorld().get(getChunkNo()).getEntities().remove(this);
		super.incChunkNo(1);
		World.getWorld().get(getChunkNo()).getEntities().add(this);
	}

}
