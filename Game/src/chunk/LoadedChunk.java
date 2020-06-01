package chunk;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import colour.Colour;
import entity.Entity;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;

public class LoadedChunk implements Chunk{
	//"resources/chunks/ShipChunk.png"

	public Mesh mesh;

	public static int levelSize = 0;
	public boolean[][] map;

	public static final int blackBlock = 0xff000000;

	List<Entity> entities = new CopyOnWriteArrayList<Entity>();

	public LoadedChunk(String fileName) {
		try {
			BufferedImage image = ImageIO.read(new File(fileName));

			levelSize = image.getWidth();
			map = new boolean[levelSize][levelSize*2];

			for (int x = 0; x < levelSize; x++) {
				for (int y = 0; y < levelSize*2; y++) {
					if (image.getRGB(x, y) == blackBlock) {
						map[x][y] = true;
					} else {
						map[x][y] = false;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		generateMesh();
	}

	private void generateMesh() {
		MeshBuilder meshBuilder = new MeshBuilder();

		for (int y = 0; y*2 < map[0].length; y++) {
			for (int x = -1; x < map.length; x++) {
				int gridOffset = y%2==0 ? 0 : 1;
				float hexagonalOffset = y%2==0 ? -0.25f : 0.25f;
				float xo = x+hexagonalOffset;
				float yo = y;

				float triangularOffset = 0.25f;

				float x1 = xo-triangularOffset;
				float x2 = xo+1-triangularOffset;
				float x1o = xo+triangularOffset;
				float x2o = xo+1+triangularOffset;
				float y1 = yo;
				float y2 = yo+1;

				Colour backgroundColour = Graphics.getColourScheme().cave();

				if (x >= 0) {
					if (!map[x][y*2]) {
						meshBuilder.drawTriangle(x1, y1,
								x2, y1,
								x1o, y2, backgroundColour);
					}
				}

				if (x+gridOffset >= 0 && x+gridOffset < map.length) {
					if (!map[x+gridOffset][y*2+1]) {
						meshBuilder.drawTriangle(x2, y1,
								x1o, y2,
								x2o, y2, backgroundColour);
					}
				}
			}
		}

		mesh = meshBuilder.asColourMesh(false);
	}

	@Override
	public void render(int xOffset, int yOffset) {
		mesh.render(xOffset, yOffset);
	}

	@Override
	public int getExit() {
		return 2;
	}

	@Override
	public int[][] getExitPerlin() {
		return GeneratedChunk.generatePerlin();
	}

	@Override
	public int getEntrance() {
		return 0;
	}

	@Override
	public int getLevelSize() {
		return levelSize;
	}

	@Override
	public boolean[][] getMap() {
		return map;
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}
}