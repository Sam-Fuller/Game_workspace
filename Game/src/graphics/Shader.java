package graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;

/**
 * taken from 3d game development with lwjgl
 * 
 * found at:
 * http://staff.fit.ac.cy/eng.ap/FALL2017/3d-game-development-with-lwjgl.pdf
 */
public class Shader {
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	private final Map<String, Integer> uniforms;

	public Shader() {
		programId = glCreateProgram();
		if (programId == 0) {
			new Exception("Could not create Shader").printStackTrace();;
		}
		uniforms = new HashMap<>();
	}

	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Type: " + shaderType);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);
		
		return shaderId;
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
		}

		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.out.println("Warning validating Shader code: " + glGetShaderInfoLog(programId, 1024));
		}
	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}

	public void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		if (uniformLocation < 0) {
			new Exception("Could not find uniform:" + uniformName).printStackTrace();
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(uniforms.get(uniformName), false,
					value.get(stack.mallocFloat(16)));
		}
	}
	
	public void setUniform(String uniformName, float x, float y, float z) {
		glUniform3f(uniforms.get(uniformName), x, y, z);
	}
	
	public void setUniform(String uniformName, float x, float y) {
		glUniform2f(uniforms.get(uniformName), x, y);
	}

	public void setUniform(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}
}
