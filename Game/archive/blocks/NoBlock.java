package blocks;

public class NoBlock implements Block{
	public static Colour colour = Colour.DARKGRAY;
	
	@Override
	public boolean solid() {
		return false;
	}
	
	@Override
	public boolean textured() {
		return false;
	}
	
	@Override
	public boolean blend() {
		return false;
	}
	
	@Override
	public int getTexture() {
		return 0;
	}

	@Override
	public Colour getColour() {
		return colour;
	}

}
