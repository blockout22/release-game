package fx.physics.graph;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class fx3DView {

	private Box box;
	private Box ground;

	public fx3DView() {
	}

	public Parent createContent() {
		box = new Box(1, 1, 1);
		box.setMaterial(new PhongMaterial(Color.RED));
		box.setDrawMode(DrawMode.FILL);

		ground = new Box(100, 0, 1);
		ground.setTranslateY(0);
		ground.setMaterial(new PhongMaterial(Color.GREEN));
		box.setDrawMode(DrawMode.FILL);

		// Create and position camera
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1f);
		camera.setFarClip(10000f);
		camera.getTransforms().addAll(new Rotate(0, Rotate.Y_AXIS), new Rotate(0, Rotate.X_AXIS), new Rotate(180, Rotate.Z_AXIS), new Translate(0, -10, -100));

		// Build the Scene Graph
		Group root = new Group();
		root.getChildren().add(camera);
		root.getChildren().addAll(box, ground);

		// Use a SubScene
		SubScene subScene = new SubScene(root, 600, 600);
		subScene.setFill(Color.ALICEBLUE);
		subScene.setCamera(camera);
		Group group = new Group();
		group.getChildren().add(subScene);
		return group;
	}

	public Box getBox() {
		return box;
	}

}
