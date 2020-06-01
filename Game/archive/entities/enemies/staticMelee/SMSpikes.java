package entities.enemies.staticMelee;

import blocks.Colour;
import entities.Player;
import main.GameWindow;
import renderLayers.textureLoader;
import world.World;

public class SMSpikes extends StaticMelee{
	static final int dHeight = 1;
	static final int dWidth = 1;
	
	static final float damage = 10;
	static final long damageCooldown = 1000000000;
	
	long lastDamage = 0;
	
	static int textureID;
	
	public static void load() {
		textureID = textureLoader.load("enemies/SM/spike.png");
	}

	public SMSpikes(float posX, float posY, int level) {
		super(dHeight, dWidth, posX, posY, level);
	}

	@Override
	public void draw(float offsetX, float offsetY) {
		float x = getPosX()-offsetX-getWidth();
		float y = getPosY()-offsetY-getHeight();
		
		Colour light = World.getLight(getChunkNo(), getPosX(), getPosY());
		GameWindow.drawPoly(x, y, x+getWidth(), y+getHeight(), textureID, true, light);
	}

	@Override
	public void onContact(float x, float y) {
		long now = System.nanoTime();
		if (now - lastDamage > damageCooldown) {
			Player.decHealth(damage);
			lastDamage = now;
		}

		World.player.setVelocityX(x > 0? 2/(x+1) : 2/(x-1));
		World.player.setVelocityY(y > 0? 2/(y+1) : 2/(y-1));
	}

	@Override
	public int getLightRadius() {
		return 0;
	}

	@Override
	public Colour glowColour() {
		return Colour.BLACK;
	}

	@Override
	public float getLightX() {
		return 0;
	}

	@Override
	public float getLightY() {
		return 0;
	}
}