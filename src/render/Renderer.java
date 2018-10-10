package render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import data.BufferManager;
import data.GameObject;
import util.Loader;
import util.ModelLoader;

public class Renderer {

	private int progId;
	private List<GameObject> objects = new ArrayList<>();
	ShaderManager sm;
	
	Camera cam;
	
	public Renderer(String vertpath, String fragpath) {
		progId = Loader.LoadShaders(vertpath, fragpath);
		cam = new Camera();
		sm = new ShaderManager(progId);
	
		//Set up matrices here (projection, view and model = new)
		//Get locations
		
		BufferManager.init();
		
		GameObject o2 = Loader.LoadObj("cube", "mario.jpg");
		addObject(o2);
	}
	
	public void addObject(GameObject o)
	{
		objects.add(o);
	}
	
	public void Render()
	{
		BufferManager.start();
		
		cam.Move();
		
		//reset view and model every frame
		//translate camera
		//scale, translate, and rotate model matrix
		//Then use program
		//then upload matrices
		//before drawing
		for(GameObject o : objects)
		{
			sm.render(cam, o.getModel());
			o.Draw();
			//sm.render(o);
		}
	}
	
	public void Destroy()
	{
		BufferManager.cleanup();
		for(GameObject o : objects)
		{
			o.CleanUp();
		}
	}
}
