package event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import event.events.Jump;
import event.events.Left;
import event.events.Menu;
import event.events.Right;

public class KeyPress {
	public static List<KeyBind> bindList = new ArrayList<KeyBind>();
	
	public static void init() {
		//bindList.add(new KeyBind(GLFW.GLFW_KEY_ESCAPE, new Menu()));
		
		bindList.add(new KeyBind(GLFW.GLFW_KEY_A, new Left()));
		bindList.add(new KeyBind(GLFW.GLFW_KEY_D, new Right()));
		bindList.add(new KeyBind(GLFW.GLFW_KEY_SPACE, new Jump()));
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
