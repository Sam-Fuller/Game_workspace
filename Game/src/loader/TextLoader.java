package loader;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import main.Settings;
import runnable.Graphics;

class textureInfo {
	Integer image;
	FontMetrics metrics;
	
	public textureInfo(Integer image, FontMetrics metrics) {
		super();
		this.image = image;
		this.metrics = metrics;
	}
}

public class TextLoader {
	private static final String FontPath = "resources/fonts/telegrama/telegrama_render.otf";
	
	private static float largeSize;

	private static FontMetrics largeFontMetrics;
	private static int largeImage = 0;

	@SuppressWarnings("serial")
	private static final Map<Integer, String> CHARS = new HashMap<Integer, String>() {{
		put(1, " 0123456789");
		put(2, "abcdefghijklmnopqrstuvwxyz");
		put(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		put(4, "$+-*/=%\"'#@&_(),.");
		put(5, ";:?!\\|<>[]§`^~");
	}};

	public static void init() {
		largeSize = Settings.settings.textRes.getValue();
		textureInfo data = generateTexture(largeSize);
		largeFontMetrics = data.metrics;
		largeImage = data.image;
	}
	
	public static int getLargeTextImage() {
		return largeImage;
	}

	public static Mesh drawLargeText(String text, float x, float y, float textSize) {
		return drawText(largeSize, largeFontMetrics, text, x, y, textSize, new MeshBuilder()).asTexturedMesh(largeImage, true, false);
	}
	public static Mesh drawLargeText(String text, float x, float y, float textSize, Colour colour, boolean HUD) {
		return drawText(largeSize, largeFontMetrics, text, x, y, textSize, new MeshBuilder()).asTexturedMesh(largeImage, true, HUD);
	}
	
	public static void addLargeText(String text, float x, float y, float textSize, MeshBuilder meshBuilder) {
		drawText(largeSize, largeFontMetrics, text, x, y, textSize, meshBuilder);
	}
	
	private static MeshBuilder drawText(float size, FontMetrics fontMetrics, String text, float startx, float starty, float textSize, MeshBuilder meshBuilder) {
		startx = Graphics.screenToResX(startx);
		starty = Graphics.screenToResY(starty);
		
		float finalSize = Settings.settings.yRes.getValue()*textSize / 2 / size;
		
		float x = startx;
		float y = starty;
		String[] c = text.split("\n");

		IntStream.range(0, c.length)
		.forEach(line -> {
			float charHeight = fontMetrics.getHeight();
			float charY = -(textSize*line)*charHeight*5 - y;
			char[] currentLine = c[line].toCharArray();

			IntStream.range(0, currentLine.length)
			.sequential()
			.forEach(letter -> {
				char currentLetter = currentLine[letter];

				for (int index : CHARS.keySet()) {
					float p = (float) CHARS.get(index).indexOf(currentLetter);

					if (p >= 0) {						
						float charWidth = fontMetrics.charWidth('c');
						float charX = x + charWidth*finalSize*letter;

						float texX1 = size*p*0.67f/(size*18f);
						float texY1 = (((charHeight+25) * (index-1)) + fontMetrics.getDescent() + 25)/(size*18f);
						float texX2 = texX1 + charWidth/(size*18f);
						float texY2 = texY1 + charHeight/(size*18f);
						
						float screenX1 = Graphics.resToScreenX(charX);
						float screenY1 = Graphics.resToScreenY(charY)+2;
						float screenX2 = Graphics.resToScreenX(charX + charWidth*finalSize);
						float screenY2 = Graphics.resToScreenY(charY - charHeight*finalSize)+2;
												
						//first triangle
						meshBuilder.pushTexture(texX1, texY1);
						meshBuilder.pushPosition(screenX1, screenY1);
						meshBuilder.pushIndex();
						
						meshBuilder.pushTexture(texX2, texY1);
						meshBuilder.pushPosition(screenX2, screenY1);
						int a = meshBuilder.pushIndex();
						
						meshBuilder.pushTexture(texX1, texY2);
						meshBuilder.pushPosition(screenX1, screenY2);
						int b = meshBuilder.pushIndex();
						
						//second triangle
						meshBuilder.pushIndex(a);
						meshBuilder.pushIndex(b);
						
						meshBuilder.pushTexture(texX2, texY2);
						meshBuilder.pushPosition(screenX2, screenY2);
						meshBuilder.pushIndex();
						
						break;
					}
				}
			});

		});
		
		return meshBuilder;
	}

	private static textureInfo generateTexture(float size) {
		try {
			Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(FontPath)).deriveFont(size);


			ByteBuffer imageData;
			Graphics2D imageGraphics;

			//Configure
			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
			graphics.setFont(font);

			//Create the image buffer
			FontMetrics fontMetrics = graphics.getFontMetrics();
			BufferedImage imageBuffer = graphics.getDeviceConfiguration().createCompatibleImage((int) (size*18), (int) (size*18), Transparency.TRANSLUCENT);
			
			//Draw the characters on our image
			imageGraphics = (Graphics2D) imageBuffer.getGraphics();
			imageGraphics.setFont(font);
			imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			// draw every CHAR by line...
			imageGraphics.setColor(new Color(Graphics.getColourScheme().menuText().red, Graphics.getColourScheme().menuText().green, Graphics.getColourScheme().menuText().blue));
			CHARS.keySet().stream().forEach(line -> {
				char[] c = CHARS.get(line).toCharArray();
				for (int i = 0; i < c.length; i++) {
					imageGraphics.drawString(""+c[i], size*i*0.67f, (fontMetrics.getHeight()+25) * line);
				}
			});

			//Generate texture data
			int[] pixels = new int[imageBuffer.getWidth() * imageBuffer.getHeight()];
			imageBuffer.getRGB(0, 0, imageBuffer.getWidth(), imageBuffer.getHeight(), pixels, 0, imageBuffer.getWidth());
			imageData = ByteBuffer.allocateDirect((imageBuffer.getWidth() * imageBuffer.getHeight() * 4));

			for (int y = 0; y < imageBuffer.getHeight(); y++) {
				for (int x = 0; x < imageBuffer.getWidth(); x++) {
					int pixel = pixels[y * imageBuffer.getWidth() + x];
					imageData.put((byte) ((pixel >> 16) & 0xFF));     // Red component
					imageData.put((byte) ((pixel >> 8) & 0xFF));      // Green component
					imageData.put((byte) (pixel & 0xFF));               // Blue component
					imageData.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
				}
			}
			

			((Buffer) imageData).flip();
			
			int textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);		
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int) (size*18), (int) (size*18), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			return new textureInfo(textureID, fontMetrics);

		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
