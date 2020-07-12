package entity;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;
import state.World;
import weapon.Pistol;
import weapon.Shotgun;
import weapon.Weapon;

public class Player extends Entity{
	static final int initialHealth = 40;
	
	static final float defaultHeight = 5;
	static final float defaultWidth = 3;
	
	static final float runSpeed = 0.5f;
	
	static Player player;
	
	public static void init() {
		player = new Player();
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	
	
	float nextUpgrade = 100;
	float upgradePoints = 50;
	
	Mesh mesh = null;
	
	boolean runLeft = false;
	boolean runRight = false;
	
	Weapon weapon = new Shotgun();
	
	public Player() {
		super(defaultHeight, defaultWidth, 0, 150, 150);
		
		setMaxHealth(initialHealth);
		setHealth(initialHealth);
	}
	
	public boolean isRunLeft() {
		return runLeft;
	}
	public void setRunLeft(boolean runLeft) {
		this.runLeft = runLeft;
	}
	public boolean isRunRight() {
		return runRight;
	}
	public void setRunRight(boolean runRight) {
		this.runRight = runRight;
	}

	@Override
	protected float getVelocityX() {
		float outVelX = velX;
		
		if (runLeft) outVelX -= runSpeed;
		if (runRight) outVelX += runSpeed;
		
		return outVelX;
	}
	
	@Override
	protected float getVelocityY() {
		return velY;
	}
	
	public float getScreenPosX() {
		return Graphics.gridToScreenX(posX);
	}
	public float getScreenPosY() {
		return Graphics.gridToScreenY(posY);
	}
	
	public float getNextUpgrade() {
		return nextUpgrade;
	}
	public void setNextUpgrade(float nextUpgrade) {
		this.nextUpgrade = nextUpgrade;
	}
	public void incNextUpgrade(float nextUpgrade) {
		this.nextUpgrade += nextUpgrade;
	}
	public float getUpgradePoints() {
		return upgradePoints;
	}
	public void setUpgradePoints(float upgradePoints) {
		this.upgradePoints = upgradePoints;
	}
	public void incUpgradePoints(float upgradePoints) {
		this.upgradePoints += upgradePoints;
	}
	
	@Override
	protected void onDestruction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action(float frameSpeed) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(float frameSpeed) {
		move(frameSpeed);
	}
	
	@Override
	public void render() {
		if (mesh == null) gameStateChange();
		
		mesh.render();
	}

	public void gameStateChange() {
		float x1 = Graphics.gridToScreenX(-player.getWidth()/2) + 1;
		float x2 = -x1;
		float y1 = Graphics.gridToScreenY(-player.getHeight()/2) - 1;
		float y2 = -y1;
		
		MeshBuilder meshBuilder = new MeshBuilder();
		
		Colour playerColour = Graphics.getColourScheme().player();
		
		meshBuilder.pushPoint(x1, y1, playerColour);
		meshBuilder.pushPoint(x2, y1, playerColour);
		meshBuilder.pushPoint(x1, y2, playerColour);
		
		meshBuilder.pushPoint(x2, y1, playerColour);
		meshBuilder.pushPoint(x1, y2, playerColour);
		meshBuilder.pushPoint(x2, y2, playerColour);
		
		mesh = meshBuilder.asColourMesh(false, true);
	}

	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public void dash() {
		
	}
	
}
