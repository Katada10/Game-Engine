package core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import render.Renderer;


public class Engine {

	private static int w, h;
	
	private String title;
	private static long window;
	
	
	public Engine(int w, int h, String title) {
		Engine.w = w;
		Engine.h = h;
		this.title = title;
	}
	
	public void Start()
	{
		Init();
		Run();
		Finish();
	}
	
	private void Init()
	{
		GLFWErrorCallback.createPrint(System.err).set();


		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		
		window = glfwCreateWindow(w, h, title, 0, 0);
		if ( window == 0 )
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window,(window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
		
	}
	
	private void Run()
	{
		GL.createCapabilities();
		
		Renderer r = new Renderer("vert.txt", "frag.txt");
		
		while ( !glfwWindowShouldClose(window) ) {
			glEnable(GL_DEPTH_TEST);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
			
			r.Render();
			
			glfwSwapBuffers(window); 
			glfwPollEvents();
		}
		
		r.Destroy();
	}
	
	private void Finish()
	{
		glfwDestroyWindow(window);

		glfwTerminate();
		 glfwSetErrorCallback(null).free();
	}
	
	

	public static float getW()
	{
		return (float) w;
	}
	
	public static float getH()
	{
		return (float) h;
	}
	
	
}
