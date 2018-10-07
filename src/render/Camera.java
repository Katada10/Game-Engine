package render;

import org.joml.Vector3f;

import core.Engine;

public class Camera {

	public static float FOV = (float) Math.toRadians(60),
			aspect = Engine.getW() / Engine.getH(), near = 0.1f, far = 100f;
	
	private static Vector3f position;
	
	
	public Camera() {
		position = new Vector3f(0, 0, 8);
	}
	
	
	public static Vector3f getPosition() {
		return position;
	}
	
	
	public static void setPosition(Vector3f position) {
		Camera.position = position;
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
	
	
	
}
