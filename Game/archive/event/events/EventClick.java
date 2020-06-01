package event.events;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import main.GameWindow;
import main.Main;
import renderLayers.EscapeMenuLayer;
import weapons.Pistol;

public class EventClick implements Event {

	@Override
	public void onPress() {		
		if (EscapeMenuLayer.display) {
			DoubleBuffer bx = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer by = BufferUtils.createDoubleBuffer(1);
			
			GLFW.glfwGetCursorPos(GameWindow.window, bx, by);
			
			float xmousepos = (float) bx.get(0);
			float ymousepos = (float) by.get(0);
			
			EscapeMenuLayer.onClick(xmousepos, ymousepos);
			
		}else {
			Main.WeaponFire.firing = true;
		}
	}

	@Override
	public void onRelease() {
		if (EscapeMenuLayer.display) {
			
		}else {
			Main.WeaponFire.firing = false;
		}
	}

}
