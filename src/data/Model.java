package data;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model {
	private float[] vertices, normals, texCoords;
	
	public Matrix4f getModelMat() {
		return modelMat;
	}

	public void setModelMat(Matrix4f modelMat) {
		this.modelMat = modelMat;
	}

	private Matrix4f modelMat;
	
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

	public Texture getTexture() {
		return texture;
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
	Texture texture;
	
	public Model(float[] vertices, float[] normals, float[] texCoords, int[] indices, Texture texture) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.indices = indices;
		this.texture = texture;
		
		modelMat = new Matrix4f();
		
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
	}
}
