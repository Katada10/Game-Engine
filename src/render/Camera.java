package render;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import core.Engine;
import core.Input;

public class Camera {

	public static float FOV = (float) Math.toRadians(60), aspect = Engine.getW() / Engine.getH(), near = 0.1f,
			far = 100f;

	private static Vector3f position;
	boolean isMoving = false;

	public Camera() {
		position = new Vector3f(0, 0, 7);

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
