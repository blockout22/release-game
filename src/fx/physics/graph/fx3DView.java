package fx.physics.graph;

import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class fx3DView {

	private Box ground;
	private Group root;

	public fx3DView() {
	}

	public Parent createContent() {
		ground = new Box(100, 0, 1);
		ground.setTranslateY(0);
		ground.setMaterial(new PhongMaterial(Color.GREEN));

		// Create and position camera
		PerspectiveCamera camera = new PerspectiveCamera(true);
//		camera.setDepthTest(true);
		System.out.println(camera.getDepthTest());
		camera.setDepthTest(DepthTest.ENABLE);
		System.out.println(camera.getDepthTest());
		camera.setNearClip(0.1f);
		camera.setFarClip(10000f);
		camera.getTransforms().addAll(new Rotate(0, Rotate.Y_AXIS), new Rotate(0, Rotate.X_AXIS), new Rotate(180, Rotate.Z_AXIS), new Translate(0, -10, -100));
		camera.setTranslateX(15);

		// Build the Scene Graph
		root = new Group();
		root.getChildren().add(camera);
		root.getChildren().addAll(ground);

		// Use a SubScene
		SubScene subScene = new SubScene(root, 900, 900);
		subScene.setFill(Color.ALICEBLUE);
		subScene.setCamera(camera);
		Group group = new Group();
		group.getChildren().add(subScene);
		return group;
	}

	public void add(Shape3D shape){
		root.getChildren().add(shape);
	}

}
