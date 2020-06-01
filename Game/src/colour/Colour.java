	package colour;

/**
 * represents an rgba colour
 * 
 * @author Sam
 *
 */
public class Colour{
	public float red;
	public float blue;
	public float green;
	public float alpha;
	
	/**
	 * creates rgb colour with no transparency
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Colour(float red, float green, float blue) {
		this(red, green, blue, 1f);
	}
	
	/**
	 * creates rgba colour
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public Colour(float red, float green, float blue, float alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}
	
	/**
	 * creates an rgb colour with no transparency
	 * @param hex 6 character hex rgb input
	 */
	public Colour(String hex) {
		this.red = (float)Integer.parseInt(hex.substring(0, 2), 16)/255f;
		this.green = (float)Integer.parseInt(hex.substring(2, 4), 16)/255f;
		this.blue = (float)Integer.parseInt(hex.substring(4, 6), 16)/255f;
		this.alpha = 1;
	}
	
	/**
	 * copies a colour
	 * @param colour
	 */
	public Colour(Colour colour) {
		this.red = colour.red;
		this.blue = colour.blue;
		this.green = colour.green;
		this.alpha = colour.alpha;
	}
	
	/**
	 * immutably changes the alpha
	 * @param alpha
	 * @return
	 */
	public Colour mutAlpha(float alpha) {
		return new Colour(this.red, this.green, this.blue, alpha);
	}
	
	public boolean equals(Colour colour) {
		return this.red == colour.red && this.green == colour.green && this.blue == colour.blue && this.alpha == colour.alpha;
	}
	
	public String toString() {
		return "(" + red*255 + "," + green*255 + "," + blue*255 + "," + alpha*255 + ")";
	}
}
