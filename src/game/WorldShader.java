package game;

import game.Camera;
import game.Matrix4;
import game.Shader;

public class WorldShader extends Shader {
	
	private int viewMatrix, modelMatrix, projectionMatrix;

	public WorldShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);

		bindAttribLocation(0, "position");
		linkAndValidate();

		modelMatrix = getUniformLocation("modelMatrix");
		projectionMatrix = getUniformLocation("projectionMatrix");
		viewMatrix = getUniformLocation("viewMatrix");
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4 matrix = createViewMatrix(camera);
		loadMatrix(viewMatrix, matrix);
	}

	public int getViewMatrix() {
		return viewMatrix;
	}

	public int getModelMatrix() {
		return modelMatrix;
	}

	public int getProjectionMatrix() {
		return projectionMatrix;
	}

}
