package event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import event.events.EventClick;

public class MouseButtonEventHandler {
	public static List<KeyBind> bindList = new ArrayList<KeyBind>();
	
	public static void init() {
		bindList.add(new KeyBind(GLFW.GLFW_MOUSE_BUTTON_1, new EventClick()));
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
