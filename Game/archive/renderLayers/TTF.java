package renderLayers;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.lwjgl.opengl.GL11;

import blocks.Colour;
import javafx.util.Pair;
import settings.Settings;

import static org.lwjgl.opengl.GL11.*;

public class TTF {
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
		put(5, ";:?!\\|<>[]�`^~");
	}};

	public static void init() {
		largeSize = Settings.textLargeRes;
		Pair<Integer, FontMetrics> data = generateTexture(largeSize);
		largeFontMetrics = data.getValue();
		largeImage = data.getKey();
	}

	//Draws the bitmap generated by the class
	public static void drawBitmap(float size, int image) {
		glBindTexture(GL_TEXTURE_2D, image);

		glEnable(GL_BLEND);
		glBegin(GL_QUADS);

		Colour colour = Colour.WHITE;
		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		glTexCoord2f(0, 0);
		glVertex2f(0, 0);

		glTexCoord2f(1, 0);
		glVertex2f(1024, 0);

		glTexCoord2f(1, 1);
		glVertex2f(1024, 1024);

		glTexCoord2f(0, 1);
		glVertex2f(0, 1024);

		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static void drawBitmap() {
		drawBitmap(largeSize, largeImage);
	}

	public static void drawLargeText(String text, float x, float y, float textSize, Colour colour) {
		drawText(largeSize, largeImage, largeFontMetrics, text, x, y, textSize, colour);
	}

	public static void drawText(float size, int image, FontMetrics fontMetrics, String text, float startx, float y, float textSize, Colour colour) {
		float finalSize = textSize / size;

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, image);
		glEnable(GL_BLEND);
		GL11.glBegin(GL11.GL_QUADS);

		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		float x = startx;
		String[] c = text.split("\n");

		IntStream.range(0, c.length)
		.forEach(line -> {
			float charHeight = fontMetrics.getHeight();
			float charY = y + charHeight*line;
			char[] currentLine = c[line].toCharArray();

			IntStream.range(0, currentLine.length)
			.forEach(letter -> {
				char currentLetter = currentLine[letter];

				for (int index : CHARS.keySet()) {
					float p = (float) CHARS.get(index).indexOf(currentLetter);

					if (p >= 0) {						
						float charWidth = fontMetrics.charWidth('c');
						float charX = x + charWidth*finalSize*letter;

						float charX1 = size*p*0.67f/(size*18f);
						float charY1 = (((charHeight+25) * (index-1)) + fontMetrics.getDescent() + 25)/(size*18f);
						float charX2 = charX1 + charWidth/(size*18f);
						float charY2 = charY1 + charHeight/(size*18f);

						glTexCoord2f(charX1, charY1);
						glVertex2f(charX, charY);

						glTexCoord2f(charX2, charY1);
						glVertex2f(charX + charWidth*finalSize, charY);

						glTexCoord2f(charX2, charY2);
						glVertex2f(charX + charWidth*finalSize, charY + charHeight*finalSize);

						glTexCoord2f(charX1, charY2);
						glVertex2f(charX, charY + charHeight*finalSize);

						break;
					}
				}
			});

		});

		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private static Pair<Integer, FontMetrics> generateTexture(float size) {
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
			imageGraphics.setColor(Color.WHITE);
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
			
			imageData.flip();
			
			int textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);		
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int) (size*18), (int) (size*18), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			return new Pair<Integer, FontMetrics>(textureID, fontMetrics);

		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}