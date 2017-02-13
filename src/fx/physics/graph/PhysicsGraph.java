package fx.physics.graph;


import javax.vecmath.Quat4d;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import game.BulletWorld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
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
	
	private ScrollPane scroll;
	private fx3DView view;

	@Override
	public void start(Stage stage) throws Exception {
		world = new BulletWorld();
		world.createPlane();
		CollisionShape s = world.createSphere(1);
		fallBody = world.addShapeToWorld(s, new Quat4d(0, 0, 0, 1), new Vector3f(0, 50, 0), 1f);
		
		
		
		pane = new BorderPane();
		scene = new Scene(pane, 900, 600);
		linechart = new LineChart<Number, Number>(xAxis, yAxis);
		linechart.setAnimated(false);
		xAxis.setAutoRanging(false);
		xAxis.setUpperBound(350);
		series = new XYChart.Series();
		
		view = new fx3DView();
		scroll = new ScrollPane();
		scroll.setContent(view.createContent());

		linechart.getData().add(series);

		start = new Button("Start");
		start.setOnAction(e -> {
			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < 300; i++) {
						world.world.stepSimulation(1f / 60.0f, 10);
						Transform trans = new Transform();
						fallBody.getMotionState().getWorldTransform(trans);
//						System.out.println(trans.origin.y);
						float y = trans.origin.y;
						view.getBox().setTranslateY(y);
						int index = i;
						Platform.runLater(new Runnable() {
							public void run() {
								series.getData().add(new XYChart.Data(index, trans.origin.y));
							}
						});
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
		});

		pane.setTop(linechart);
		pane.setCenter(scroll);
		pane.setBottom(start);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
