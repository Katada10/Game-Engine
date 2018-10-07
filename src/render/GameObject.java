package render;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import core.Loader;

public class GameObject {
	private int vao;
	private int vbo;
	private int tbo;
	
	int ebo;
	
	private int texId;
	String imagePath;
	
	private float[] vertices, texCoords;
	private int[] indices;

	private Vector3f position, rotation, scale;

	public GameObject(float[] vertices, float[] texCoords, int[] indices, String path) {
		this.vertices = vertices;
		this.imagePath = path;
		this.texCoords = texCoords;
		this.indices = indices;

		position = new Vector3f(0, -0.001f, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		
		GenArrays();
	}
	
	public void Draw()
	{
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texId);
		
		GL30.glBindVertexArray(vao);
		
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL30.glDrawElements(GL30.GL_TRIANGLE_FAN, indices.length, GL30.GL_UNSIGNED_INT, 0);
		
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		
		GL30.glBindVertexArray(0);
	}
	
	private void GenArrays()
	{
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		vbo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT,false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		
		
		ebo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		texId = Loader.LoadImage("res/images/" + imagePath);
		
		
		tbo = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, tbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, texCoords, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT,false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
	}
	
	public void CleanUp()
	{
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteBuffers(vbo);
		GL30.glDeleteBuffers(tbo);
	}
	
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
}
