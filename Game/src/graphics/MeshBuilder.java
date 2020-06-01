package graphics;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import colour.Colour;
import loader.TextLoader;
import main.Settings;
import runnable.Graphics;

class Node {
	float x;
	float y;
	Colour colour;

	public Node(float x, float y, Colour colour) {
		this.x = x;
		this.y = y;
		this.colour = colour;
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public Colour getColour() {
		return colour;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) return false;

		Node n = (Node) o;
		if (x==n.getX() && y==n.getY() && colour.equals(n.getColour())) return true;

		return false;
	}

	public int hashCode(){
		return (int) (x*1000+y);
	}
}

/**
 * helps with mesh building of unknown length meshes
 * 
 * @author Sam
 *
 */
public class MeshBuilder {
	private HashMap<Node, Integer> points = new HashMap<Node, Integer>();

	private ArrayList<Float> positions = new ArrayList<Float>();
	private ArrayList<Integer> indices = new ArrayList<Integer>();

	private ArrayList<Colour> colours = new ArrayList<Colour>();
	private ArrayList<Float> texture = new ArrayList<Float>();

	/**
	 * adds position coordinate to the mesh
	 * @param x
	 * @param y
	 */
	@Deprecated
	public void pushPosition(float x, float y) {
		positions.add(x);
		positions.add(y);
	}

	/**
	 * adds index of last position to the mesh
	 */
	@Deprecated
	public int pushIndex() {
		int i = (positions.size()-2)/2;
		indices.add(i);
		return i;
	}

	/**
	 * adds position index to the mesh
	 * @param index
	 */
	@Deprecated
	public void pushIndex(int index) {
		indices.add(index);
	}

	/**
	 * adds colour and position to the mesh
	 * @param colour
	 */
	@Deprecated
	public void pushColour(float x, float y, Colour colour) {
		positions.add(x);
		positions.add(y);
		colours.add(colour);
	}
	/**
	 * adds texture coordinate to the mesh
	 * @param x
	 * @param y
	 */
	@Deprecated
	public void pushTexture(float x, float y) {
		texture.add(x);
		texture.add(y);
	}
	/**
	 * adds colour to the mesh
	 * @param colour
	 */
	@Deprecated
	public void pushColour(Colour colour) {
		colours.add(colour);
	}

	/**
	 * pushes coordinates if they do not exist
	 * @param x
	 * @param y
	 */
	@Deprecated
	public int pushPoint(float x, float y, Colour colour) {
		Node node = new Node(x, y, colour);
		if (points.containsKey(node)) {
			int i = points.get(node);
			pushIndex(i);
			return i;
		}

		pushColour(x, y, colour);
		points.put(node, (positions.size()-2)/2);
		return pushIndex();
	}	

	public void drawTriangle(float ax, float ay, Colour ac, float bx, float by, Colour bc, float cx, float cy, Colour cc) {
		pushPoint(Graphics.gridToScreenX(ax), Graphics.gridToScreenY(ay), ac);
		pushPoint(Graphics.gridToScreenX(bx), Graphics.gridToScreenY(by), bc);
		pushPoint(Graphics.gridToScreenX(cx), Graphics.gridToScreenY(cy), cc);
	}

	public void drawTriangle(float ax, float ay, float bx, float by, float cx, float cy, Colour colour) {
		drawTriangle(ax, ay, colour, bx, by, colour, cx, cy, colour);
	}

	public void drawTriangle(float x, float y, float width, float height, float angle, Colour colourTop, Colour colourBottom) {
		float heightVectorX = height/2 * (float)Math.sin(angle);
		float heightVectorY = height/2 * (float)Math.cos(angle);
		
		float widthVectorX = width/2 * (float)Math.cos(angle);
		float widthVectorY = width/2 * (float)Math.sin(angle);
		
		drawTriangle(x + heightVectorX, y + heightVectorY, colourTop,
				x - heightVectorX + widthVectorX, y - heightVectorY + widthVectorY, colourBottom,
				x - heightVectorX - widthVectorX, y - heightVectorY - widthVectorY, colourBottom);
	}

	/**
	 * draws a sector of a circle
	 * @param x x position of the centre of the circle
	 * @param y y position of the centre of the circle
	 * @param r1 inner radius of the circle
	 * @param r2 outer radius of the circle
	 * @param th1 start angle of the sector
	 * @param th2 end angle of the sector
	 * @param colourIn colour of the inside of the circle
	 * @param colourOut colour of the outside of the circle
	 */
	private void drawSector(float x, float y, float r1, float r2, float th1, float th2, Colour colourIn, Colour colourOut) {
		float sinTh1 = (float) Math.sin(th1);
		float sinTh2 = (float) Math.sin(th2);
		float cosTh1 = (float) Math.cos(th1);
		float cosTh2 = (float) Math.cos(th2);

		//1st triangle
		pushPoint(Graphics.gridToScreenX(x+cosTh2*r1), Graphics.gridToScreenY(y+sinTh2*r1), colourIn);
		int a = pushPoint(Graphics.gridToScreenX(x+cosTh1*r1), Graphics.gridToScreenY(y+sinTh1*r1), colourIn);
		int b = pushPoint(Graphics.gridToScreenX(x+cosTh2*r2), Graphics.gridToScreenY(y+sinTh2*r2), colourOut);

		//2nd triangle
		pushIndex(a);
		pushIndex(b);
		pushPoint(Graphics.gridToScreenX(x+cosTh1*r2), Graphics.gridToScreenY(y+sinTh1*r2), colourOut);
	}

	/**
	 * draw an arc made of sectors with a colour fade across the radius
	 * @param x x position of the centre of the circle
	 * @param y y position of the centre of the circle
	 * @param r1 inner radius of the circle
	 * @param r2 outer radius of the circle
	 * @param th1 start angle of the arc
	 * @param th2 end angle of the arc
	 * @param slices number of sectors in the circle (poly count will be 2 * slices * Settings.segmentMultiplier)
	 * @param colourIn the colour of the inside of the circle
	 * @param colourOut the colour of the outside of the circle
	 */
	public void drawSectorArc(float x, float y, float r1, float r2, float th1, float th2, int slices, Colour colourIn, Colour colourOut) {		
		float maxAngle = th2 - th1;
		float anglePerSlice = maxAngle/slices;

		for(int i = 0; i < slices; th1 += anglePerSlice, i++){
			drawSector(x, y, r1, r2, th1, th1 + anglePerSlice, colourIn, colourOut);
		}
	}
	/**
	 * draw an arc made of sectors with a solid colour
	 * @param x x position of the centre of the circle
	 * @param y y position of the centre of the circle
	 * @param r1 inner radius of the circle
	 * @param r2 outer radius of the circle
	 * @param th1 start angle of the arc
	 * @param th2 end angle of the arc
	 * @param slices number of sectors in the circle (poly count will be 2 * slices * Settings.segmentMultiplier)
	 * @param colour the colour of the circle
	 */
	public void drawSectorArc(float x, float y, float r1, float r2, float th1, float th2, int slices, Colour colour) {
		drawSectorArc(x, y, r1, r2, th1, th2, slices, colour, colour);
	}

	/**
	 * draw a full circle with a colour fade across the radius
	 * @param x x position of the centre of the circle
	 * @param y y position of the centre of the circle
	 * @param r1 inner radius of the circle
	 * @param r2 outer radius of the circle
	 * @param thOffset the offset of the start of the segments
	 * @param slices number of sectors in the circle (poly count will be 2 * slices * Settings.segmentMultiplier)
	 * @param colourIn the colour of the inside of the circle
	 * @param colourOut the colour of the outside of the circle
	 */
	public void drawSectorCircle(float x, float y, float r1, float r2, float thOffset, int slices, Colour colourIn, Colour colourOut) {
		drawSectorArc(x, y, r1, r2, thOffset, (float)(2*Math.PI) + thOffset, slices, colourIn, colourOut);
	}
	/**
	 * draw a full circle with a solid colour
	 * @param x x position of the centre of the circle
	 * @param y y position of the centre of the circle
	 * @param r1 inner radius of the circle
	 * @param r2 outer radius of the circle
	 * @param thOffset the offset of the start of the segments
	 * @param slices number of sectors in the circle (poly count will be 2 * slices * Settings.segmentMultiplier)
	 * @param colour the colour of the circle
	 */
	public void drawSectorCircle(float x, float y, float r1, float r2, float thOffset, int slices, Colour colour) {
		drawSectorCircle(x, y, r1, r2, thOffset, slices, colour, colour);
	}

	/**
	 * draw text within the mesh, must be a textured mesh
	 * @param x x position of text
	 * @param y y position of text
	 * @param text the text to be rendered
	 * @param size size of the text
	 * @param colour colour of the text
	 */
	public void drawText(String text, float x, float y, float size) {
		TextLoader.addLargeText(text, x, y, size, this);
	}
	
	
	
	
	
	/**
	 * returns the mesh assuming colours have been input
	 * @param transparent
	 * @return
	 */
	public ColourMesh asColourMesh(boolean transparent) {
		return asColourMesh(transparent, false);
	}

	/**
	 * returns the mesh assuming textures have been input
	 * @param textureID
	 * @param transparent
	 * @return
	 */
	public TexturedMesh asTexturedMesh(int textureID, boolean transparent) {
		return asTexturedMesh(textureID, transparent, false);
	}

	/**
	 * returns the mesh assuming colours have been input
	 * @param transparent
	 * @param HUD
	 * @return
	 */
	public ColourMesh asColourMesh(boolean transparent, boolean HUD) {
		float[] positions = new float[this.positions.size()];
		for (int i = 0; i < positions.length; i++) positions[i] = this.positions.get(i);

		float[] colours = new float[this.colours.size()*4];
		for (int i = 0; i < colours.length; i+=4) {
			colours[i] = this.colours.get(i/4).red;
			colours[i+1] = this.colours.get(i/4).green;
			colours[i+2] = this.colours.get(i/4).blue;
			colours[i+3] = this.colours.get(i/4).alpha;
		}

		int[] indices = new int[this.indices.size()];
		for (int i = 0; i < indices.length; i++) indices[i] = this.indices.get(i);

		return new ColourMesh(positions, colours, indices, transparent, HUD);
	}

	/**
	 * returns the mesh assuming textures have been input
	 * @param textureID
	 * @param transparent
	 * @param HUD
	 * @return
	 */
	public TexturedMesh asTexturedMesh(int textureID, boolean transparent, boolean HUD) {
		float[] positions = new float[this.positions.size()];
		for (int i = 0; i < positions.length; i++) positions[i] = this.positions.get(i);

		float[] texture = new float[this.texture.size()];
		for (int i = 0; i < texture.length; i++) texture[i] = this.texture.get(i);

		int[] indices = new int[this.indices.size()];
		for (int i = 0; i < indices.length; i++) indices[i] = this.indices.get(i);

		return new TexturedMesh(positions, texture, indices, textureID, transparent, HUD);
	}
}
