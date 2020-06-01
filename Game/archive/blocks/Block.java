package blocks;

public interface Block {
	public boolean solid();
	public boolean textured();
	public boolean blend();
	public int getTexture();
	public Colour getColour();
}
