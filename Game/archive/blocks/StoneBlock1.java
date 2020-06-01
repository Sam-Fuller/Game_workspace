package blocks;

import renderLayers.textureLoader;

public class StoneBlock1 implements Block{
	static int textureID;
	
	public static void load() {
		textureID = textureLoader.load("stone/stone1.png");
	}

	@Override
	public boolean solid() {
		return true;
	}
	
	@Override
	public boolean textured() {
		return true;
	}
	
	@Override
	public boolean blend() {
		return false;
	}

	@Override
	public int getTexture() {
		return textureID;
	}

	@Override
	public Colour getColour() {
		return Colour.BLACK;
	}
}