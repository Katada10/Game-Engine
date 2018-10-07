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
		
		addObject(Loader.LoadObj("cube1.obj", "image.jpg"));
	}

	public void addObject(float[] verts, float[] texCoords, int[] indices, String image)
	{
		objects.add(new GameObject(verts, texCoords, indices, image));
	}
	
	public void addObject(GameObject o)
	{
		objects.add(o);
	}
	
	public void Render()
	{
		GL20.glUseProgram(progId);
		
		sm.render();
		for(GameObject o : objects)
		{
			o.setRotation(new Vector3f(0, -0.01f, 0));
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
