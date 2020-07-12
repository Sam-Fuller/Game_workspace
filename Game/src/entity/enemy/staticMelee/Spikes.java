package entity.enemy.staticMelee;

import colour.Colour;
import entity.Player;
import graphics.Mesh;
import graphics.MeshBuilder;
import loader.TextureLoader;
import runnable.Graphics;
import state.World;

public class Spikes extends StaticMelee {
	static final int dHeight = 1;
	static final int dWidth = 1;

	public static final int startHealth = 10;
	public static final int healthScalar = 10;
	
	static final int damage = 5;
	static final long damageCooldown = 1000000000;

	long lastDamage = 0;

	Mesh colourMesh;

	public Spikes(float posX, float posY, int level) {
		super(dHeight, dWidth, posX, posY, level);
		
		int health = startHealth + level * healthScalar;
		setMaxHealth(health);
		setHealth(health);
	}

	@Override
	public void render() {
		if (colourMesh == null) rerender();

		colourMesh.render(getPosX()+World.getOffsetX(getChunkNo()), getPosY()+World.getOffsetY(getChunkNo()));
	}

	private void rerender() {
		MeshBuilder meshBuilder = new MeshBuilder();

		Colour enemyColour = Graphics.getColourScheme().enemySpikes();
		meshBuilder.drawTriangle(0, 0, getHeight(), getWidth(), (float) Math.PI, enemyColour, enemyColour);

		colourMesh = meshBuilder.asColourMesh(false);
	}

	@Override
	public void action(float frameSpeed) {
		long now = System.nanoTime();

		if (now - lastDamage > damageCooldown) {
			float x = getPosX() + World.getOffsetX(getChunkNo()) - Player.getPlayer().getPosX();
			float y = getPosY() + World.getOffsetY(getChunkNo()) - Player.getPlayer().getPosY();
						
			if (this.touchingSquarePlayer()) {
				float a = Player.getPlayer().getPosX();
				float b = Player.getPlayer().getPosY();
				float c = getPosX();
				float d = getPosY();
				
				Player.getPlayer().decHealth(damage);
				lastDamage = now;

				Player.getPlayer().incVelocityX(x > 0? -2f/(x+1) : -2f/(x-1));
				Player.getPlayer().incVelocityY(y > 0? -2f/(y+1) : -2f/(y-1));
			}
		}
	}
	
	@Override
	public void onDestruction() {
		World.getWorld().get(getChunkNo()).getEntities().remove(this);

		colourMesh.cleanUp();
	}
}