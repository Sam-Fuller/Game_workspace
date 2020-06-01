package world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import blocks.Colour;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import main.GameWindow;

public class World {
	public static List<Chunk> world = new ArrayList<Chunk>();

	public static float posX = 150;
	public static float posY = 150;

	public static int level = 0;

	//the player object
	public static Player player = new Player();
	
	public static List<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>();

	public static void init() {
		//world.add(new ShipChunk());
		world.add(new RandomChunk(0, 2, RandomChunk.generatePerlin()));
		generateMap(level);
	}

	public static void generateMap(int level) {
		if (level+5 > world.size()) {
			world.add(new RandomChunk(world.size(), world.get(world.size()-1).getExit(), world.get(world.size()-1).getExitPerlin()));
		}
	}

	public static Colour getLight(int chunk, float x, float y) {
		Colour light = new Colour(Colour.BLACK);

		float offX = World.getOffsetX(chunk);
		float offY = World.getOffsetY(chunk);

		float brightness = 0;

		float distX = x + offX - World.posX + (float) Player.playerWidth/2 + 0.5f;
		float distY = y + offY - World.posY + (float) Player.playerHeight/2;

		if (distX < Player.getLightRadius() && distY < Player.getLightRadius()) {
			brightness = (Player.getLightRadius() - 
					(float) Math.sqrt(
							Math.pow(distX, 2)
							+ Math.pow(distY, 2)))
					/(Player.getLightRadius()/2);
		}

		if (brightness > 0) {
			if (brightness > 1) brightness = 1;
			light.red += 0.75f * brightness * (1-light.red);
			light.green += 0.75f * brightness * (1-light.green);
			light.blue += 0.75f * brightness * (1-light.blue);
		}

		for (int level = -1; level <= 1; level++) {
			if (World.level+level>=0) {
				for (Entity enemy: World.world.get(World.level+level).getEntities()) {
					if (enemy.getLightRadius() == 0) continue;

					distX = x + offX - enemy.getPosX() - World.getOffsetX(enemy.getChunkNo()) + enemy.getLightX();
					distY = y + offY - enemy.getPosY() - World.getOffsetY(enemy.getChunkNo()) + enemy.getLightY();

					if (distX < enemy.getLightRadius() && distY < enemy.getLightRadius()) {
						brightness = (enemy.getLightRadius() -
								(float) Math.sqrt(
										Math.pow(distX, 2)
										+ Math.pow(distY, 2)))
								/(enemy.getLightRadius()/2);
					} else {
						brightness = 0;
					}


					if (brightness > 0) {
						if (brightness > 1) brightness = 1;
						light.red += enemy.glowColour().red * brightness * (1-light.red);
						light.green += enemy.glowColour().green * brightness * (1-light.green);
						light.blue += enemy.glowColour().blue * brightness * (1-light.blue);
					}
				}
			}
		}

		return light;
	}

	public static float getOffsetX(int chunk) {
		chunk -= World.level;
		
		if (chunk == -1) {
			if (World.world.get(World.level).getEntrance() == 1) 		return -World.world.get(World.level-1).getLevelSize();
			else if (World.world.get(World.level).getEntrance() == 3) 	return World.world.get(World.level).getLevelSize();
		} else if (chunk == 1) {
			if (World.world.get(World.level).getExit() == 1) 		return -World.world.get(World.level+1).getLevelSize();
			else if (World.world.get(World.level).getExit() == 3) 	return World.world.get(World.level).getLevelSize();
		}

		return 0;
	}

	public static float getOffsetY(int chunk) {
		chunk -= World.level;
		
		if (chunk == -1) {
			if (World.world.get(World.level).getEntrance() == 0) 		return -World.world.get(World.level-1).getLevelSize();
		} else if (chunk == 1) {
			if (World.world.get(World.level).getExit() == 2) 	return World.world.get(World.level).getLevelSize();
		}

		return 0;
	}
}
