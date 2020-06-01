package world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import blocks.Block;
import blocks.NoBlock;
import blocks.StoneBlock1;
import entities.Entity;

public class ShipChunk implements Chunk{
	public static final File ShipChunkImage = new File("resources/chunks/ShipChunk.png");
		
	public static int levelSize = 0;
	public Block[][] map;
	
	public static final int blackBlock = 0xff000000;
	
	public ShipChunk() {
		try {
			BufferedImage image = ImageIO.read(ShipChunkImage);
			
			levelSize = image.getWidth() < image.getHeight() ? image.getWidth() : image.getHeight();
			map = new Block[levelSize][levelSize];			
			
			for (int x = 0; x < levelSize; x++) {
				for (int y = 0; y < levelSize; y++) {
					if (image.getRGB(x, y) == blackBlock) {
						map[x][y] = new StoneBlock1();
					} else {
						map[x][y] = new NoBlock();
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getExit() {
		return 2;
	}

	@Override
	public int[][] getExitPerlin() {
		return RandomChunk.generatePerlin();
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
	public Block[][] getMap() {
		return map;
	}

	@Override
	public List<Entity> getEntities() {
		return new ArrayList<Entity>();
	}
}
