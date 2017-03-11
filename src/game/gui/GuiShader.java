package game.gui;

import game.Shader;

public class GuiShader extends Shader {

	private int modelMatrix;

	public GuiShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);

		bindAttribLocation(0, "position");
		bindAttribLocation(1, "texCoords");
		linkAndValidate();
		validateAllUniforms();

		modelMatrix = getUniformLocation("modelMatrix");
	}

	public int getModelMatrix() {
		return modelMatrix;
	}

}
