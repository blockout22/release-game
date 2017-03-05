package game;

import org.lwjgl.opengl.GL11;

public class Texture {
	private int ID;
	private int width;
	private int height;

	public Texture(int iD, int width, int height) {
		ID = iD;
		this.width = width;
		this.height = height;
	}

	public int getID() {
		return ID;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void cleanUp() {
		GL11.glDeleteTextures(ID);
	}
}
