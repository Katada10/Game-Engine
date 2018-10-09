package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import core.Engine;
import core.Input;

public class Camera {

	public static float FOV = (float) Math.toRadians(60), aspect = Engine.getW() / Engine.getH(), near = 0.1f,
			far = 100f;

	private static Vector3f position;
	boolean isMoving = false;
	public Matrix4f view;
	
	
	public Camera() {

		position = new Vector3f(0, 0, 7);
		
		view = new Matrix4f();
		createView(view);
	}
	
	private void createView(Matrix4f view) {
		
		Vector3f neg = new Vector3f(-position.x, -position.y, -position.z);
		view.translate(neg)
				.rotate(pitch, new Vector3f(1, 0, 0))
				.rotate(yaw, new Vector3f(0, 1, 0));
		}
	
	public void Move()
	{
		if(Input.getKey(GLFW_KEY_A))
		{
			view.translate(0.2f, 0, 0);
		}
		if(Input.getKey(GLFW_KEY_S))
		{
			view.translate(0, 0, -0.2f);
		}
		if(Input.getKey(GLFW_KEY_D))
		{
			view.translate(-0.2f, 0, 0);
		}
		if(Input.getKey(GLFW_KEY_W))
		{
			view.translate(0, 0, 0.2f);
		}
	}

	public static Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public static float getYaw() {
		return yaw;
	}

	public static void setYaw(float yaw) {
		Camera.yaw = yaw;
	}

	public static float getPitch() {
		return pitch;
	}

	public static void setPitch(float pitch) {
		Camera.pitch = pitch;
	}

	private static float yaw, pitch;

	public boolean isMoving() {
		return isMoving;
	}

}
