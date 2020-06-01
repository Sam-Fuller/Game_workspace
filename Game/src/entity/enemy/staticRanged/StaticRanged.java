package entity.enemy.staticRanged;

import entity.enemy.Enemy;

public abstract class StaticRanged extends Enemy {
	public static final int startHealth = 100;
	public static final int healthScalar = 1;

	public StaticRanged(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		setHazard(false);
		
		int health = startHealth + level * healthScalar;
		setMaxHealth(health);
		setHealth(health);
		
		setDecelerate(true);
	}

	@Override
	public void update(float frameSpeed) {
		moveChunk();
		insideWall();
		
		incPosY(getMovementY(frameSpeed));
	}
}
