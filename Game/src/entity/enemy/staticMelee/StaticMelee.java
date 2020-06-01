package entity.enemy.staticMelee;

import entity.enemy.Enemy;

public abstract class StaticMelee extends Enemy {
	public StaticMelee(int height, int width, float posX, float posY, int level) {
		super(height, width, level, posX, posY);
		
		setDecelerate(true);
		setGravityImmune(true);
	}

	@Override
	public void update(float frameSpeed) {
		return;
	}
}
