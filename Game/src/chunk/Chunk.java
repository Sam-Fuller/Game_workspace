package chunk;

import java.util.List;

import entity.Entity;
import graphics.Mesh;
import graphics.MeshBuilder;

public interface Chunk {
	public int getExit();
	public int[][] getExitPerlin();
	public int getEntrance();
	public int getLevelSize();
	public boolean[][] getMap();
	public void render(int xOffset, int yOffset);
	public List<Entity> getEntities();
}