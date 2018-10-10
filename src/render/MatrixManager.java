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
		
		createProjection();
	}
	
	public static Matrix4f createView(Camera cam) {
		Matrix4f view = new Matrix4f();
        view.rotate((float) Math.toRadians(cam.getPitch()), new Vector3f(1, 0, 0));
        view.rotate((float) Math.toRadians(cam.getYaw()), new Vector3f(0, 1, 0));
        Vector3f cameraPos = cam.getPosition();
        Vector3f neg = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        view.translate(neg);
        return view;
		
	}
	
	
	protected static void setVec3(String name, Vector3f value) {
		int location = GL30.glGetUniformLocation(progId, name);

		FloatBuffer fb = BufferUtils.createFloatBuffer((int)value.length() * 3);
		value.get(fb);

		GL30.glUniform3f(location, value.x, value.y, value.z);
	}
	
	public static void setMatrix(String name, Matrix4f value) {
		int location = GL30.glGetUniformLocation(progId, name);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);

		value.get(fb);

		GL30.glUniformMatrix4fv(location, false, fb);
	}

	public static void createProjection() {
		projection = new Matrix4f().perspective(Camera.FOV, Camera.aspect, Camera.near, Camera.far);
	}
}
