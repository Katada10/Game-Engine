package Util;

import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Combination {
 private List<Vector3f> vertices;
 private List<Vector2f> textures;
 
 public Combination(List<Vector3f> vertices, List<Vector2f> textures, List<Integer> indices) {
	super();
	this.vertices = vertices;
	this.textures = textures;
	this.indices = indices;
}
private List<Integer> indices;
 
 
public List<Vector3f> getVertices() {
	return vertices;
}
public void setVertices(List<Vector3f> vertices) {
	this.vertices = vertices;
}
public List<Vector2f> getTextures() {
	return textures;
}
public void setTextures(List<Vector2f> textures) {
	this.textures = textures;
}
public List<Integer> getIndices() {
	return indices;
}
public void setIndices(List<Integer> indices) {
	this.indices = indices;
}
 
 
}
