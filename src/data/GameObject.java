package data;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class GameObject {
	private List<Integer> buffers;
	private int modelId;

	private Vector3f position, rotation, scale;	
	

	private Matrix4f modelMat;
	
	public GameObject(int modelId) {
		buffers = new ArrayList<>();
		
		this.modelId = modelId;
		
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		
		modelMat = new Matrix4f();
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
	
	public Matrix4f getModelMat() {
		return modelMat;
	}

	public void setModelMat(Matrix4f modelMat) {
		this.modelMat = modelMat;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x,float y, float z) {
		this.position.x  = x;
		this.position.y = y;
		this.position.z  = z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x,float y, float z) {
		this.rotation.x  = x;
		this.rotation.y = y;
		this.rotation.z  = z;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(float x,float y, float z) {
		this.scale.x  = x;
		this.scale.y = y;
		this.scale.z  = z;
	}
}
