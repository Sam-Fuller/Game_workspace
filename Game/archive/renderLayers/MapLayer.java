package renderLayers;

import java.util.stream.IntStream;

import blocks.Colour;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import main.GameWindow;
import world.Chunk;
import world.World;

public class MapLayer {
	public static void draw() {
		//draw previous chunk
		if (World.level > 0) {
			if (World.world.get(World.level).getEntrance() == 0) 		drawChunk(World.level-1, 0, 												World.world.get(World.level-1).getLevelSize());
			else if (World.world.get(World.level).getEntrance() == 1) 	drawChunk(World.level-1, World.world.get(World.level-1).getLevelSize(),		0);
			else if (World.world.get(World.level).getEntrance() == 3) 	drawChunk(World.level-1, -World.world.get(World.level).getLevelSize(),		0);
		}

		//draw current chunk
		drawChunk(World.level, 0, 0);

		//draw next chunk
		if (World.world.get(World.level).getExit() == 1) 		drawChunk(World.level+1, World.world.get(World.level+1).getLevelSize(),	0);
		else if (World.world.get(World.level).getExit() == 2) 	drawChunk(World.level+1, 0,					 							-World.world.get(World.level).getLevelSize());
		else if (World.world.get(World.level).getExit() == 3) 	drawChunk(World.level+1, -World.world.get(World.level).getLevelSize(), 	0);

		for (Projectile projectile: World.projectiles) {
			projectile.draw(World.getOffsetX(projectile.getChunkNo()), World.getOffsetY(projectile.getChunkNo()));
		}
	}

	public static void drawChunk(int chunkNo, float offsestX, float offsestY) {
		Chunk chunk = World.world.get(chunkNo);

		//get offsets
		float minX = offsestX + World.posX - (GameWindow.gameX+Player.playerWidth)/2;
		float minY = offsestY + World.posY - (GameWindow.gameY+Player.playerHeight)/2;

		for (Entity enemy: chunk.getEntities()) {
			enemy.checkHitbox(minX, minY);
		}
		
		for (int x = (int) minX;	x<(minX+GameWindow.gameX+Player.playerWidth) && x<chunk.getLevelSize();	x++) {
			if (x < 0) x = 0;
			for (int y = (int) minY;	y<(minY+GameWindow.gameY+Player.playerHeight) && y<chunk.getLevelSize();	y++) {
				if (y < 0) y = 0;
				Colour light = World.getLight(chunkNo, x, y);

				if (chunk.getMap()[x][y].textured()) {
					if (light.red>0 || light.green>0 || light.blue>0) GameWindow.drawSquare(x - minX, y - minY, chunk.getMap()[x][y].getTexture(), chunk.getMap()[x][y].blend(), light);
				} else {
					light.red += 0.25f; light.green += 0.25f; light.blue += 0.25f;
					if (light.red < 0.5f) light.red = 0.5f;
					if (light.green < 0.5f) light.green = 0.5f;
					if (light.blue < 0.5f) light.blue = 0.5f;

					GameWindow.drawSquare(x - minX, y - minY, chunk.getMap()[x][y].getColour(), light);
				}
			}
		}

		for (Entity enemy: chunk.getEntities()) {
			enemy.draw(minX, minY);
		}
	}
}
