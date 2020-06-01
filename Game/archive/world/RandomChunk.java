package world;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import blocks.Block;
import blocks.NoBlock;
import blocks.StoneBlock1;
import entities.Entity;
import entities.enemies.mobileMelee.MMDasher;
import entities.enemies.staticMelee.SMSpikes;
import entities.enemies.staticRanged.SRDamageAura;
import main.GameWindow;
import settings.Settings;

public class RandomChunk implements Chunk {
	public static final int levelSize = 300;
	public static final int centreRad = 50;

	public static final double perlinMult = 1; 				//multiplier
	public static final int perlinMinX = 30;				//minimum x size
	public static final double perlinStartWeight = 10;		//start weight
	public static final double perlinWeightDecrement = 0.8;	//weight decrement
	public static final int perlinMaxRoofOctaveSize = 32; 	//roof max octave size
	public static final int perlinOffsetX = 0;				//x offset
	public static final int perlinMinY = 20;				//minimum y size

	public int entrance;
	public int exit;

	public int[][] exitPerlin; 
	public boolean checkpoint;

	public Block[][] map;

	public List<Entity> enemies = new CopyOnWriteArrayList<Entity>();

	public static int[][] generatePerlin() {
		double[][] PerlinSeed;
		PerlinSeed = new double[2][levelSize+2*centreRad];
		int[][] outPerlin = new int[2][levelSize+2*centreRad];

		for (int i = 0; i < levelSize+2*centreRad; i++) {
			PerlinSeed[0][i] = Math.random();
			PerlinSeed[1][i] = Math.random();
		}

		for (int x = 0; x < levelSize+2*centreRad; x++) {
			int perlinOut = 0;
			double size = perlinStartWeight/1.75;
			for (int p = levelSize+2*centreRad; p > 0; p /= 2) {
				int sample1 = (x / p) * p;
				int sample2 = (sample1 + p) % levelSize+2*centreRad;

				float blend = (x - sample1) / p;
				float sample = (float) ((1 - blend) * PerlinSeed[0][sample1] + blend * PerlinSeed[0][sample2]);
				perlinOut += sample * size;;
				size*=perlinWeightDecrement;
			}
			outPerlin[0][x] = perlinOut;

			perlinOut = 0;
			size = perlinStartWeight;
			for (int p = perlinMaxRoofOctaveSize; p > 0; p /= 2) { //levelSize+2*centreRad
				int sample1 = (x / p) * p;
				int sample2 = (sample1 + p) % levelSize+2*centreRad;

				float blend = (x - sample1) / p;
				float sample = (float) ((1 - blend) * PerlinSeed[1][sample1] + blend * PerlinSeed[1][sample2]);
				perlinOut += sample * size * perlinMult;
				size*=perlinWeightDecrement;
			}
			outPerlin[1][x] = perlinOut;
		}

		return outPerlin;
	}

	public RandomChunk(int level, int prevExit, int[][] perlin) {	
		map = new Block[levelSize][levelSize];


		entrance = prevExit - 2;
		if (entrance < 0) entrance += 4;

		List<Integer> validExits = new ArrayList<Integer>();
		for (int i = 1; i <= 3; i++) {
			validExits.add(i);
		}

		if (entrance != 0) validExits.remove(entrance-1);
		else validExits.remove(1);

		int rand = (int) (Math.random() * (validExits.size()));
		exit = validExits.get(rand);

		exitPerlin = generatePerlin();


		//fill in all
		for (int x = 0; x < levelSize; x++) {
			for (int y = 0; y < levelSize; y++) {
				map[x][y] = new StoneBlock1();
			}
		}

		int halfPerlinlength = levelSize/2+centreRad;

		//generate entrance
		if (entrance == 0) {
			for (int y = 0; y < levelSize/2; y++) {
				int x = exitPerlin[0][y+halfPerlinlength]-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y+halfPerlinlength]+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					map[x][y] = new NoBlock();
				}
			}

		}else if (entrance == 1) {
			for (int x = 0; x < halfPerlinlength; x++) {
				int y = perlin[0][x+halfPerlinlength]-perlin[1][x+halfPerlinlength]+levelSize/2;
				int end = perlin[0][x+halfPerlinlength]+levelSize/2+perlinMinY;
				
				for (; y < end; y++) {
					map[x][y] = new NoBlock();
				}
			}

		}else if (entrance == 3) {
			for (int x = levelSize/2-centreRad; x < levelSize; x++) {
				int y = perlin[0][x]-perlin[1][x]+levelSize/2;
				int end = perlin[0][x]+levelSize/2+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = new NoBlock();
				}
			}
		}



		//generate exit
		if (exit == 1) {
			for (int x = 0; x < halfPerlinlength; x++) {
				int y = exitPerlin[0][x+halfPerlinlength]-exitPerlin[1][x+halfPerlinlength]+levelSize/2;
				int end = exitPerlin[0][x+halfPerlinlength]+levelSize/2+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = new NoBlock();
				}
			}

		} else if (exit == 2) {
			boolean complete = false;
			int y = levelSize-1;
			
			for (; y > levelSize/2-centreRad/2; y--) {
				int x = exitPerlin[0][y]-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y]+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					if (!map[x][y].solid()) complete = true;
					map[x][y] = new NoBlock();
				}
				if (complete) break;
			}
			int yFinal = y-perlinMinY;
			for (; y > yFinal; y--) {
				int[] tempLine = new int[8];

				int x = exitPerlin[0][y]-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y]+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					map[x][y] = new NoBlock();
				}
			}

		} else if (exit == 3) {
			for (int x = levelSize/2-centreRad; x < levelSize; x++) {
				int y = exitPerlin[0][x]-exitPerlin[1][x]+levelSize/2;
				int end = exitPerlin[0][x]+levelSize/2+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = new NoBlock();
				}
			}
		}

		//generate circle
		for (int x = levelSize/2-centreRad; x < levelSize/2+centreRad; x++) {
			for (int y = levelSize/2-centreRad; y < levelSize/2+6; y++) {
				if (Math.pow(x-levelSize/2, 2) + Math.pow(y-levelSize/2, 2) < Math.pow(centreRad-0.5, 2)) map[x][y] = new NoBlock();
				else if (Math.pow(x-levelSize/2, 2) + Math.pow(y-levelSize/2, 2) < Math.pow(centreRad, 2) && Math.random() > 0.5) map[x][y] = new NoBlock();
			}
		}


		//create enemies
		enemies.add(new SRDamageAura(levelSize/2, levelSize/2, level));
		enemies.add(new SMSpikes(levelSize/2, levelSize/2, level));
		enemies.add(new MMDasher(levelSize/2, levelSize/2, level));
	}

	@Override
	public int getExit() {
		return exit;
	}

	@Override
	public int[][] getExitPerlin() {
		return exitPerlin;
	}

	@Override
	public int getEntrance() {
		return entrance;
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
		return enemies;
	}
}
