package state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import chunk.Chunk;
import chunk.GeneratedChunk;
import chunk.LoadedChunk;
import colour.Colour;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import graphics.ColourMesh;
import graphics.Mesh;
import graphics.MeshBuilder;
import graphics.TexturedMesh;
import main.Game;
import runnable.Graphics;

public class World {
	static List<Chunk> world = new ArrayList<Chunk>();

	public static List<Chunk> getWorld() {
		return world;
	}

	public static void init() {
		//world.add(new LoadedChunk("resources/chunks/ShipChunk.png"));
		world.add(new GeneratedChunk(0, 2, GeneratedChunk.generatePerlin()));

		generateMap();
		generateMap();
		generateMap();

		Game.worldGenThread.start();
	}

	public static void generateMap() {
		if (world.size() - Player.getPlayer().getChunkNo() > 5) return;

		world.add(new GeneratedChunk(world.size(), world.get(world.size()-1).getExit(), world.get(world.size()-1).getExitPerlin()));
	}

	//TODO improve to work on more than -1 0 and 1
	public static int getOffsetX(int chunk) {
		chunk -= Player.getPlayer().getChunkNo();

		return getOffsetXrel(chunk);
	}

	public static int getOffsetY(int chunk) {
		chunk -= Player.getPlayer().getChunkNo();

		return getOffsetYrel(chunk);
	}

	public static int getOffsetXrel(int chunk) {
		if (chunk == -1) {
			if (World.world.get(Player.getPlayer().getChunkNo()).getEntrance() == 1) 		return -World.world.get(Player.getPlayer().getChunkNo()-1).getLevelSize();
			else if (World.world.get(Player.getPlayer().getChunkNo()).getEntrance() == 3) 	return World.world.get(Player.getPlayer().getChunkNo()).getLevelSize();
		} else if (chunk == 1) {
			if (World.world.get(Player.getPlayer().getChunkNo()).getExit() == 1) 		return -World.world.get(Player.getPlayer().getChunkNo()+1).getLevelSize();
			else if (World.world.get(Player.getPlayer().getChunkNo()).getExit() == 3) 	return World.world.get(Player.getPlayer().getChunkNo()).getLevelSize();
		}

		return 0;
	}

	public static int getOffsetYrel(int chunk) {
		if (chunk == -1) {
			if (World.world.get(Player.getPlayer().getChunkNo()).getEntrance() == 0) 		return -World.world.get(Player.getPlayer().getChunkNo()-1).getLevelSize();
		} else if (chunk == 1) {
			if (World.world.get(Player.getPlayer().getChunkNo()).getExit() == 2) 	return World.world.get(Player.getPlayer().getChunkNo()).getLevelSize();
		}

		return 0;
	}

	public static void update(float frameSpeed) {
		Player.getPlayer().update(frameSpeed);

		int playerChunk = Player.getPlayer().getChunkNo();

		if (playerChunk > 0) {
			for (Entity entity: world.get(playerChunk - 1).getEntities()) {
				entity.update(frameSpeed);
			}
		}
		for (Entity entity: world.get(playerChunk).getEntities()) {
			entity.update(frameSpeed);
		}
		for (Entity entity: world.get(playerChunk + 1).getEntities()) {
			entity.update(frameSpeed);
		}

	}

	public static void render() {
		if (World.getWorld().size() < Player.getPlayer().getChunkNo()+2) {
			generateMap();
		}

		if (Player.getPlayer().getChunkNo() > 0) world.get(Player.getPlayer().getChunkNo()-1).render(getOffsetXrel(-1), getOffsetYrel(-1));
		world.get(Player.getPlayer().getChunkNo()).render(getOffsetXrel(0), getOffsetYrel(0));
		world.get(Player.getPlayer().getChunkNo()+1).render(getOffsetXrel(1), getOffsetYrel(1));

		Player.getPlayer().render();

		if (Player.getPlayer().getChunkNo() > 0)
			for (Entity entity: world.get(Player.getPlayer().getChunkNo()-1).getEntities())
				entity.render();

		for (Entity entity: world.get(Player.getPlayer().getChunkNo()).getEntities()) {
			entity.render();
		}

		for (Entity entity: world.get(Player.getPlayer().getChunkNo()+1).getEntities())
			entity.render();
	}

}
