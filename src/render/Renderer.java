package render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import core.Loader;

public class Renderer {

	private int progId;
	private List<GameObject> objects = new ArrayList<>();
	ShaderManager sm;
	
	Camera cam;
	
	
	public Renderer(String vertpath, String fragpath) {
		progId = Loader.LoadShaders(vertpath, fragpath);
	
		cam = new Camera();
		sm = new ShaderManager(progId);
		
		float[] verts = {
				 -1.0f,-1.0f,-1.0f, 
				    -1.0f,-1.0f, 1.0f,
				    -1.0f, 1.0f, 1.0f, 
				    1.0f, 1.0f,-1.0f, 
				    -1.0f,-1.0f,-1.0f,
				    -1.0f, 1.0f,-1.0f, 
				    1.0f,-1.0f, 1.0f,
				    -1.0f,-1.0f,-1.0f,
				    1.0f,-1.0f,-1.0f,
				    1.0f, 1.0f,-1.0f,
				    1.0f,-1.0f,-1.0f,
				    -1.0f,-1.0f,-1.0f,
				    -1.0f,-1.0f,-1.0f,
				    -1.0f, 1.0f, 1.0f,
				    -1.0f, 1.0f,-1.0f,
				    1.0f,-1.0f, 1.0f,
				    -1.0f,-1.0f, 1.0f,
				    -1.0f,-1.0f,-1.0f,
				    -1.0f, 1.0f, 1.0f,
				    -1.0f,-1.0f, 1.0f,
				    1.0f,-1.0f, 1.0f,
				    1.0f, 1.0f, 1.0f,
				    1.0f,-1.0f,-1.0f,
				    1.0f, 1.0f,-1.0f,
				    1.0f,-1.0f,-1.0f,
				    1.0f, 1.0f, 1.0f,
				    1.0f,-1.0f, 1.0f,
				    1.0f, 1.0f, 1.0f,
				    1.0f, 1.0f,-1.0f,
				    -1.0f, 1.0f,-1.0f,
				    1.0f, 1.0f, 1.0f,
				    -1.0f, 1.0f,-1.0f,
				    -1.0f, 1.0f, 1.0f,
				    1.0f, 1.0f, 1.0f,
				    -1.0f, 1.0f, 1.0f,
				    1.0f,-1.0f, 1.0f
				    };
		
		addObject(verts, "res/image.png");
	}

	public void addObject(float[] verts, String image)
	{
		objects.add(new GameObject(verts, image));
	}
	
	public void Render()
	{
		GL20.glUseProgram(progId);
		
		sm.render();
		for(GameObject o : objects)
		{
			sm.render(o);
			o.Draw();
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
