package game;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.CollisionWorld.LocalRayResult;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import game.debug.Debug;
import game.gui.Gui;
import game.gui.GuiRender;
import game.gui.GuiShader;
import game.gui.Quad;

public class ReleaseGame {

	private String TITLE = "Game ";

	private Camera camera;
	private WorldShader shader;
	private Mesh mesh;
	private MeshObject object;
	private Texture texture;

	// GUI
	private GuiRender guiRender;
	private GuiShader guiShader;
	private Gui crossHair;

	// physics
	private BulletWorld world;
	private ArrayList<PhysicsObject> bulletObjects = new ArrayList<PhysicsObject>();
	private PhysicsObject player;

	// DEBUG
	public Debug debug;
	private Mesh debugMesh;
	private MeshObject debugObject;

	public ReleaseGame() {
		init();
		initGui();
		loop();
		close();
	}

	private void init() {
		Window.createWindow();
		Window.enableDepthBuffer();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		camera = new Camera(70f, 0.1f, 10000f);
		// camera.setPosition(new Vector3f(10f, 15f, 50f));
		shader = new WorldShader("vertexShader.glsl", "fragmentShader.glsl");

		shader.bind();
		shader.loadMatrix(shader.getProjectionMatrix(), camera.getProjectionMatrix());
		shader.unbind();

		mesh = OBJLoader.load("box.obj");
		// mesh.add(getVertices(), getTexCoords(), getIndices());
		object = new MyObject(mesh, new Vector3f(0f, 0f, 100f), new Vector3f(0f, 0f, 0f), 1f) {
		};

		texture = TextureLoader.loadTexture("box.jpg");
		object.setTexture(texture);

		// initPhysics();
		world = new BulletWorld();
		world.createPlane();
		// BoxShape box = world.createBox(new Vector3f(1f, 1f, 1f));
		ConvexHullShape box = world.createConvexHull(mesh.getPoints());
		// SphereShape box = world.createSphere(1f);

		int startSize = 6;
		int size = 16;

		debug = new Debug();
		world.world.setDebugDrawer(debug);

		RigidBody playerBody = world.addShapeToWorld(box, new Quat4f(0, 0, 0, 1), camera.getPosition(), 100f, false);
		player = new PhysicsObject(-1, playerBody, object);
		for (int x = startSize; x < size; x++) {
			for (int y = startSize; y < size; y++) {
				for (int z = startSize; z < size; z++) {
					MeshObject object = new MyObject(mesh, new Vector3f(x + 800.5f + 0.1f, y + 400.5f, z + 400.5f), new Vector3f(0f, 0f, 0f), 1f);
					object.setTexture(texture);

					// object.setRotation(new
					// Vector3f(Math.sin(Time.getDelta()), 0, 0));

					Random r = new Random();
					float mass = 1f;
					if (r.nextInt(4) == 0) {
						mass = 0f;
					}
					// int rotation = r.nextInt(360);
					// object.setRotation(new Vector3f(0, rotation, rotation));

					RigidBody body = world.addShapeToWorld(box, new Quat4f(0, 0, 0, 1), new Vector3f(x * 2.5f + 0.1f, y * 2.5f, z * 2.5f), mass, true);
					bulletObjects.add(new PhysicsObject(x + y + z, body, object));

					// Transform transform = new Transform();
					// body.getMotionState().getWorldTransform(transform);
					// Quat4f rot = new Quat4f();
					// body.setWorldTransform(transform);
					// transform.getRotation(rot);

				}
			}

		}
		world.world.debugDrawWorld();

		MeshObject o = new MeshObject(mesh, new Vector3f(-150, 150, -150), new Vector3f(0f, 0f, 0f), 1f) {
			public void update() {
			}

			@Override
			public void interact() {
				System.out.println("This is my custom Object");
				System.out.println(GL11.glGetString(GL11.GL_VERSION));
			}

		};
		RigidBody body = world.addShapeToWorld(box, new Quat4f(0, 0, 0, 1), new Vector3f(-50, 50, -50), 1f, true);
		bulletObjects.add(new PhysicsObject(50398, body, o));

		debugMesh = new Mesh();
		debugMesh.setupLine();
		debugObject = new MeshObject(debugMesh, new Vector3f(0f, 25f, 0f), new Vector3f(0f, 0f, 0f), 1f) {
			public void update() {
			}

			@Override
			public void interact() {
			}
		};
	}

	private void initGui() {
		guiRender = new GuiRender();
		guiRender.add(Quad.getVertices(), Quad.getTexCoords(), Quad.getIndices());

		guiShader = new GuiShader("guiVertexShader.glsl", "guiFragmentShader.glsl");

		crossHair = new Gui(new Vector2f(0, 0), 0, new Vector2f(0.01f, 0.01f)) {
			public void update() {
			}
		};
	}

	private void loop() {
		while (!Window.isCloseRequested()) {
			render3D();
			render2D();

			rayCast();

			input();
			// Window.sync(60);
			// camera.setPosition(player.getObject().getPosition());

			world.world.stepSimulation(0.005f);
			world.world.debugDrawWorld();
			camera.update();
			Input.update(Window.window);

			// Window.setTitle(TITLE + " [FPS: " + Window.getFPS() + "]");
			Window.update();
		}
	}

	private void render3D() {
		shader.bind();
		{
			shader.loadViewMatrix(camera);
			mesh.enable();
			{
				Transform transform = new Transform();
				for (int i = 0; i < bulletObjects.size(); i++) {
					Quat4f rotation = new Quat4f();
					float[] m = new float[4 * 4];
					bulletObjects.get(i).getBody().getMotionState().getWorldTransform(transform).getOpenGLMatrix(m);
					transform.getRotation(rotation);

					// bulletObjects.get(i).getObject().setPosition(transform.origin);
					// bulletObjects.get(i).getObject().setRotation(toEulerianAngle(rotation));

					mesh.render(shader, shader.getModelMatrix(), bulletObjects.get(i).getObject(), camera, m);
				}

				mesh.render(shader, shader.getModelMatrix(), player.getObject(), camera);
				player.getBody().getMotionState().getWorldTransform(transform);
				player.getObject().setPosition(transform.origin);
			}
			mesh.disable();

			debugMesh.enable();
			{
				debugMesh.drawLine(shader, shader.getModelMatrix(), debugObject, new Vector3f(10, yLoc, 10), new Vector3f(-10, -10, -10));
			}
			debugMesh.disable();
		}
		shader.unbind();
	}

	private void render2D() {
		guiShader.bind();
		{
			guiRender.enable();
			{
				guiRender.render(guiShader, guiShader.getModelMatrix(), crossHair);
			}
			guiRender.disable();
		}
		guiShader.unbind();
	}

	private void close() {

		guiShader.cleanup();
		guiRender.cleanup();
		debugMesh.cleanUp();
		texture.cleanUp();
		mesh.cleanUp();
		shader.cleanup();
		world.cleanup();
		Window.close();
	}

	private void rayCast() {
		if (world.world != null) {
			Vector3f start = new Vector3f(camera.getPosition());
			Vector3f res = new Vector3f(camera.getPosition());
			Vector3f dir = new Vector3f(camera.rayForward(100));
			res.add(dir);
			Vector3f end = new Vector3f(res);
			// System.out.println(start + " : " + end);
			CollisionWorld.ClosestRayResultCallback rayCallback = new CollisionWorld.ClosestRayResultCallback(start, end);
			world.world.rayTest(start, end, rayCallback);
			if (rayCallback.hasHit()) {
				RigidBody body = RigidBody.upcast(rayCallback.collisionObject);
				if (body != null) {
					PhysicsObject ob = null;
					boolean found = false;
					for (int i = 0; i < bulletObjects.size(); i++) {
						ob = bulletObjects.get(i);
						if (ob.getBody().equals(body)) {
							// Window.setTitle("ID: " + ob.getID());
							found = true;
							break;
						}
					}

					if (found) {
						if (Input.isMousePressed(Window.window, Key.MOUSE_BUTTON_LEFT)) {
							ob.getObject().interact();
						}
					}
					// body.setActivationState(CollisionObject.ACTIVE_TAG);
					// Vector3f impulse = new Vector3f(end);
					// impulse.normalize();
					// float impulseStrength = 1f;
					// impulse.scale(impulseStrength);
					// Vector3f relPos = new Vector3f();
					// relPos.sub(rayCallback.hitPointWorld,
					// body.getCenterOfMassPosition(new Vector3f()));
					// body.applyImpulse(impulse, relPos);
				}
			}
		}
	}

	private void input() {
		// if (Input.isKeyDown(Window.window, Key.KEY_W)) {
		// float x = (float) (Math.sin(camera.getYaw() * Math.PI / 180) *
		// camera.SPEED * Time.getDelta());
		// float y = 0f;
		// float z = (float) -(Math.cos(camera.getYaw() * Math.PI / 180) *
		// camera.SPEED * Time.getDelta());
		// Vector3f force = new Vector3f(x, y, z);
		// player.getBody().activate();
		// player.getBody().setLinearVelocity(force);
		// camera.moveForward();
		// }

		if (Input.isKeyDown(Window.window, Key.KEY_W)) {
			camera.moveForward();
		}

		if (Input.isKeyDown(Window.window, Key.KEY_S)) {
			camera.moveBack();
		}

		if (Input.isKeyDown(Window.window, Key.KEY_A)) {
			camera.moveLeft();
		}

		if (Input.isKeyDown(Window.window, Key.KEY_D)) {
			camera.moveRight();
		}

		if (Input.isKeyDown(Window.window, Key.KEY_P)) {
			world.world.stepSimulation(0.005f);
		}

		if (Input.isKeyDown(Window.window, Key.KEY_UP)) {
			System.out.println("UP");
			player.getBody().applyImpulse(new Vector3f(0f, 0f, 1f), player.getObject().getPosition());
		}

		if (Input.isKeyPressed(Window.window, Key.KEY_1)) {
			yLoc += 0.5f;
		}

		if (Input.isKeyDown(Window.window, Key.KEY_Q)) {
			camera.setYaw(camera.getYaw() - 0.05f);
		}

		if (Input.isKeyDown(Window.window, Key.KEY_E)) {
			camera.setYaw(camera.getYaw() + 0.05f);
		}
	}

	float yLoc = -1f;

	public float[] getVertices() {
		float[] vertices = {
				//
				-1f, 1f, 0f, //
				-1f, -1f, 0f, //
				1f, -1f, 0f, //
				1f, 1f, 0f //
		};
		return vertices;
	}

	public Vector3f toEulerianAngle(Quat4f q) {
		double ysqr = q.y * q.y;

		// roll (x-axis rotation)
		double t0 = +2.0 * (q.w * q.x + q.y * q.z);
		double t1 = +1.0 - 2.0 * (q.x * q.x + ysqr);
		float roll = (float) Math.atan2(t0, t1);

		// pitch (y-axis rotation)
		double t2 = +2.0 * (q.w * q.y - q.z * q.x);
		t2 = t2 > 1.0 ? 1.0 : t2;
		t2 = t2 < -1.0 ? -1.0 : t2;
		float pitch = (float) Math.asin(t2);

		// yaw (z-axis rotation)
		double t3 = +2.0 * (q.w * q.z + q.x * q.y);
		double t4 = +1.0 - 2.0 * (ysqr + q.z * q.z);
		float yaw = (float) Math.atan2(t3, t4);

		Vector3f v = new Vector3f(roll, pitch, yaw);
		return v;
	}

	public float[] getTexCoords() {
		float[] texCoords = {
				//
				0, 0, //
				0, 1, //
				1, 1, //
				1, 0, //
		};
		return texCoords;
	}

	public int[] getIndices() {
		int[] indices = {
				//
				0, 1, 2, //
				2, 3, 0 //
		};
		return indices;
	}

	public static void main(String[] args) {
		new ReleaseGame();
	}

}
