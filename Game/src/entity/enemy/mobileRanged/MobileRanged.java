package entity.enemy.mobileRanged;

import entity.enemy.Enemy;

public abstract class MobileRanged extends Enemy {
	public static final int startHealth = 100;
	public static final int healthScalar = 1;
	
	public MobileRanged(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		setHazard(false);
		
		int health = startHealth + level * healthScalar;
		setMaxHealth(health);
		setHealth(health);
		
		setDecelerate(true);
	}
}
