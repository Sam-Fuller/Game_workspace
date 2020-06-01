package entities.enemies.staticMelee;

import entities.Entity;
import entities.enemies.Enemy;
import world.Chunk;

public abstract class StaticMelee extends Enemy {
	public static final float startHealth = 1000;
	public static final float healthScalar = 10;
		
	public StaticMelee(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		health = maxHealth = startHealth + level * healthScalar;
	}

	@Override
	public boolean gravityImmune() {
		return true;
	}

	@Override
	public void update(float frameSpeed) {
		return;
	}
	
	@Override
	public void action(float frameSpeed) {}
	
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
