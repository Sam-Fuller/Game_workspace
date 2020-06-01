package chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import colour.Colour;
import entity.Entity;
import entity.enemy.staticMelee.Spikes;
import graphics.ColourMesh;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;

public class GeneratedChunk implements Chunk {
	static final int levelSize = 300;
	static final int centreRad = 50;
	static final int smallCentreRad = 10;

	static final double perlinMult = 1; 				//multiplier
	static final int perlinMinX = 30;				//minimum x size
	static final double perlinStartWeight = 10;		//start weight
	static final double perlinWeightDecrement = 0.8;	//weight decrement
	static final int perlinMaxRoofOctaveSize = 32; 	//roof max octave size
	static final int perlinOffsetX = 0;				//x offset
	static final int perlinMinY = 40;				//minimum y size

	int entrance;
	int exit;

	int[][] exitPerlin; 
	boolean checkpoint;

	public MeshBuilder meshBuilder;
	public Mesh mesh;

	boolean[][] map;

	List<Entity> entities = new CopyOnWriteArrayList<Entity>();

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

	public GeneratedChunk(int level, int prevExit, int[][] perlin) {	
		map = new boolean[levelSize][levelSize*2];


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
			for (int y = 0; y < levelSize*2; y++) {
				map[x][y] = true;
			}
		}

		int halfPerlinlength = levelSize/2+centreRad;

		//generate entrance
		if (entrance == 0) {
			for (int y = 0; y < levelSize/2; y++) {
				int x = exitPerlin[0][y+halfPerlinlength]/2-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y+halfPerlinlength]/2+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					map[x][y*2] = false;
					map[x][y*2+1] = false;
				}
			}

		}else if (entrance == 1) {
			for (int x = 0; x < halfPerlinlength; x++) {
				int y = perlin[0][x+halfPerlinlength]*2-perlin[1][x+halfPerlinlength]+levelSize;
				int end = perlin[0][x+halfPerlinlength]*2+levelSize+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = false;
				}
			}

		}else if (entrance == 3) {
			for (int x = levelSize/2-centreRad; x < levelSize; x++) {
				int y = perlin[0][x]*2-perlin[1][x]+levelSize;
				int end = perlin[0][x]*2+levelSize+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = false;
				}
			}
		}



		//generate exit
		if (exit == 1) {
			for (int x = 0; x < halfPerlinlength; x++) {
				int y = exitPerlin[0][x+halfPerlinlength]*2-exitPerlin[1][x+halfPerlinlength]+levelSize;
				int end = exitPerlin[0][x+halfPerlinlength]*2+levelSize+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = false;
				}
			}

		} else if (exit == 2) {
			boolean complete = false;
			int y = levelSize-1;

			for (; y > levelSize/2-centreRad/2; y--) {
				int x = exitPerlin[0][y]-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y]+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					if (!map[x][y*2]) complete = true;
					map[x][y*2] = false;
					map[x][y*2+1] = false;
				}
				if (complete) break;
			}
			int yFinal = y-5;
			for (; y > yFinal; y--) {
				int x = exitPerlin[0][y]-perlinMinX/2+perlinOffsetX+levelSize/2;
				int end = exitPerlin[0][y]+perlinMinX/2+perlinOffsetX+levelSize/2;

				for (; x < end; x++) {
					map[x][y*2] = false;
					map[x][y*2+1] = false;
				}
			}

		} else if (exit == 3) {
			for (int x = levelSize/2-centreRad; x < levelSize; x++) {
				int y = exitPerlin[0][x]*2-exitPerlin[1][x]+levelSize;
				int end = exitPerlin[0][x]*2+levelSize+perlinMinY;

				for (; y < end; y++) {
					map[x][y] = false;
				}
			}
		}

		//generate semi circle
		for (int x = levelSize/2-centreRad; x < levelSize/2+centreRad; x++) {
			for (int y = levelSize/2-centreRad; y < levelSize/2; y++) {
				if (Math.pow(x-levelSize/2, 2) + Math.pow(y-levelSize/2, 2) < Math.pow(centreRad-0.5, 2)) {
					map[x][y*2] = false;
				}
				if (Math.pow(x-levelSize/2, 2) + Math.pow(y-levelSize/2+1, 2) < Math.pow(centreRad-0.5, 2)) {
					map[x][y*2+1] = false;
				}
			}
		}

		//generate smaller circle
		for (int x = levelSize/2-smallCentreRad; x < levelSize/2+smallCentreRad; x++) {
			for (int y = levelSize/2-smallCentreRad; y < levelSize/2+smallCentreRad; y++) {
				if (Math.pow(x-levelSize/2, 2) + Math.pow(y-levelSize/2, 2) < Math.pow(smallCentreRad-0.5, 2)) {
					map[x][y*2] = false;
					map[x][y*2+1] = false;
				}
			}
		}

		generateMesh();

		//create enemies
		//enemies.add(new SRDamageAura(levelSize/2, levelSize/2, level));
		entities.add(new Spikes(160, 140, level));
		//enemies.add(new MMDasher(levelSize/2, levelSize/2, level));
	}

	private void generateMesh() {
		meshBuilder = new MeshBuilder();

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

				Colour caveColour = Graphics.getColourScheme().cave();

				if (x >= 0) {
					if (!map[x][y*2]) {
						meshBuilder.drawTriangle(x1, y1,
								x2, y1,
								x1o, y2,
								caveColour);
					}
				}

				if (x+gridOffset >= 0 && x+gridOffset < map.length) {
					if (!map[x+gridOffset][y*2+1]) {
						meshBuilder.drawTriangle(x2, y1,
								x1o, y2,
								x2o, y2,
								caveColour);
					}
				}
			}
		}
	}

	@Override
	public void render(int xOffset, int yOffset) {
		if (mesh == null) {
			mesh = meshBuilder.asColourMesh(false);
			meshBuilder = null;
		}

		mesh.render(xOffset, yOffset);
	}

	@Override
	public int getExit() {
		return exit;
	}

	@Override
	public int[][] getExitPerlin() {
		int[][] temp = exitPerlin;
		exitPerlin = null;
		return temp;
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
	public boolean[][] getMap() {
		return map;
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}
}