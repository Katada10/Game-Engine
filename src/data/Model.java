package data;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class Model {
	private float[] vertices, normals, texCoords;
	private Texture texture;
	private int ebo;
	private int id;

	public void setId(int id) {
		this.id = id;
	}

	private Matrix4f modelMat;
	

	public Model(float[] vertices, float[] normals, float[] texCoords, int[] indices, Texture t) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.indices = indices;
		
		ebo = LoadIndices();
		this.texture = t;
		
		modelMat = new Matrix4f();
		
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
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

	public Matrix4f getModelMat() {
		return modelMat;
	}

	public void setModelMat(Matrix4f modelMat) {
		this.modelMat = modelMat;
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
	
	private Vector3f position, rotation, scale;	public Vector3f getPosition() {
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

	public int getEbo() {
		return ebo;
	}
	
	public int getId() {
		return id;
	}

}
