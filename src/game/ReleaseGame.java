package game;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class ReleaseGame {
	
	private String TITLE = "Game ";
	
	//physics
	private ArrayList<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
	private DynamicsWorld dynamicsWorld;
	private BroadphaseInterface broadphase;
	private CollisionDispatcher dispatcher;
	private ConstraintSolver solver;
	private DefaultCollisionConfiguration collisionConfiguration;
	
	private BulletWorld world;
	
	public ReleaseGame()
	{
		Window.createWindow();
		world = new BulletWorld();
//		initPhysics();
		
		while(!Window.isCloseRequested()){
			Window.setTitle(TITLE + " [FPS: " + Window.getFPS() + "]");
			Window.update();
		}
		
		world.cleanup();
		Window.close();
	}

	private void initPhysics() {
		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		broadphase = new DbvtBroadphase();
		solver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3f(0f, -10f, 0f));
		
		//add ground
		addGround();
		
		addBox(new Vector3f(1, 1, 1), new Vector3f(5f, 5f, 5f), 1f);
	}
	
	private void addBox(Vector3f halfExtents, Vector3f origin, float mass) {
		CollisionShape colShape = new BoxShape(halfExtents);
		collisionShapes.add(colShape);
		
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		
//		float mass = 1f;
		
		boolean isDynamic = (mass != 0f);
		
		Vector3f localIntertia = new Vector3f(0, 0, 0);
		if(isDynamic){
			colShape.calculateLocalInertia(mass, localIntertia);
		}
		
		startTransform.origin.set(origin);
		
		DefaultMotionState motionState = new DefaultMotionState();
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, colShape);
		RigidBody body = new RigidBody(info);
		body.setActivationState(RigidBody.ISLAND_SLEEPING);
		dynamicsWorld.addRigidBody(body);
		body.setActivationState(RigidBody.ISLAND_SLEEPING);
	}

	private void addGround()
	{
		CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));
		collisionShapes.add(groundShape);
		
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(0, -56, 0);
		
		float mass = 0f;
		boolean isDynamic = (mass != 0f);
		
		Vector3f localIntertia = new Vector3f(0, 0, 0);
		if(isDynamic){
			groundShape.calculateLocalInertia(mass, localIntertia);
		}
		
		DefaultMotionState motionState = new DefaultMotionState(groundTransform);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, groundShape);
		RigidBody body = new RigidBody(info);
		
		dynamicsWorld.addRigidBody(body);
	}

	public static void main(String[] args) {
		new ReleaseGame();
	}

}
