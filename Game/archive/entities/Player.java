package entities;

import blocks.Colour;
import main.GameWindow;
import weapons.Pistol;
import weapons.Shotgun;
import weapons.Sword;
import weapons.Weapon;
import world.World;

public class Player implements Collidable {
	public static final int playerWidth = 3;
	public static final int playerHeight = 5;

	static final float xWalkingSpeed = 0.75f;

	static final float yJumpAcceleration = -0.75f;

	static float maxHealth = 100;
	static float health = 100;

	static float nextUpgrade = 100;
	static float upgradePoints = 50;

	public static boolean runL = false;
	public static boolean runR = false;

	public static boolean jumped = false;
	public static int jumpNumber = 0;

	static float velocityX = 0;
	static float velocityY = 0;

	static float lightRadius = 25;
	
	static Weapon weapon = new Sword();


	public static float getLightRadius() {
		return lightRadius;
	}
	public static void setLightRadius(float lightRadius) {
		Player.lightRadius = lightRadius;
	}

	@Override
	public boolean gravityImmune() {
		return false;
	}
	@Override
	public float getPureVelocityX() {
		return velocityX;
	}
	@Override
	public float getPureVelocityY() {
		return velocityY;
	}
	@Override
	public float getVelocityX() {
		float vel = velocityX;
		if (runL) vel -= xWalkingSpeed;
		if (runR) vel += xWalkingSpeed;
		return vel;
	}
	@Override
	public float getVelocityY() {
		return velocityY;
	}
	@Override
	public void setVelocityX(float X) {
		velocityX = X;
	}
	@Override
	public void setVelocityY(float Y) {
		velocityY = Y;
	}
	@Override
	public void incVelocityX(float X) {
		velocityX += X;
	}
	@Override
	public void incVelocityY(float Y) {
		velocityY += Y;
	}
	
	@Override
	public void setVelocities(float X, float Y) {
		setVelocityX(X);
		setVelocityY(Y);
	}

	@Override
	public float getWidth() {
		return playerWidth;
	}
	@Override
	public float getHeight() {
		return playerHeight;
	}

	@Override
	public float getPosX() {
		return World.posX;
	}
	@Override
	public float getPosY() {
		return World.posY;
	}
	@Override
	public void setPosX(float posX) {
		World.posX = posX;
	}
	@Override
	public void setPosY(float posY) {
		World.posY = posY;
	}
	@Override
	public void incPosX(float posX) {
		World.posX += posX;
	}
	@Override
	public void incPosY(float posY) {
		World.posY += posY;
	}

	public static float getMaxHealth() {
		return maxHealth;
	}
	public static void setMaxHealth(float maxHealth) {
		Player.maxHealth = maxHealth;
	}
	public static void incMaxHealth(float maxHealth) {
		Player.maxHealth += maxHealth;
	}

	public static float getHealth() {
		return health;
	}
	public static void setHealth(float health) {
		Player.health = health;
	}
	public static void incHealth(float health) {
		Player.health += health;
		
		if (Player.health > Player.maxHealth) Player.health = Player.maxHealth;
		else if (Player.health < 0) Player.health = 0;
	}
	public static void decHealth(float health) {
		Player.health -= health;
		
		if (Player.health > Player.maxHealth) Player.health = Player.maxHealth;
		else if (Player.health < 0) Player.health = 0;
	}


	public static float getNextUpgrade() {
		return nextUpgrade;
	}
	public static void setNextUpgrade(float nextUpgrade) {
		Player.nextUpgrade = nextUpgrade;
	}
	public static void incNextUpgrade(float nextUpgrade) {
		Player.nextUpgrade += nextUpgrade;
	}

	public static float getUpgradePoints() {
		return upgradePoints;
	}
	public static void incUpgradePoints(float upgradePoints) {
		Player.upgradePoints += upgradePoints;
	}
	

	public static Weapon getWeapon() {
		return weapon;
	}
	public static void setWeapon(Weapon weapon) {
		Player.weapon = weapon;
	}
	
	
	public static void jump() {
		if (jumped) return;
		jumped = true;
		velocityY = yJumpAcceleration;
	}

	public void calculatePosition(float frameSpeed) {
		//movement
		moveChunk();
		World.posX += getMovementX(frameSpeed);
		World.posY += getMovementY(frameSpeed);
	}

	public static void draw() {
		int maxX = (GameWindow.gameX+Player.playerWidth)/2;
		int maxY = (GameWindow.gameY+Player.playerHeight)/2;

		for (int x = (GameWindow.gameX-Player.playerWidth)/2; x < maxX; x++) {
			for (int y = (GameWindow.gameY-Player.playerHeight)/2; y < maxY; y++) {
				GameWindow.drawSquare(x, y, Colour.BLUE);
			}
		}
	}

	public static float distance(int chunk, float x, float y) {			
		float distX = World.posX - x - World.getOffsetX(chunk);
		float distY = World.posY - y - World.getOffsetY(chunk);

		return (float) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2)) - Player.playerWidth/2;
	}
	public static boolean within(int chunk, float x, float y, float distance) {
		float distX = World.posX - x - World.getOffsetX(chunk);
		float distY = World.posY - y - World.getOffsetY(chunk);

		if (distX > distance) return false;
		if (distY > distance) return false;
		if (distance(chunk, x, y) > distance) return false;

		return true;
	}

	@Override
	public int getChunkNo() {
		return World.level;
	}
	@Override
	public void incChunkNo(int inc) {
		World.level += inc;
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
