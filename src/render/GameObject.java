package render;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

public class GameObject {
	private int vao;
	private int vbo;
	String imagePath;
	
	private float[] vertices;

	private Vector3f position, rotation, scale;

	public GameObject(float[] vertices, String path) {
		this.vertices = vertices;
		this.imagePath = path;

		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		
		GenArrays();
	}
	
	public void Draw()
	{
		GL30.glBindVertexArray(vao);
		GL30.glEnableVertexAttribArray(0);
		
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, vertices.length);
		
		GL30.glDisableVertexAttribArray(0);
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
		
		GL30.glBindVertexArray(0);
		GL30.glBindVertexArray(1);
	}
	
	public void CleanUp()
	{
		GL30.glDeleteVertexArrays(vao);
		GL30.glDeleteBuffers(vbo);
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
