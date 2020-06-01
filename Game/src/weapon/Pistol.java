package weapon;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import colour.Colour;
import entity.Player;
import entity.Projectile;
import main.Settings;
import runnable.Graphics;
import state.World;

public class Pistol extends Weapon {
	static final long defaultFireRate = 250000000;
	static final float defaultProjectileSpeed = 0.5f;
	static final float defaultProjectileSize = 0.5f;

	public Pistol() {
		super(defaultFireRate, defaultProjectileSpeed);
	}

	public void fire() {
		long now = System.nanoTime();
		if (lastFired+fireRate > now) return;
		lastFired = now;
		
		Projectile projectile = new Projectile(true, defaultProjectileSize, defaultProjectileSize,
				Player.getPlayer().getChunkNo(),
				Player.getPlayer().getPosX() - 1 + (float)Player.getPlayer().getWidth()/2f,
				Player.getPlayer().getPosY() - (float)Player.getPlayer().getHeight()/2f,
				Graphics.getColourScheme().pistolProjectile());

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
