package render;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class MatrixManager{
	
	protected static Matrix4f projection;
	protected static Matrix4f view;
	protected static int progId;
	
	public MatrixManager(int progId) {
		MatrixManager.progId = progId;
		
		view = new Matrix4f();
		createView(view);
		projection = createMatrices(projection);
	}
	
	
	protected static void setVec3(String name, Vector3f value) {
		int location = GL30.glGetUniformLocation(progId, name);

		FloatBuffer fb = BufferUtils.createFloatBuffer((int)value.length() * 3);
		value.get(fb);

		GL30.glUniform3f(location, value.x, value.y, value.z);
	}
	
	protected static void setMatrix(String name, Matrix4f value) {
		int location = GL30.glGetUniformLocation(progId, name);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);

		value.get(fb);

		GL30.glUniformMatrix4fv(location, false, fb);
	}

	protected static void createView(Matrix4f view) {
		view.rotate((float) Math.toRadians(Camera.getYaw()), new Vector3f(0, 1, 0));
		view.rotate((float) Math.toRadians(Camera.getPitch()), new Vector3f(1, 0, 0));

		view.translate(new Vector3f(-Camera.getPosition().x, -Camera.getPosition().y, -Camera.getPosition().z));
	}

	protected static Matrix4f createMatrices(Matrix4f projection) {
		projection = new Matrix4f().perspective(Camera.FOV, Camera.aspect, Camera.near, Camera.far);
		return projection;
	}
}
