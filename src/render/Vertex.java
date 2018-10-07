package render;

public class Vertex {
	
	
	private float x, y, z;
	private int key;
	
	public int getKey()
	{
		return key;
	}
	
	public void setKey(int key)
	{
		this.key = key;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
