package data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

public class GameObject {
	private int ebo;
	
	private List<Integer> buffers;
	private Model model;

	public GameObject(Model mesh) {
		buffers = new ArrayList<>();
		
		this.model = mesh;
		
		GenArrays();
	}
	
	public void Draw()
	{	
		prep();

		GL30.glDrawElements(GL30.GL_TRIANGLES, model.getIndices().length, GL30.GL_UNSIGNED_INT, 0);
		
		finish();
	}
	
	private void prep()
	{	
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
	}
	
	private void finish()
	{
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
		GL30.glBindVertexArray(2);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
	}
	
	private int loadIndices()
	{
		int ebo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, model.getIndices(), GL30.GL_STATIC_DRAW);
		return ebo;
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
		buffers.add(loadAttrib(0, model.getVertices(), 3));
		buffers.add(loadAttrib(1, model.getTexCoords(), 2));
		buffers.add(loadAttrib(2, model.getNormals(), 3));
		
		ebo = loadIndices();
		buffers.add(ebo);
	}
	
	public Model getModel() {
		return model;
	}
}
