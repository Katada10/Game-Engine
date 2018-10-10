package render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

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

		GameObject o = Loader.LoadObj("cube", "mario.jpg");
		GameObject o2 = Loader.LoadObj("cube", "mario.jpg");

		addObject(o2);
		addObject(o);
	}

	public void bind(Model m) {
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);

		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, m.getEbo());
	}

	public void draw(GameObject o) {
		sm.render(o);

		GL30.glDrawElements(GL30.GL_TRIANGLES, BufferManager.models.get(o.getModelId()).getIndices().length, GL30.GL_UNSIGNED_INT, 0);
	}

	public void render() {
		BufferManager.start();

		cam.Move();

		sm.render(cam);

		for (Model m : BufferManager.modList) {
			bind(m);
		}

		//Instead of arrays, change to instanced
		for (GameObject obj : objects) {
			draw(obj);
		}

		finish();
	}

	public void finish() {
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);

		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
		GL30.glBindVertexArray(2);

		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
	}

	public void destroy() {
		BufferManager.cleanup();
		for (GameObject o : objects) {
			o.CleanUp();
		}
	}

	public void addObject(GameObject o) {
		objects.add(o);
	}

}
