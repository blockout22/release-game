package game.debug;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class DebugRenderer {

	private int vao;
	private int vbo;
	private int vboi;
	private int indicesSize;

	private Vector3f start;
	private Vector3f end;
	
	private FloatBuffer verticesData;

	public DebugRenderer() {
		vao = GL30.glGenVertexArrays();
		vbo = GL15.glGenBuffers();
		vboi = GL15.glGenBuffers();
	}

	public void setup() {
		float[] vertices = {
				//
				-1, -1, -1, //
				1, 1, 1, //
		};

		float[] indices = {
				//
				0, 1 //
		};
		
		indicesSize = indices.length;
		GL30.glBindVertexArray(vao);

		verticesData = flip(vertices);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesData, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, flip(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private static IntBuffer flip(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	private static FloatBuffer flip(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

}
