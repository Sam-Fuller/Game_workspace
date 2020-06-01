package world;

import java.util.List;

import blocks.Block;
import entities.Entity;

public interface Chunk {
	public int getExit();
	public int[][] getExitPerlin();
	public int getEntrance();
	public int getLevelSize();
	public Block[][] getMap();
	public List<Entity> getEntities();
}
