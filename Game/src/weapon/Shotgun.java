package weapon;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import colour.Colour;
import entity.Player;
import entity.Projectile;
import graphics.MeshBuilder;
import main.Settings;
import runnable.Graphics;
import state.World;

class ShotgunProjectile extends Projectile {
	public static final long defaultTimeLimit = 500000000l;
	public static final float growthMultiplier = 0.5f;
	
	public ShotgunProjectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, Colour colourIn, Colour colourOut) {
		super(madeByPlayer, height, width, chunkNo, posX, posY, colourIn, colourOut);
		
		setDestroy(false);
		setDestroyOnDamage(false);
		
		setTimeLimit(defaultTimeLimit);
	}
	
	@Override
	public void action(float frameSpeed) {		
		if (timeLimited) if (timeLimit < System.nanoTime()) toBeDestroyed = true;

		moveChunk();
		
		incPosX(getVelocityX()*frameSpeed);
		incPosY(getVelocityY()*frameSpeed);
		
		this.setHeight(getHeight()+frameSpeed*growthMultiplier);
		this.setWidth(getWidth()+frameSpeed*growthMultiplier);
		
		if (isMadeByPlayer()) checkEnemyHitbox();
	}
	
	@Override
	public void update(float frameSpeed) {
		if (toBeDestroyed) onDestruction();
		
		this.gameStateChange();
	}
	
	@Override
	public void gameStateChange() {
		float x = Graphics.gridToScreenX(0);//+0.5f;//+Graphics.getGameX()/2+1f;
		float y = Graphics.gridToScreenY(0)-1;//.5f;//+Graphics.getGameY()/2+1f;

		MeshBuilder meshBuilder = new MeshBuilder();

		meshBuilder.drawSectorCircle(x, y, getWidth()/2-0.5f, getWidth()/2, 0, 40, colourIn, colourOut);

		colourMesh = meshBuilder.asColourMesh(true);
	}
}

public class Shotgun extends Weapon {
	static final long defaultFireRate = 500000000;
	static final float defaultProjectileSpeed = 1f;
	static final float defaultProjectileSize = 0.5f;
	
	public Shotgun() {
		super(defaultFireRate, defaultProjectileSpeed);
	}

	@Override
	public void fire() {		
		long now = System.nanoTime();
		if (lastFired+fireRate > now) return;
		lastFired = now;
		
		Projectile projectile = new ShotgunProjectile(true, defaultProjectileSize, defaultProjectileSize,
				Player.getPlayer().getChunkNo(),
				Player.getPlayer().getPosX() - 1 + (float)Player.getPlayer().getWidth()/2f,
				Player.getPlayer().getPosY() - (float)Player.getPlayer().getHeight()/2f,
				Graphics.getColourScheme().shotgunInsideProjectile(),
				Graphics.getColourScheme().shotgunOutsideProjectile());

		projectile.setHazard(false);
		projectile.setDamage(10);

		DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer by = BufferUtils.createDoubleBuffer(1);

		GLFW.glfwGetCursorPos(Graphics.getWindow(), bx, by);

		float XMousePos = ((float)bx.get(0)-Settings.settings.xRes.getValue()/2) / Settings.settings.xRes.getValue();
		float YMousePos = ((float)by.get(0)-Settings.settings.yRes.getValue()/2) / Settings.settings.xRes.getValue();
				
		float mouseDistance = (float) Math.sqrt(Math.pow(XMousePos, 2) + Math.pow(YMousePos, 2));
		float velX = projectileSpeed*XMousePos / mouseDistance;
		float velY = projectileSpeed*YMousePos / mouseDistance;

		projectile.setVelocities(velX, velY);
		
		World.getWorld().get(Player.getPlayer().getChunkNo()).getEntities().add(projectile);
	}

}
