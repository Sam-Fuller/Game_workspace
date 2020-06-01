package blocks;

public class Colour {
	public float red;
	public float blue;
	public float green;
	public float alpha;
	
	public Colour(float red, float green, float blue) {
		this(red, green, blue, 1f);
	}
	
	public Colour(float red, float green, float blue, float alpha) {
		this.red = red;
		this.blue = blue;
		this.green = green;
		this.alpha = alpha;
	}
	
	public Colour(Colour colour) {
		this.red = colour.red;
		this.blue = colour.blue;
		this.green = colour.green;
		this.alpha = colour.alpha;
	}

	public static final Colour DARKRED = new Colour(0.5f, 0, 0);
	public static final Colour RED = new Colour(1, 0, 0);
	
	public static final Colour DARKYELLOW = new Colour(0.5f, 0.5f, 0);
	public static final Colour YELLOW = new Colour(1, 1, 0);
	
	public static final Colour GREEN = new Colour(0, 1, 0);
	
	public static final Colour BLUE = new Colour(0, 0, 1);

	
	public static final Colour BLACK = new Colour(0, 0, 0);
	public static final Colour DARKGRAY = new Colour(0.25f, 0.25f, 0.25f);
	public static final Colour GRAY = new Colour(0.5f, 0.5f, 0.5f);
	public static final Colour LIGHTGRAY = new Colour(0.75f, 0.75f, 0.75f);
	public static final Colour WHITE = new Colour(1, 1, 1);

	
	public static final Colour LIGHTBLUE = new Colour(0.2f, 0.5f, 0.5f, 1f);
	public static final Colour PLIGHTBLUE = new Colour(0.2f, 0.5f, 0.5f, 0.5f);
	public static final Colour TLIGHTBLUE = new Colour(0.2f, 0.5f, 0.5f, 0);
}
