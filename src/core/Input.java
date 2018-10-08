package core;

import org.lwjgl.glfw.GLFW;

public class Input {

	private static long window = Engine.getWindow();
	
	public static boolean getKey(int key)
	{
		if(GLFW.glfwGetKey(window, key) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
