package data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

public class GameObject {
	private List<Integer> buffers;
	private int modelId;

	public GameObject(int modelId) {
		buffers = new ArrayList<>();
		
		this.modelId = modelId;
		
		GenArrays();
	}
	
	private int loadAttrib(int id, float[] data, int size)
	{
		int v = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, v);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(id, size, GL30.GL_FLOAT,false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		return v;
	}
	
	public void CleanUp()
	{	
		for (Integer i : buffers) {
			GL30.glDeleteBuffers(i);
		}
	}
	
	private void GenArrays()
	{
		Model model = BufferManager.models.get(modelId);
		buffers.add(loadAttrib(0, model.getVertices(), 3));
		buffers.add(loadAttrib(1, model.getTexCoords(), 2));
		buffers.add(loadAttrib(2, model.getNormals(), 3));
	}
	
	public int getModelId() {
		return modelId;
	}
}
