package Util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DataPair {

	private Vector3f vertex;
	private Vector2f texture;
	
	
	public DataPair(Vector3f vertex, Vector2f texture) {
		super();
		this.vertex = vertex;
		this.texture = texture;
	}


	public Vector3f getVertex() {
		return vertex;
	}


	public void setVertex(Vector3f vertex) {
		this.vertex = vertex;
	}


	public Vector2f getTexture() {
		return texture;
	}


	public void setTexture(Vector2f texture) {
		this.texture = texture;
	}
	
	
}
