package entities.enemies.mobileMelee;

import entities.Entity;
import entities.enemies.Enemy;

public abstract class MobileMelee extends Enemy {
	public static final float startHealth = 100;
	public static final float healthScalar = 1;
	
	public MobileMelee(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);

		health = maxHealth = startHealth + level * healthScalar;
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
