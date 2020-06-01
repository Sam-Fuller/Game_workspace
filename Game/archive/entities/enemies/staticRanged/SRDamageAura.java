package entities.enemies.staticRanged;

import blocks.Colour;
import entities.Player;
import main.GameWindow;
import renderLayers.textureLoader;
import world.World;

public class SRDamageAura extends StaticRanged {
	static final int dHeight = 3;
	static final int dWidth = 2;

	static final float damage = 0.5f;
	static final float damageRadius = 10;
	
	static int textureID;

	public static void load() {
		textureID = textureLoader.load("enemies/SR/damageAura.png");
	}

	public SRDamageAura(float posX, float posY, int level) {
		super(dHeight, dWidth, posX, posY, level);
	}

	@Override
	public void action(float frameSpeed) {
		float dist = Player.distance(getChunkNo(), getPosX(), getPosY());

		if (dist < damageRadius)
			Player.decHealth(damage * frameSpeed);
	}

	@Override
	public void draw(float offsetX, float offsetY) {
		float x = getPosX()-offsetX-getWidth();
		float y = getPosY()-offsetY-getHeight();

		Colour light = World.getLight(getChunkNo(), getPosX(), getPosY());
		GameWindow.drawPoly(x, y, x+getWidth(), y+getHeight(), textureID, true, light);

		float dist = Player.distance(getChunkNo(), getPosX(), getPosY());
		
		if (dist < 2*damageRadius) {
			Colour colourIn = new Colour(0.4f, 0.2f, 0.2f, 0f);
			Colour colourOut = new Colour(0.4f, 0.2f, 0.2f, 1-((dist/damageRadius)-1));

			GameWindow.drawSectorCircle(x+getWidth()/2, y+getHeight()/2, damageRadius-1, damageRadius, 20, colourIn, colourOut);
		}
	}

	@Override
	public int getLightRadius() {
		return 5;
	}

	@Override
	public Colour glowColour() {
		return Colour.RED;
	}

	@Override
	public float getLightX() {
		return 1.5f;
	}

	@Override
	public float getLightY() {
		return 2.5f;
	}
}
