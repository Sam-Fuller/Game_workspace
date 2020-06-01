package event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import event.events.*;


public class KeyEventHandler {
	public static List<KeyBind> bindList = new ArrayList<KeyBind>();
	
	public static void init() {
		bindList.add(new KeyBind(GLFW.GLFW_KEY_ESCAPE, new EventMenu()));
		
		bindList.add(new KeyBind(GLFW.GLFW_KEY_A, new EventWalkLeft()));
		bindList.add(new KeyBind(GLFW.GLFW_KEY_D, new EventWalkRight()));
		bindList.add(new KeyBind(GLFW.GLFW_KEY_SPACE, new EventJump()));
	}

	public static void keyEvent(int key, int action) {
		if (action == GLFW.GLFW_PRESS) keyPressed(key);
		else if (action == GLFW.GLFW_RELEASE) keyReleased(key);
	}

	private static void keyPressed(int key) {	
		for (KeyBind keyBind : bindList) {
			if (keyBind.key == key) keyBind.action.onPress();
		}
	}

	private static void keyReleased(int key) {
		for (KeyBind keyBind : bindList) {
			if (keyBind.key == key) keyBind.action.onRelease();
		}
	}

}
