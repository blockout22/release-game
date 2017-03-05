package game;

import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

public class BulletWorld {

	public BroadphaseInterface broadpahse = new DbvtBroadphase();
	public DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
	public CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
	public SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

	public DiscreteDynamicsWorld world = new DiscreteDynamicsWorld(dispatcher, broadpahse, solver, collisionConfiguration);
	{
		world.setGravity(new Vector3f(0, -10f, 0));
		Vector3f gravity = new Vector3f();
		world.getGravity(gravity);
	}

	public void createPlane() {
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1);
		Transform transform = new Transform();
		transform.basis.set(new Quat4d(0, 0, 0, 1));
		transform.origin.set(new Vector3f(0, -1, 0));
		DefaultMotionState groundMotionState = new DefaultMotionState(transform);

		RigidBodyConstructionInfo groundInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
		RigidBody groundBody = new RigidBody(groundInfo);

		world.addRigidBody(groundBody);
	}

	public BoxShape createBox(Vector3f halfExtents) {
		BoxShape box = new BoxShape(halfExtents);
		return box;
	}

	public void stepSimulation() {
		// world.stepSimulation(timeStep);
	}

	public SphereShape createSphere(float radius) {
		SphereShape fallShape = new SphereShape(radius);
		return fallShape;
	}
	
	public ConvexHullShape createConvexHull(ObjectArrayList<Vector3f> points){
		ConvexHullShape shape = new ConvexHullShape(points);
		return shape;
	}

	// shape, new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1f;
	
	//TODO depending on the distance from the Camera(Player) to the rigidBody, if the RigidBody is to far away and cant possible collide then relocate it to a closer target
	//TODO set a limit to how many RigidBody's are used at once (should save some memory)
	public RigidBody addShapeToWorld(CollisionShape shape, Quat4f rotation, Vector3f position, float mass, boolean allowRotation) {
		Transform transform = new Transform();
		transform.basis.set(rotation);
		transform.origin.set(position);
		DefaultMotionState mostionState = new DefaultMotionState(transform);
		Vector3f intertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(mass, intertia);

		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, mostionState, shape, intertia);
		RigidBody body = new RigidBody(info);
		if (!allowRotation) {
			body.setAngularFactor(0f);
		}

		world.addRigidBody(body);
		return body;
	}

	public void cleanup() {
		world.destroy();
	}

}
