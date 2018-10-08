package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import Util.Texture;
import core.Loader;

public class GameObject {
	private int vao, vbo, tbo, ebo, texId;
	
	private Matrix4f model;
	String imagePath;
	
	private float[] vertices, texCoords;
	private int[] indices;

	private Vector3f position, rotation, scale;
	Texture texture;
	
	public GameObject(float[] vertices, float[] texCoords, int[] indices,  Texture t) {
		this.vertices = vertices;
		this.texture = t;
		this.texCoords = texCoords;
		this.indices = indices;
		
		model = new Matrix4f();
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		
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
	
	
	public void Draw()
	{	
		GL30.glBindVertexArray(vao);
		
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		
		//Fix This
		GL30.glDrawElements(GL30.GL_TRIANGLES, indices.length, GL30.GL_UNSIGNED_INT, 0);
		
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
	}
	
	private void GenArrays()
	{
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		vbo = loadAttrib(0, vertices, 3);
		
		texId = texture.getTexId();
		
		tbo = loadAttrib(1, texCoords, 2);
		
		ebo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
	}
	
	public void CleanUp()
	{
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteBuffers(vbo);
		GL30.glDeleteBuffers(tbo);
	}
	
	
	public Matrix4f getModel() {
		return model;
	}

	public void setModel(Matrix4f model) {
		this.model = model;
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
	
	public Texture getTexture() {
		return texture;
	}

}
