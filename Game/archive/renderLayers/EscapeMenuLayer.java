package renderLayers;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.nio.DoubleBuffer;

import blocks.Colour;
import event.events.EventMenu;
import main.GameWindow;

public class EscapeMenuLayer {
	public static boolean display = false;
	
	public static final float xOffset = GameWindow.actualX(30);
	
	public static final float yOffset = GameWindow.actualY(30);
	
	public static final float sizeLarge = GameWindow.actualX(7);
	public static final float sizeSmall = GameWindow.actualX(3);
	
	static boolean hoverResume = false;
	static boolean hoverSettings = false;
	static boolean hoverQuit = false;
	
	public static void draw() {
		if (!display) return;
		
		drawBackground();
		
		drawLine();
		
		drawBracket(xOffset, 							yOffset-GameWindow.actualY(3), yOffset+GameWindow.actualY(20), 1);
		drawBracket(xOffset + GameWindow.actualX(43), 	yOffset-GameWindow.actualY(3), yOffset+GameWindow.actualY(20), -1);
		
		if (hoverResume) 	TTF.drawLargeText("[Resume]", 			xOffset + GameWindow.actualX(2.5f),	yOffset,							sizeLarge,	Colour.WHITE);
		else 				TTF.drawLargeText(" Resume", 			xOffset + GameWindow.actualX(2.5f),	yOffset,							sizeLarge,	Colour.WHITE);
		
		if (hoverSettings)	TTF.drawLargeText("[Settings]", 		xOffset + GameWindow.actualX(5f),	yOffset + GameWindow.actualY(10),	sizeSmall,	Colour.WHITE);
		else				TTF.drawLargeText(" Settings", 			xOffset + GameWindow.actualX(5f),	yOffset + GameWindow.actualY(10),	sizeSmall,	Colour.WHITE);
		
		if (hoverQuit)		TTF.drawLargeText("[Save and Quit]", 	xOffset + GameWindow.actualX(5f),	yOffset + GameWindow.actualY(15),	sizeSmall,	Colour.WHITE);
		else				TTF.drawLargeText(" Save and Quit", 	xOffset + GameWindow.actualX(5f),	yOffset + GameWindow.actualY(15),	sizeSmall,	Colour.WHITE);
	}
	
	public static void drawBackground() {		
		glEnable(GL_BLEND);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);


		// draw quad
		glBegin(GL11.GL_QUADS);
		Colour colour = Colour.PLIGHTBLUE;
		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);
		
		glVertex2f(xOffset,								yOffset - GameWindow.actualY(3));
		glVertex2f(xOffset + GameWindow.actualX(43),	yOffset - GameWindow.actualY(3));
		glVertex2f(xOffset + GameWindow.actualX(43),	yOffset + GameWindow.actualY(20));
		glVertex2f(xOffset,								yOffset + GameWindow.actualY(20));
		glEnd();

		glDisable(GL_BLEND);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
	}
	
	public static void drawLine() {
		glBegin(GL11.GL_QUADS);
		glColor3f(1, 1, 1);
		
		glVertex2f(GameWindow.actualX(15),	GameWindow.actualX(15));
		glVertex2f(GameWindow.actualX(15),	GameWindow.actualX(15));
		glVertex2f(xOffset,								yOffset - GameWindow.actualY(1));
		glVertex2f(xOffset,								yOffset);
		glEnd();
	}
	
	public static void drawBracket(float x, float y1, float y2, int rev) {
		glColor4f(1, 1, 1, 1);

		glBegin(GL11.GL_QUADS);
		glVertex2f(x-GameWindow.actualX(1f),	y1);
		glVertex2f(x+GameWindow.actualX(1f),	y1);
		glVertex2f(x+GameWindow.actualX(1f),	y2);
		glVertex2f(x-GameWindow.actualX(1f),	y2);
		glEnd();

		glBegin(GL11.GL_QUADS);
		glVertex2f(x-GameWindow.actualX(1f*rev),	y1-GameWindow.actualY(1f));
		glVertex2f(x+GameWindow.actualX(4f*rev),	y1-GameWindow.actualY(1f));
		glVertex2f(x+GameWindow.actualX(4f*rev),	y1+GameWindow.actualY(1f));
		glVertex2f(x-GameWindow.actualX(1f*rev),	y1+GameWindow.actualY(1f));
		glEnd();

		glBegin(GL11.GL_QUADS);
		glVertex2f(x-GameWindow.actualX(1f*rev),	y2-GameWindow.actualY(1f));
		glVertex2f(x+GameWindow.actualX(4f*rev),	y2-GameWindow.actualY(1f));
		glVertex2f(x+GameWindow.actualX(4f*rev),	y2+GameWindow.actualY(1f));
		glVertex2f(x-GameWindow.actualX(1f*rev),	y2+GameWindow.actualY(1f));
		glEnd();
	}
	
	public static void onClick(double xmousepos, double ymousepos) {
		//if resume click
		if (insideResume(xmousepos, ymousepos)) new EventMenu().onPress();
		
		if (insideSettings(xmousepos, ymousepos)) ;
		
		if (insideQuit(xmousepos, ymousepos)) GameWindow.quit();
	}

	public static void onMove(double xpos, double ypos) {
		//if inside resume
		if (insideResume(xpos, ypos)) hoverResume = true;
		else hoverResume = false;
		
		if (insideSettings(xpos, ypos)) hoverSettings = true;
		else hoverSettings = false;
		
		if (insideQuit(xpos, ypos)) hoverQuit = true;
		else hoverQuit = false;
	}
	
	public static boolean insideResume(double x, double y) {
		return inside(x, y, xOffset, yOffset, xOffset + GameWindow.actualX(35), yOffset + sizeLarge);
	}
	
	public static boolean insideSettings(double x, double y) {
		return inside(x, y, xOffset, yOffset + GameWindow.actualY(10), xOffset + GameWindow.actualX(20), yOffset + GameWindow.actualY(10) + sizeSmall);
	}
	
	public static boolean insideQuit(double x, double y) {
		return inside(x, y, xOffset, yOffset + GameWindow.actualY(15), xOffset + GameWindow.actualX(30), yOffset + GameWindow.actualY(15) + sizeSmall);
	}
	
	public static boolean inside(double x, double y, double x1, double y1, double x2, double y2) {
		if (x < x1) return false;
		if (y < y1) return false;
		if (x > x2) return false;
		if (y > y2) return false;
		return true;
	}
} 