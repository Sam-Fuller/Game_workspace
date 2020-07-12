package graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;

import entity.Player;
import main.Settings;
import runnable.Graphics;
import state.GameState;

/**
 * taken from 3d game development with lwjgl
 * 
 * found at:
 * http://staff.fit.ac.cy/eng.ap/FALL2017/3d-game-development-with-lwjgl.pdf
 */
public class ColourMesh implements Mesh{
	private static Shader shaderProgram;
	
    private final int vaoId;
    private final int posVboId;
    private final int colourVboId;
    private final int idxVboId;
    private final int vertexCount;
	private final boolean transparent;
	private final boolean HUD;
	
	public static void init() {
		try {
			shaderProgram = new Shader();
			String vertex = Files.lines(Paths.get("resources/shaders/colourVertex.vs")).collect(Collectors.joining("\n"));
			shaderProgram.createVertexShader(vertex);
			String fragment = Files.lines(Paths.get("resources/shaders/colourFragment.fs")).collect(Collectors.joining("\n"));
			shaderProgram.createFragmentShader(fragment);
			shaderProgram.link();
			
			shaderProgram.createUniform("cameraPosition");
			shaderProgram.createUniform("cameraZoom");
			
			shaderProgram.createUniform("litUniform");
			shaderProgram.createUniform("lightSource");
			
			shaderProgram.createUniform("aspectRatio");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cleanUpShader() {
		shaderProgram.cleanup();
	}

    public ColourMesh(float[] positions, float[] colours, int[] indices, boolean transparent, boolean HUD) {
        FloatBuffer posBuffer = null;
        FloatBuffer colourBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
    		this.transparent = transparent;
			this.HUD = HUD;

            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

            // Colour VBO
            colourVboId = glGenBuffers();
            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
            colourBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colourVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colourBuffer != null) {
                MemoryUtil.memFree(colourBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private int getVaoId() {
        return vaoId;
    }

    private int getVertexCount() {
        return vertexCount;
    }

    public void render() {
		shaderProgram.bind();
		if (isTransparent()) glEnable(GL_BLEND);
		
		if (!HUD) {
			shaderProgram.setUniform("cameraPosition", Player.getPlayer().getScreenPosX(), Player.getPlayer().getScreenPosY());
			shaderProgram.setUniform("litUniform", 0);
		}
		else {
			shaderProgram.setUniform("cameraPosition", 0, 0);
			shaderProgram.setUniform("litUniform", 1);
		}
		shaderProgram.setUniform("aspectRatio", (float)Settings.settings.xRes.getValue()/(float)Settings.settings.yRes.getValue());
		shaderProgram.setUniform("cameraZoom", 1);
		shaderProgram.setUniform("lightSource", 0, 0, 0.5f);
		
		glBindVertexArray(getVaoId());
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
    }
    
    public void render(float x, float y) {
		shaderProgram.bind();
		if (isTransparent()) glEnable(GL_BLEND);

		shaderProgram.setUniform("cameraPosition", Player.getPlayer().getScreenPosX()+Graphics.gridToScreenXScale(-x), Player.getPlayer().getScreenPosY()-Graphics.gridToScreenYScale(-y));
		shaderProgram.setUniform("cameraZoom", 1);
		shaderProgram.setUniform("aspectRatio", (float)Settings.settings.xRes.getValue()/(float)Settings.settings.yRes.getValue());
		shaderProgram.setUniform("litUniform", 0);
		shaderProgram.setUniform("lightSource", 0, 0, 0.5f);
		
		glBindVertexArray(getVaoId());
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
    }
    
    @Deprecated
	public void render(float x, float y, float zoom) {
		shaderProgram.bind();
		if (isTransparent()) glEnable(GL_BLEND);
		
		shaderProgram.setUniform("cameraPosition", Graphics.gridToScreenXScale(x), Graphics.gridToScreenXScale(y));
		shaderProgram.setUniform("cameraZoom", zoom);
		shaderProgram.setUniform("aspectRatio", (float)Settings.settings.xRes.getValue()/(float)Settings.settings.yRes.getValue());
		shaderProgram.setUniform("litUniform", 0);
		shaderProgram.setUniform("lightSource", 0, 0, 0.5f);

		glBindVertexArray(getVaoId());
		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
	}
    
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colourVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

	public boolean isTransparent() {
		return transparent;
	}
}
