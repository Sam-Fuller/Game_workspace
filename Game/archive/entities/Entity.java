package entities;

import blocks.Colour;
import main.GameWindow;
import world.Chunk;
import world.World;

public abstract class Entity implements Collidable{
	float height;
	float width;

	float posX;
	float posY;

	float velX;
	float velY;

	int chunkNo;
	
	public float maxHealth;
	public float health;

	boolean hazard = true;

	public Entity(float height, float width, int chunkNo, float posX, float posY) {
		this.height = height;
		this.width = width;
		this.chunkNo = chunkNo;
		this.posX = posX;
		this.posY = posY;
	}

	public float getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}
	public void incMaxHealth(float maxHealth) {
		this.maxHealth += maxHealth;
	}

	public float getHealth() {
		return health;
	}
	public void setHealth(float health) {
		this.health = health;
		if (this.health <= 0) onDestruction();
	}
	public void incHealth(float health) {
		this.health += health;
		if (this.health <= 0) onDestruction();
	}
	public void decHealth(float health) {
		this.health -= health;
		if (this.health <= 0) onDestruction();
	}


	@Override
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}



	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public void incPosX(float velX) {
		this.posX += velX;
	}

	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public void incPosY(float velY) {
		this.posY += velY;
	}

	@Override
	public float getPureVelocityX() {
		return velX;
	}
	@Override
	public float getVelocityX() {
		return velX;
	}
	@Override
	public void setVelocityX(float velX) {
		this.velX = velX;
	}
	public void incVelocityX(float velX) {
		this.velX += velX;
	}

	@Override
	public float getPureVelocityY() {
		return velY;
	}
	@Override
	public float getVelocityY() {
		return velY;
	}
	@Override
	public void setVelocityY(float velY) {
		this.velY = velY;
	}
	public void incVelocityY(float velY) {
		this.velY += velY;
	}
	
	public void setVelocities(float X, float Y){
		setVelocityX(X);
		setVelocityY(Y);
	}
	
	@Override
	public int getChunkNo() {
		return chunkNo;
	}
	@Override
	public void incChunkNo(int inc) {
		World.world.get(chunkNo).getEntities().remove(this);
		chunkNo += inc;
		World.world.get(chunkNo).getEntities().add(this);
	}

	public boolean isHazard() {
		return hazard;
	}
	public void setHazard(boolean hazard) {
		this.hazard = hazard;
	}

	public void checkHitbox(float offsetX, float offsetY) {
		if (!hazard) return;

		float x = offsetX - getPosX() + GameWindow.gameX/2;
		float y = offsetY - getPosY() + GameWindow.gameY/2;
		
		if (Math.abs(x) < (Player.playerWidth+getWidth())/2 && Math.abs(y) < (Player.playerHeight+getHeight())/2)
			onContact(x, y);
	}
	
	public abstract void onDestruction();

	public abstract void action(float frameSpeed);
	
	public abstract void onContact(float x, float y);

	public abstract void draw(float offsetX, float offsetY);

	public abstract void update(float frameSpeed);
	
	public abstract float getLightX();
	public abstract float getLightY();
	public abstract int getLightRadius();
	public abstract Colour glowColour();
}
