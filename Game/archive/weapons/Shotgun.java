package weapons;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import blocks.Colour;
import entities.Projectile;
import main.GameWindow;
import world.World;

public class Shotgun extends Weapon {
	long lastFired = 0;
	long fireRate = 1000000000;

	static final float projectileNo = 4;
	static final float projectileSpread = 0.1f;

	static final float projectileSpeed = 0.5f;

	@Override
	public void fire() {
		long now = System.nanoTime();
		if (lastFired+fireRate > now) return;
		lastFired = now;

		DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer by = BufferUtils.createDoubleBuffer(1);

		GLFW.glfwGetCursorPos(GameWindow.window, bx, by);

		float xmousepos = (float) bx.get(0);
		float ymousepos = (float) by.get(0);
		
		float XRelMousePos = GameWindow.gridX(xmousepos) - GameWindow.gameX/2;
		float YRelMousePos = GameWindow.gridY(ymousepos) - GameWindow.gameY/2;
		
		float angle = (float) Math.atan(YRelMousePos/(XRelMousePos==0?0.01f:XRelMousePos));
				
		float max = projectileNo/ 2f;
		for (float i = -(projectileNo-1)/2f; i < max; i++) {

			Projectile projectile = new Projectile(true, 0.5f, 0.5f, World.level, World.player.getPosX(), World.player.getPosY(), Colour.WHITE, Colour.WHITE);

			projectile.setHazard(false);
			projectile.setDamage(10);
			
			float projAngle = angle + i*projectileSpread;
			
			float velX = (float) (projectileSpeed*Math.cos(projAngle)) * (XRelMousePos<0?-1:1);
			float velY = (float) (projectileSpeed*Math.sin(projAngle)) * (XRelMousePos<0?-1:1);
			
			projectile.setVelocities(velX, velY);

			World.projectiles.add(projectile);

		}
	}

}
