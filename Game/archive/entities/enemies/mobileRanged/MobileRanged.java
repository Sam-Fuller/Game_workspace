package entities.enemies.mobileRanged;

import entities.Entity;
import entities.enemies.Enemy;

public abstract class MobileRanged extends Enemy {
	public static final float startHealth = 100;
	public static final float healthScalar = 1;
	
	public MobileRanged(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		setHazard(false);
		
		health = maxHealth = startHealth + level * healthScalar;
	}

	@Override
	public void onContact(float x, float y) {}
	
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
