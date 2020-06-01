package state;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import entity.Player;
import loader.TextureLoader;
import runnable.Graphics;
import upgrade.UpgradeMenu;

public class GameState {	
	public static enum State {
		MENU,
		ESCAPEMENU,
		SETTINGSMENU,
		INGAME,
		UPGRADEMENU
	}
	
	static State currentState;
	
	/**
	 * create new game state with all layers
	 */
	public GameState() {

	}

	/**
	 * load all textures in all layers
	 * must be performed before rendering
	 */
	public static void loadTextures() {
		// TODO Auto-generated method stub
		TextureLoader.loadAllTextures();
	}
	
	public static void changeState(State newState) {
		currentState = newState;
	}
	
	public static State getState() {
		return currentState;
	}

	/**
	 * update all framerate-bound entities
	 * render one frame
	 * @param frameSpeed
	 */
	public static void render(float frameSpeed) {
		if (currentState == State.MENU) {
			World.update(frameSpeed);
			
			World.render();
			Hud.render();
			
			//MainMenu.render();
			
			//UpgradeMenu.render();
		}
		
		glBindVertexArray(0);
	}
	
	/**
	 * update all non-framerate-bound entities
	 * @param frameSpeed
	 */
	public static void action(float frameSpeed) {
		
	}
	
	
	public static void fire() {
		if (currentState == State.MENU) {
			Player.getPlayer().getWeapon().fire();
		}
	}
	
	public static void click() {
		DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer by = BufferUtils.createDoubleBuffer(1);

		GLFW.glfwGetCursorPos(Graphics.getWindow(), bx, by);

		float ypos = Graphics.resToGridx((float) bx.get(0));
		float xpos = Graphics.resToGridy((float) by.get(0));
		
		if (currentState == State.MENU) {
			UpgradeMenu.cursorClick(ypos, xpos);
		}
	}

	public static void cursorMove(float xpos, float ypos) {
		if (currentState == State.MENU) {

		}
	}

	public static void dash() {
		if (currentState == State.MENU) {
			Player.getPlayer().dash();
		}
	}
	
}
