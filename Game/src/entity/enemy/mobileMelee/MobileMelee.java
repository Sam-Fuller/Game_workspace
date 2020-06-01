package entity.enemy.mobileMelee;

import entity.enemy.Enemy;

public abstract class MobileMelee extends Enemy {
	public static final int startHealth = 100;
	public static final int healthScalar = 1;
	
	public MobileMelee(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);

		int health = startHealth + level * healthScalar;
		setMaxHealth(health);
		setHealth(health);
		
		setDecelerate(true);
	}
}
