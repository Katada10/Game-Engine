package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import core.Engine;
import core.Input;

public class Camera {

	public static float FOV = (float) Math.toRadians(60), aspect = Engine.getW() / Engine.getH(), near = 0.1f,
			far = 100f;
	private float yaw = 0, pitch = 0;

	private Vector3f position;

	public Camera() {

		position = new Vector3f(0, 0, 5);
	}
	
	
	public void Move()
	{
		if(Input.getKey(GLFW_KEY_A))
		{
			position.x -= 0.2;
		}
		if(Input.getKey(GLFW_KEY_D))
		{
			position.x += 0.2;
		}
		if(Input.getKey(GLFW_KEY_S))
		{
			position.z += 0.2;
		}
		if(Input.getKey(GLFW_KEY_W))
		{
			position.z -= 0.2;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getYaw() {
		return yaw;
	}

	
	public float getPitch() {
		return pitch;
	}

	
}
