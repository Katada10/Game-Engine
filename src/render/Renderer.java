package render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Util.MatrixManager;
import core.Loader;
import core.ModelLoader;

public class Renderer {

	private int progId;
	private List<GameObject> objects = new ArrayList<>();
	ShaderManager sm;
	
	Camera cam;
	
	public Renderer(String vertpath, String fragpath) {
		progId = Loader.LoadShaders(vertpath, fragpath);
	
		cam = new Camera();
		sm = new ShaderManager(progId);
	
		addObject(Loader.LoadObj("cube", "mario.jpg"));
		
		/*for (GameObject obj : ModelLoader.Load("model.obj", "mario.jpg")) {
			addObject(obj);
		}*/
	}
	
	public void addObject(GameObject o)
	{
		objects.add(o);
	}
	
	public void Render()
	{
		GL20.glUseProgram(progId);
		
		
		for(GameObject o : objects)
		{
			o.getModel().setRotation(0, -0.01f, 0);
			sm.render(o);
		}
	}
	
	public void Destroy()
	{
		for(GameObject o : objects)
		{
			o.CleanUp();
		}
	}
}
