package fx.physics.graph;


import java.util.ArrayList;

import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import game.BulletWorld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.stage.Stage;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class PhysicsGraph extends Application {

	private BorderPane pane;
	private Scene scene;

	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	private LineChart<Number, Number> linechart;
	private XYChart.Series series;
	private Button start;
	
	private BulletWorld world;
	private CollisionShape shape;
	private RigidBody fallBody;
	private RigidBody fallBody2;
	
	private ScrollPane scroll;
	private fx3DView view;
	private Parent content;
	
	private ArrayList<CollisionList> items = new ArrayList<CollisionList>();

	@Override
	public void start(Stage stage) throws Exception {
		view = new fx3DView();
		content = view.createContent();
		world = new BulletWorld();
		world.createPlane();
		CollisionShape s = world.createSphere(1);
		fallBody = world.addShapeToWorld(s, new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1f, true);
		fallBody2 = world.addShapeToWorld(s, new Quat4f(0, 0, 0, 1), new Vector3f(0, 70, 0), 1f, true);
		
//		Box box = new Box(2, 2, 2);
//		box.setMaterial(new PhongMaterial(Color.RED));
//		view.add(box);
//		items.add(new CollisionList(fallBody, box));
//		
//		Box box2 = new Box(2, 2, 2);
//		box2.setMaterial(new PhongMaterial(Color.AQUA));
//		view.add(box2);
//		items.add(new CollisionList(fallBody2, box2));
		
		int startSize = 6;
		int size = 12;
		for(int x = startSize; x < size; x++){
			for(int y = startSize; y < size; y++){
				for(int z = startSize; z < size; z++){
					Box b = new Box(2, 2, 2);
					b.setMaterial(new PhongMaterial(new Color(Math.random(), Math.random(), Math.random(), 1)));
					view.add(b);
					RigidBody body = world.addShapeToWorld(s, new Quat4f(0, 0, 0, 1), new Vector3f(x * 2.5f + 0.1f, y * 2.5f, z * 2.5f), 1f, true);
					items.add(new CollisionList(body, b));
				}
			}
		}
		
		pane = new BorderPane();
		scene = new Scene(pane, 900, 600);
		linechart = new LineChart<Number, Number>(xAxis, yAxis);
		linechart.setAnimated(false);
		xAxis.setAutoRanging(false);
		xAxis.setUpperBound(350);
		series = new XYChart.Series();
		
		scroll = new ScrollPane();
		scroll.setContent(content);

		linechart.getData().add(series);

		start = new Button("Start");
		start.setOnAction(e -> {
			simulate(stage);
		});

//		pane.setTop(linechart);
		pane.setCenter(scroll);
		pane.setBottom(start);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}
	
	public void simulate(Stage stage)
	{
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < 700; i++) {
					world.world.stepSimulation(1f / 60.0f, 10);
					Transform trans = null;
//					fallBody.getMotionState().getWorldTransform(trans);
//					System.out.println(trans.origin.y);
//					float y = trans.origin.y;
					
					
					for(int j = 0; j < items.size(); j++){
						trans = new Transform();
						Quat4f rotation = new Quat4f();
						items.get(j).getBody().getMotionState().getWorldTransform(trans);
						trans.getRotation(rotation);
						items.get(j).getShape().setTranslateX(trans.origin.x);
						items.get(j).getShape().setTranslateY(trans.origin.y);
						items.get(j).getShape().setTranslateZ(trans.origin.z);
						items.get(j).getShape().setRotationAxis(new Point3D(rotation.x, rotation.y, rotation.z));
					}
					
					
					
//					view.getBox().setTranslateY(y);
//					int index = i;
//					Platform.runLater(new Runnable() {
//						public void run() {
//							series.getData().add(new XYChart.Data(index, trans.origin.y));
//						}
//					});
					try {
						Thread.sleep(55);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!stage.isShowing())
					{
						break;
					}
				}
			}
		}).start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public class CollisionList{
		RigidBody body;
		Shape3D shape;
		public CollisionList(RigidBody body, Shape3D shape) {
			this.body = body;
			this.shape = shape;
		}
		public RigidBody getBody() {
			return body;
		}
		public Shape3D getShape() {
			return shape;
		}
		
		
	}

}
