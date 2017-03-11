package game.gui;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import game.Matrix4;
import game.Shader;

public class GuiRender {

	private int vao;
	private int vbo;
	private int vboTexture;
	private int vboi;

	private int indicesSize;

	public GuiRender() {
		vao = GL30.glGenVertexArrays();
		vbo = GL15.glGenBuffers();
		vboTexture = GL15.glGenBuffers();
		vboi = GL15.glGenBuffers();
	}

	public void add(float[] vertices, float[] texCoords, int[] indices) {
		indicesSize = indices.length;

		GL30.glBindVertexArray(vao);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, flip(vertices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexture);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, flip(texCoords), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, flip(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void enable() {
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
	}

	public void render(Shader shader, int modelMatrix, Gui node) {
		node.update();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, node.getTexture());
		Matrix4 transformMatrix = createTransformMatrix(node.getPosition(), node.getRotation(), node.getScale());
		shader.loadMatrix(modelMatrix, transformMatrix);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
	}

	public void disable() {

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	public void cleanup() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);

		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(vboi);
		// GL15.glDeleteBuffers(vboNormal);
		GL15.glDeleteBuffers(vboTexture);
	}

	private Matrix4 createTransformMatrix(Vector2f position, float rotation, Vector2f scale) {
		Matrix4 matrix = new Matrix4();
		matrix.setIdentity();
		Matrix4.translate(position, matrix, matrix);
		Matrix4.rotate(((float) Math.toRadians(0)), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4.rotate(((float) Math.toRadians(0)), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4.scale(new Vector3f(scale.x, scale.y, 0), matrix, matrix);
		return matrix;
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
