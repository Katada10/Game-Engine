package data;

import util.Loader;

public class Texture {

	private int texId;
	
	public int getTexId() {
		return texId;
	}

	public void setTexId(int texId) {
		this.texId = texId;
	}

	private void loadTexture(String path)
	{
		texId = Loader.LoadImage(path);
	}
	
	public Texture(String path)
	{
		loadTexture(path);
	}
}
