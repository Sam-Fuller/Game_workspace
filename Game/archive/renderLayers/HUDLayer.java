package renderLayers;

import static org.lwjgl.opengl.GL11.glColor4f;

import org.lwjgl.opengl.GL11;

import blocks.Colour;
import entities.Player;
import main.GameWindow;

public class HUDLayer {
	public static boolean display = true;
	
	//centre of the hud
	public static final float xCentre = 15;
	public static final float yCentre = 15;
	
	//upgrade offsets
	public static final float xOffset1 = 4;
	public static final float xOffset2 = 3;
	public static final float yOffset1 = 2;
	public static final float yOffset2 = 6;
	
	public static final float hpxpOffsetRotation = 0.975f;

	public static void init() {
		
	}
	
	public static void draw() {
		if (!display) return;
		
		//blue light
		GameWindow.drawCircle(xCentre, yCentre, 6, 20, Colour.LIGHTBLUE, Colour.TLIGHTBLUE);
		
		GameWindow.drawSectorCircle(xCentre, yCentre, 6, 10, 50, Colour.TLIGHTBLUE, Colour.LIGHTBLUE);
		GameWindow.drawSectorCircle(xCentre, yCentre, 12, 13, 50, Colour.LIGHTBLUE, Colour.TLIGHTBLUE);
		
		//inner circles
		GameWindow.drawSectorCircle(xCentre, yCentre, 2, 2.4f, 20, Colour.WHITE);
		GameWindow.drawSectorCircle(xCentre, yCentre, 4f, 5f, 30, Colour.WHITE);
		GameWindow.drawSectorCircle(xCentre, yCentre, 6, 6.4f, 80, Colour.WHITE);
		
		//ability 1
		GameWindow.drawSectorCircle(xCentre - xOffset1, yCentre - yOffset2, 2.5f, 3f, 30, Colour.LIGHTGRAY);
		
		//ability 2
		GameWindow.drawSectorCircle(xCentre + xOffset2, yCentre - yOffset1, 2.5f, 3f, 30, Colour.LIGHTGRAY);
		
		//ability 3
		GameWindow.drawSectorCircle(xCentre - xOffset2, yCentre + yOffset1, 2.5f, 3f, 30, Colour.LIGHTGRAY);
		
		//ability 4
		GameWindow.drawSectorCircle(xCentre + xOffset1, yCentre + yOffset2, 2.5f, 3f, 30, Colour.LIGHTGRAY);
		
		
		Colour cXP = new Colour(0, 0.612f, 0.612f);
		Colour cHP = new Colour(0.612f, 0, 0);
		
		//health and xp	
		float upgradeLimit = (float) (Math.PI * (Player.getUpgradePoints()/Player.getNextUpgrade()));
		GameWindow.drawSectorArc(xCentre, yCentre, 10.3f, 11.7f, upgradeLimit + hpxpOffsetRotation, (float) (Math.PI) + hpxpOffsetRotation, 20, cXP);
		
		float healthLimit = (float) -(Math.PI * (Player.getHealth()/Player.getMaxHealth()));
		GameWindow.drawSectorArc(xCentre, yCentre, 10.3f, 11.7f, healthLimit + hpxpOffsetRotation, (float) hpxpOffsetRotation, 20, cHP);
		

		GameWindow.drawSectorCircle(xCentre, yCentre, 10, 10.35f, 50, Colour.WHITE);
		GameWindow.drawSectorCircle(xCentre, yCentre, 11.65f, 12, 50, Colour.WHITE);
		
		
		GL11.glBegin(GL11.GL_QUADS);
		
		Colour colour = Colour.WHITE;

		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);
		
		GL11.glVertex2f(GameWindow.actualX(xCentre + 5.4f),	GameWindow.actualY(yCentre + 8.5f));
		GL11.glVertex2f(GameWindow.actualX(xCentre + 5.7f),	GameWindow.actualY(yCentre + 8.2f));
		
		GL11.glVertex2f(GameWindow.actualX(xCentre + 6.8f),	GameWindow.actualY(yCentre + 9.8f));
		GL11.glVertex2f(GameWindow.actualX(xCentre + 6.5f),	GameWindow.actualY(yCentre + 10.1f));
		
		GL11.glEnd();
		
		
		
		GL11.glBegin(GL11.GL_QUADS);
				
		GL11.glVertex2f(GameWindow.actualX(xCentre - 5.4f),	GameWindow.actualY(yCentre - 8.5f));
		GL11.glVertex2f(GameWindow.actualX(xCentre - 5.7f),	GameWindow.actualY(yCentre - 8.2f));
		
		GL11.glVertex2f(GameWindow.actualX(xCentre - 6.8f),	GameWindow.actualY(yCentre - 9.8f));
		GL11.glVertex2f(GameWindow.actualX(xCentre - 6.5f),	GameWindow.actualY(yCentre - 10.1f));
		
		GL11.glEnd();
	}
}