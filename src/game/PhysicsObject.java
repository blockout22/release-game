package game;

import com.bulletphysics.dynamics.RigidBody;

public class PhysicsObject {

	private int ID;
	private RigidBody body;
	private MeshObject object;

	public PhysicsObject(int ID, RigidBody body, MeshObject object) {
		this.ID = ID;
		this.body = body;
		this.object = object;
	}
	
	public int getID()
	{
		return ID;
	}

	public RigidBody getBody() {
		return body;
	}

	public MeshObject getObject() {
		return object;
	}

}
