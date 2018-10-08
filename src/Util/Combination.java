package Util;

import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Combination {
	private List<Vector3f> vertices;
	private List<Vector3f> normals;
	private List<Vector2f> textures;

	public Combination(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textures,
			List<Integer> indices) {
		super();
		this.vertices = vertices;
		this.normals = normals;
		this.textures = textures;
		this.indices = indices;
	}

	private List<Integer> indices;

	public List<Vector3f> getVertices() {
		return vertices;
	}
	
	public List<Vector3f> getNormals() {
		return normals;
	}

	public List<Vector2f> getTextures() {
		return textures;
	}
	
	public List<Integer> getIndices() {
		return indices;
	}

}
