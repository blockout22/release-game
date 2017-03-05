package game;

import com.bulletphysics.dynamics.RigidBody;

public class PhysicsObject {

	private RigidBody body;
	private MeshObject object;

	public PhysicsObject(RigidBody body, MeshObject object) {
		super();
		this.body = body;
		this.object = object;
	}

	public RigidBody getBody() {
		return body;
	}

	public MeshObject getObject() {
		return object;
	}

}
