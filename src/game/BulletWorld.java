package game;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

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
		System.out.println("GRAVITY: " + gravity);
	}
	
	public void createPlane()
	{
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1);
		Transform transform = new Transform();
		transform.basis.set(new Quat4d(0, 0, 0, 1));
		transform.origin.set(new Vector3f(0, -1, 0));
		DefaultMotionState groundMotionState = new DefaultMotionState(transform);
		
		RigidBodyConstructionInfo groundInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
		RigidBody groundBody = new RigidBody(groundInfo);
		
		world.addRigidBody(groundBody);
	}
	
	public void stepSimulation()
	{
//		world.stepSimulation(timeStep);
	}
	
	public CollisionShape createSphere(float radius){
		CollisionShape fallShape = new SphereShape(radius);
		return fallShape;
	}
	
	//shape, new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1f;
	public RigidBody addShapeToWorld(CollisionShape shape, Quat4d rotation, Vector3f position, float mass){
		Transform transform = new Transform();
		transform.basis.set(rotation);
		transform.origin.set(position);
		DefaultMotionState mostionState = new DefaultMotionState(transform);
		Vector3f intertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(mass, intertia);
		
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, mostionState, shape, intertia);
		RigidBody body = new RigidBody(info);
		
		world.addRigidBody(body);
		return body;
	}
	
	public void cleanup()
	{
		world.destroy();
	}

}
