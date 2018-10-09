package data;

import util.Loader;

public class Texture {

	private int texId;
	
	public Texture(int id)
	{
		this.texId = id;
	}
	
	public int getTexId() {
		return texId;
	}

	public void setTexId(int texId) {
		this.texId = texId;
	}
}
