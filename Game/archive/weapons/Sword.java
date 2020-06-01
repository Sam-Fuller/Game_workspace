package weapons;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import blocks.Colour;
import entities.Projectile;
import main.GameWindow;
import world.World;

public class Sword extends Weapon {
	long lastFired = 0;
	long fireRate = 500000000;
	
	static final float projectileSpeed = 0.5f;
	static final long timeLimit = 500000000;
	
	public void fire() {
		long now = System.nanoTime();
		if (lastFired+fireRate > now) return;
		lastFired = now;
		
		Projectile projectile = new Projectile(true, 5f, 5f, World.level, World.player.getPosX(), World.player.getPosY(), Colour.WHITE, Colour.WHITE);
		
		projectile.setHazard(false);
		projectile.setDamage(10);
		
		DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer by = BufferUtils.createDoubleBuffer(1);

		GLFW.glfwGetCursorPos(GameWindow.window, bx, by);

		float xmousepos = (float) bx.get(0);
		float ymousepos = (float) by.get(0);
		
		float XRelMousePos = GameWindow.gridX(xmousepos) - GameWindow.gameX/2;
		float YRelMousePos = GameWindow.gridY(ymousepos) - GameWindow.gameY/2;
		
		float mouseDistance = (float) Math.sqrt(Math.pow(XRelMousePos, 2) + Math.pow(YRelMousePos, 2));
		float velX = projectileSpeed*XRelMousePos / mouseDistance;
		float velY = projectileSpeed*YRelMousePos / mouseDistance;
		
		projectile.setVelocities(velX, velY);
		
		projectile.setTimeLimit(timeLimit);
		projectile.setDestroyOnDamage(false);
				
		World.projectiles.add(projectile);
	}
}
