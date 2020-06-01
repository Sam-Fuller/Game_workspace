package weapons;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import entities.Player;
import main.GameWindow;

public class RunWeaponFire implements Runnable{
	public boolean interrupted = false;
	public boolean firing = false;

	@Override
	public void run() {
		while (!interrupted) {
			try {
				Thread.sleep(1);
				
				if (firing) {
					Player.getWeapon().fire();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
