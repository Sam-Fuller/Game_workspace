package entities.enemies.staticRanged;

import entities.Entity;
import entities.enemies.Enemy;
import world.Chunk;
import world.World;

public abstract class StaticRanged extends Enemy {
	public static final float startHealth = 100;
	public static final float healthScalar = 1;

	public StaticRanged(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		setHazard(false);
		
		health = maxHealth = startHealth + level * healthScalar;
	}

	@Override
	public boolean gravityImmune() {
		return false;
	}
	
	@Override
	public void onContact(float x, float y) {}

	@Override
	public void update(float frameSpeed) {
		moveChunk();
		incPosY(getMovementY(frameSpeed));
	}
	
	@Override
	public boolean bounce() {
		return false;
	}
	@Override
	public boolean destroy() {
		return false;
	}

	@Override
	public boolean getDecelerate() {
		return true;
	}
}
