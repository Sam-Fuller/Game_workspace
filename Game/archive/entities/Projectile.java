package entities;

import blocks.Colour;
import main.GameWindow;
import world.World;

public class Projectile extends Entity{
	boolean madeByPlayer;
	
	float damage;
	boolean destroyOnContact;

	boolean gravityImmune = true;

	boolean textured = false;
	int textureID = 0;

	boolean biColoured = false;
	Colour colourIn, colourOut;

	int knockback = 0;

	long lastDamage = 0;
	long damageCooldown = 100000000;
	
	float lightX = 0;
	float lightY = 0;
	int lightRadius = 0;
	
	Colour glowColour;
	
	boolean bounce = false;
	int bounceLimit = 0;
	
	boolean destroy = true;
	boolean destroyOnDamage = true;
	boolean toBeDestroyed = false;
	
	boolean timeLimited = false;
	long timeLimit = 0;
	long startTime = 0;


	public Projectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, Colour colourIn, Colour colourOut) {
		super(height, width, chunkNo, posX-1, posY-1);

		this.colourIn = colourIn;
		this.colourOut = colourOut;
		this.biColoured = true;
		this.madeByPlayer = madeByPlayer;
	}
	public Projectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, Colour colour) {
		super(height, width, chunkNo, posX-1, posY-1);

		this.colourIn = colour;
		this.madeByPlayer = madeByPlayer;
	}
	public Projectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, int textureID) {
		super(height, width, chunkNo, posX-1, posY-1);

		this.textureID = textureID;
		this.textured = true;
		this.madeByPlayer = madeByPlayer;
	}

	public boolean isDestroyOnContact() {
		return destroyOnContact;
	}
	public void setDestroyOnContact(boolean destroyOnContact) {
		this.destroyOnContact = destroyOnContact;
	}

	public int getKnockback() {
		return knockback;
	}
	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}
	
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public long getDamageCooldown() {
		return damageCooldown;
	}
	public void setDamageCooldown(long damageCooldown) {
		this.damageCooldown = damageCooldown;
	}

	@Override
	public boolean gravityImmune() {
		return gravityImmune;
	}
	public void setGravityImmune(boolean gravityImmune) {
		this.gravityImmune = gravityImmune;
	}

	public boolean isDestroyOnDamage() {
		return destroyOnDamage;
	}
	public void setDestroyOnDamage(boolean destroyOnDamage) {
		this.destroyOnDamage = destroyOnDamage;
	}
	
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
		this.startTime = System.nanoTime();
		this.timeLimited = true;
	}
	
	@Override
	public void action(float frameSpeed) {
		if (toBeDestroyed) onDestruction();
		
		if (timeLimited) if (startTime + timeLimit < System.nanoTime()) toBeDestroyed = true;
		
		checkEnemyHitbox();
	}
	
	public void checkEnemyHitbox() {
		float x = this.getPosX() - this.getWidth()/2f;
		float y = this.getPosY() - this.getHeight()/2f;
		
		if (lastDamage + damageCooldown > System.nanoTime()) return;
		
		for (Entity enemy: World.world.get(getChunkNo()).getEntities()) {
			float distX = x - (enemy.getPosX() - enemy.getWidth()/2f);
			float distY = y - (enemy.getPosY() - enemy.getHeight()/2f);
			
			if (Math.abs(distX) < (this.getWidth()+enemy.getWidth())/2f && Math.abs(distY) < (this.getHeight()+enemy.getHeight())/2f) {
				enemy.decHealth(damage);
				if (isDestroyOnDamage()) toBeDestroyed = true;
				else lastDamage = System.nanoTime();
			}
		}
	}

	@Override
	public void onContact(float x, float y) {
		if (isDestroyOnContact()) {
			Player.decHealth(damage);

			if (knockback != 0) {
				World.player.setVelocityX(x > 0? knockback/(x+1) : knockback/(x-1));
				World.player.setVelocityY(y > 0? knockback/(y+1) : knockback/(y-1));
			}
			toBeDestroyed = true;
			
		}else {
			long now = System.nanoTime();
			if (now - lastDamage > damageCooldown) {
				Player.decHealth(damage);
				lastDamage = now;

				if (knockback != 0) {
					World.player.setVelocityX(x > 0? knockback/(x+1) : knockback/(x-1));
					World.player.setVelocityY(y > 0? knockback/(y+1) : knockback/(y-1));
				}
			}
		}
	}

	@Override
	public void draw(float offsetX, float offsetY) {
		float x = getPosX()+World.getOffsetX(getChunkNo())-World.player.getPosX()+GameWindow.gameX/2+1f;
		float y = getPosY()+World.getOffsetY(getChunkNo())-World.player.getPosY()+GameWindow.gameY/2+1f;
		
		Colour light = World.getLight(getChunkNo(), getPosX(), getPosY());
				
		if (textured) {
			GameWindow.drawPoly(x, y, x+getWidth(), y+getHeight(), textureID, true, light);
		}
		else if (biColoured) GameWindow.drawCircle(x, y, getWidth()/2, 20, colourIn, colourOut);
		else GameWindow.drawCircle(x, y, getWidth()/2, 20, colourIn);
	}

	@Override
	public void update(float frameSpeed) {
		moveChunk();
		
		incPosX(getMovementX(frameSpeed));
		incPosY(getMovementY(frameSpeed));
	}

	public void setLightX(float lightX) {
		this.lightX = lightX;
	}
	@Override
	public float getLightX() {
		return lightX;
	}

	
	public void setLightY(float lightY) {
		this.lightY = lightY;
	}
	@Override
	public float getLightY() {
		return lightY;
	}

	
	public void setLightRadius(int lightRadius) {
		this.lightRadius = lightRadius;
	}
	@Override
	public int getLightRadius() {
		return lightRadius;
	}
	
	public void setGlowColour(Colour glowColour) {
		this.glowColour = glowColour;
	}
	@Override
	public Colour glowColour() {
		return glowColour;
	}

	public void setLight(int lightRadius, float lightX, float lightY, Colour colour) {
		setLightRadius(lightRadius);
		setLightX(lightX);
		setLightY(lightY);
		setGlowColour(colour);
	}
	
	public void setBounceLimit(int bounceLimit) {
		this.bounce = true;
		this.bounceLimit = bounceLimit;
	}
	@Override
	public boolean bounce() {
		if (!bounce) return false;
		if (bounceLimit-- > 0) return true;
		return false;
	}

	@Override
	public boolean destroy() {
		if (bounce && bounceLimit > 0) return false;
		
		return toBeDestroyed = true;
	}
	@Override
	public boolean getDecelerate() {
		return false;
	}
	@Override
	public void onDestruction() {
		World.projectiles.remove(this);
	}
}
