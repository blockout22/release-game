package game;

import javax.vecmath.Vector3f;

public class MyObject extends MeshObject {

	public MyObject(Mesh mesh, Vector3f position, Vector3f rotation, float scale) {
		super(mesh, position, rotation, scale);
	}

	@Override
	public void update() {
	}

	@Override
	public void interact() {
		System.out.println("This is a my object");
	}

}
