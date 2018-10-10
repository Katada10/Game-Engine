package data;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class Model {
	private float[] vertices, normals, texCoords;
	private Texture texture;
	private int ebo;
	private int id;

	public Model(float[] vertices, float[] normals, float[] texCoords, int[] indices, Texture t) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.indices = indices;
		
		ebo = LoadIndices();
		this.texture = t;
	
	}
	
	
	private int LoadIndices()
	{
		int ebo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
		return ebo;
	}
	
	public Texture getTexture() {
		return texture;
	}

	
	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTexCoords() {
		return texCoords;
	}

	private int[] indices;
	

	public int getEbo() {
		return ebo;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int i)
	{
		this.id = i;
	}
}
