package entities;

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
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

import blocks.Colour;
import main.GameWindow;
import renderLayers.AlertLayer;
import renderLayers.RenderLayer;
import renderLayers.TTF;

public class Alert implements RenderLayer{
	public static final float openSpeed = 0.5f;

	public float time;

	public float x;
	public float y;

	public float height;

	public float maxWidth;
	public float width;

	public String title;
	public String text;

	boolean opening = true;
	boolean closing = false;
	
	float opened = 0;

	//public RunAlert runAlert;
	public Thread threadAlert;

	public Alert(float x, float y, float maxWidth, float height, String title, String text) {
		this(x, y, maxWidth, height, 5, title, text);
	}

	public Alert(float x, float y, float maxWidth, float height, float time, String title, String text) {
		this.x = x;
		this.y = y;

		this.maxWidth = maxWidth;
		this.height = height;
		this.time = time * 1000000000;

		this.width = 0;

		this.title = title;
		this.text = text;

		AlertLayer.alerts.add(this);
	}

	@Override
	public void draw(float frameSpeed) {
		if (opening) {
			if (width < maxWidth) alterWidth(frameSpeed*openSpeed);
			if (width >= maxWidth) {
				opening = false;
				opened = System.nanoTime();
			}
		}else if (closing) {
			if (width > 0f) alterWidth(-frameSpeed*openSpeed);
			if (width <= 0f) {
				AlertLayer.alerts.remove(this);
			}
		}else if ((time > 0) && (System.nanoTime() - opened > time)) {
			closing = true;
		}

		float left = x + maxWidth/2 + width/2;
		float right = x + maxWidth/2 - width/2;

		drawBackground(left, right);
		if (width == maxWidth) drawText(left, right);

		if (width > 0.5) {
			drawBracket(left, y, y+height, -1);	
			drawBracket(right, y, y+height, 1);
		}else {
			drawBracket(left, y, y+height, 0);	
			drawBracket(right, y, y+height, 0);
		}
	}

	public void drawBackground(float left, float right) {		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);


		// draw quad
		glBegin(GL11.GL_QUADS);
		Colour colour = Colour.PLIGHTBLUE;
		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		glVertex2f(GameWindow.actualX(left),	GameWindow.actualY(y));
		glVertex2f(GameWindow.actualX(right),	GameWindow.actualY(y));
		glVertex2f(GameWindow.actualX(right),	GameWindow.actualY(y+height));
		glVertex2f(GameWindow.actualX(left),	GameWindow.actualY(y+height));
		glEnd();

		glDisable(GL_BLEND);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
	}
	public void drawText(float left, float right) {
		TTF.drawLargeText(title, GameWindow.actualX(x+1), GameWindow.actualY(y + 0.5f), GameWindow.actualX(1.5f), Colour.WHITE);
		TTF.drawLargeText(text, GameWindow.actualX(x+1), GameWindow.actualY(y + 2), GameWindow.actualX(1), Colour.WHITE);
	}
	public void drawBracket(float x, float y1, float y2, int rev) {
		glColor4f(1, 1, 1, 1);

		glBegin(GL11.GL_QUADS);
		glVertex2f(GameWindow.actualX(x-0.25f),	GameWindow.actualY(y1));
		glVertex2f(GameWindow.actualX(x+0.25f),	GameWindow.actualY(y1));
		glVertex2f(GameWindow.actualX(x+0.25f),	GameWindow.actualY(y2));
		glVertex2f(GameWindow.actualX(x-0.25f),	GameWindow.actualY(y2));
		glEnd();

		glBegin(GL11.GL_QUADS);
		glVertex2f(GameWindow.actualX(x-0.25f*rev),	GameWindow.actualY(y1-0.25f));
		glVertex2f(GameWindow.actualX(x+1f*rev),	GameWindow.actualY(y1-0.25f));
		glVertex2f(GameWindow.actualX(x+1f*rev),	GameWindow.actualY(y1+0.25f));
		glVertex2f(GameWindow.actualX(x-0.25f*rev),	GameWindow.actualY(y1+0.25f));
		glEnd();

		glBegin(GL11.GL_QUADS);
		glVertex2f(GameWindow.actualX(x-0.25f*rev),	GameWindow.actualY(y2-0.25f));
		glVertex2f(GameWindow.actualX(x+1f*rev),	GameWindow.actualY(y2-0.25f));
		glVertex2f(GameWindow.actualX(x+1f*rev),	GameWindow.actualY(y2+0.25f));
		glVertex2f(GameWindow.actualX(x-0.25f*rev),	GameWindow.actualY(y2+0.25f));
		glEnd();
	}

	public void alterWidth(float f) {
		width += f;
		if (width > maxWidth) width = maxWidth;
		if (width < 0f) width = 0f;
	}
}