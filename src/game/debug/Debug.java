package game.debug;

import javax.vecmath.Vector3f;

import com.bulletphysics.linearmath.IDebugDraw;

import game.Mesh;
import game.OBJLoader;

public class Debug extends IDebugDraw{
	
	private Mesh mesh;
	
	public Debug()
	{
		createBox();
	}
	
	public void createBox()
	{
		mesh = OBJLoader.load("box.obj");
	}

	@Override
	public void drawLine(Vector3f from, Vector3f to, Vector3f color) {
		System.out.println("DRAW LINE");
	}

	@Override
	public void drawContactPoint(Vector3f PointOnB, Vector3f normalOnB, float distance, int lifeTime, Vector3f color) {
		System.out.println("DRAW CONTACT POINT");
	}

	@Override
	public void reportErrorWarning(String warningString) {
		System.out.println("ERROR WARNING: " + warningString);
	}

	@Override
	public void draw3dText(Vector3f location, String textString) {
		System.out.println("DRAW 3D TEXT");
	}

	@Override
	public void setDebugMode(int debugMode) {
		System.out.println("DEBUG MODE: " + debugMode);
	}

	@Override
	public int getDebugMode() {
		return 0;
	}

}
