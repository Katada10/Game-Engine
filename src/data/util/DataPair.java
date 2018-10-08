package data.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DataPair {

	private Vector3f vertex;
	private Vector3f normal;
	private Vector2f texture;
	
	
	
	public DataPair(Vector3f vertex, Vector3f normal, Vector2f texture) {
		super();
		this.vertex = vertex;
		this.texture = texture;
		this.normal = normal;
	}


	public Vector3f getVertex() {
		return vertex;
	}
	
	public Vector3f getNormal() {
		return normal;
	}

	public Vector2f getTexture() {
		return texture;
	}

	
}
