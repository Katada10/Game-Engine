package render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import data.BufferManager;
import data.GameObject;
import data.Model;
import shaders.Camera;
import shaders.ShaderManager;
import shaders.ShaderRenderer;
import util.Loader;

public class Renderer {

	private List<GameObject> objects = new ArrayList<>();
	ShaderRenderer r;
	
	ShaderManager sm;
	
	Camera cam;
	
	public Renderer() {
		r = new ShaderRenderer("vert.txt", "frag.txt");
		
		cam = new Camera();
		sm = new ShaderManager(r.getProgId());
		
		BufferManager.init();
		
		GameObject o2 = Loader.LoadObj("cube", "mario.jpg");
		
		addObject(o2);
	}
	
	public void bind(GameObject obj)
	{
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		
		Model m = BufferManager.models.get(obj.getModelId());
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, m.getEbo());
	}
	
	public void draw(GameObject o)
	{
		BufferManager.start();
		
		cam.Move();
		
		sm.render(cam);
		
		Model m = BufferManager.models.get(o.getModelId());
		sm.render(m);
		
		bind(o);
		GL30.glDrawElements(GL30.GL_TRIANGLES, m.getIndices().length, GL30.GL_UNSIGNED_INT, 0);
		finish();
	}
	
	public void render()
	{
		for (GameObject obj : objects) {
			draw(obj);
		}
	}
	
	public void finish()
	{
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
		GL30.glBindVertexArray(2);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
	}
	
	public void destroy()
	{
		BufferManager.cleanup();
		for(GameObject o : objects)
		{
			o.CleanUp();
		}
	}
	

	public void addObject(GameObject o)
	{
		objects.add(o);
	}
	
}
