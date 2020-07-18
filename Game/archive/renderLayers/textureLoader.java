package renderLayers;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import blocks.StoneBlock1;
import entities.enemies.mobileMelee.MMDasher;
import entities.enemies.staticMelee.SMSpikes;
import entities.enemies.staticRanged.SRDamageAura;

public class textureLoader {
	public static void loadAllTextures() {
		StoneBlock1.load();
		
		MMDasher.load();
		
		SMSpikes.load();
		
		SRDamageAura.load();
	}
	
	
	public static int load(String fileName) {
		int textureID;
		
		try {
			BufferedImage img = ImageIO.read(Paths.get("resources/textures/" + fileName).toFile());

			int width = img.getWidth();
			int height = img.getHeight();
			
			int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer buff = BufferUtils.createByteBuffer((width*height) * 4);
			
			textureID = GL11.glGenTextures();

			for (int i = 0; i < pixels.length; i++) {
				int pixel = pixels[i];
				buff.put((byte) ((pixel >> 16) & 0xFF));     // Red component
				buff.put((byte) ((pixel >> 8) & 0xFF));      // Green component
				buff.put((byte) (pixel & 0xFF));               // Blue component
				buff.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
			}
			
			((Buffer) buff).flip();
			
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
			
			glBindTexture(GL_TEXTURE_2D, 0);

			return textureID;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}
}
