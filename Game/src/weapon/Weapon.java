package weapon;

public abstract class Weapon {
	long lastFired;
	long fireRate;
	float projectileSpeed;
	
	public Weapon(long fireRate, float projectileSpeed) {
		this.lastFired = 0;
		this.fireRate = fireRate;
		this.projectileSpeed = projectileSpeed;
	}
	
	public abstract void fire();
	
}
