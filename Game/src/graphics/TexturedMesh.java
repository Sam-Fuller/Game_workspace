package graphics;

import org.lwjgl.system.MemoryUtil;

import entity.Player;
import runnable.Graphics;
import state.GameState;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import static org.lwjgl.opengl.GL11.*;

public class TexturedMesh implements Mesh{
	private static Shader shaderProgram;

	private final int vaoId;
    private final int posVboId;
    private final int texVboId;
    private final int idxVboId;	
    private final int vertexCount;
	private final int texture;
	private final boolean transparent;
	private final boolean HUD;
	
	public static void init() {
		try {
			shaderProgram = new Shader();
			String vertex = Files.lines(Paths.get("resources/shaders/textureVertex.vs")).collect(Collectors.joining("\n"));
			shaderProgram.createVertexShader(vertex);
			String fragment = Files.lines(Paths.get("resources/shaders/textureFragment.fs")).collect(Collectors.joining("\n"));
			shaderProgram.createFragmentShader(fragment);
			shaderProgram.link();
			
	        // Create uniforms for modelView and projection matrices and texture
	        shaderProgram.createUniform("textureSampler");
	        shaderProgram.setUniform("textureSampler", 0);
	        
	        shaderProgram.createUniform("cameraPosition");
	        shaderProgram.createUniform("cameraZoom");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void cleanUpShader() {
		shaderProgram.cleanup();
	}

	public TexturedMesh(float[] positions, float[] textCoords, int[] indices, int texture, boolean transparent, boolean HUD) {
		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			this.transparent = transparent;
			this.HUD = HUD;
			this.texture = texture;
			vertexCount = indices.length;
			//vboIdList = new ArrayList<>();

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Position VBO
			posVboId = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			((Buffer) posBuffer.put(positions)).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

			// Texture coordinates VBO
			texVboId = glGenBuffers();
			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			((Buffer) textCoordsBuffer.put(textCoords)).flip();
			glBindBuffer(GL_ARRAY_BUFFER, texVboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			// Index VBO
			idxVboId = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			((Buffer) indicesBuffer.put(indices)).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
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
		
		if (!HUD) shaderProgram.setUniform("cameraPosition", Player.getPlayer().getScreenPosX(), Player.getPlayer().getScreenPosY());
		else shaderProgram.setUniform("cameraPosition", 0, 0);
		shaderProgram.setUniform("cameraZoom", 1);
		
		// Activate firs texture bank
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, texture);

		// Draw the mesh
		glBindVertexArray(getVaoId());

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		// Restore state
		glBindVertexArray(0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
	}
	
	public void render(float x, float y) {
		shaderProgram.bind();
		if (isTransparent()) glEnable(GL_BLEND);
				
		shaderProgram.setUniform("cameraPosition",Player.getPlayer().getScreenPosX()+Graphics.gridToScreenXScale(-x), Player.getPlayer().getScreenPosY()-Graphics.gridToScreenYScale(-y));
		shaderProgram.setUniform("cameraZoom", 1);
		
		// Activate firs texture bank
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, texture);

		// Draw the mesh
		glBindVertexArray(getVaoId());

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		// Restore state
		glBindVertexArray(0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
	}
	
	@Deprecated
	public void render(float x, float y, float zoom) {
		shaderProgram.bind();
		if (isTransparent()) glEnable(GL_BLEND);
		
		shaderProgram.setUniform("cameraPosition", Graphics.gridToScreenXScale(x), Graphics.gridToScreenXScale(y));
		shaderProgram.setUniform("cameraZoom", zoom);
		
		// Activate firs texture bank
		glActiveTexture(GL_TEXTURE0);
		// Bind the texture
		glBindTexture(GL_TEXTURE_2D, texture);

		// Draw the mesh
		glBindVertexArray(getVaoId());

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		
		// Restore state
		glBindVertexArray(0);
		
		if (isTransparent()) glDisable(GL_BLEND);
		shaderProgram.unbind();
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(texVboId);
        glDeleteBuffers(idxVboId);

		// Delete the texture
		glDeleteTextures(texture);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

	public boolean isTransparent() {
		return transparent;
	}
}
