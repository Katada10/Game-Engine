package shaders;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import data.BufferManager;
import data.GameObject;
import util.Loader;
import util.ModelLoader;

public class ShaderRenderer {

	private int progId;
	
	public ShaderRenderer(String vertpath, String fragpath) {
		progId = Loader.LoadShaders(vertpath, fragpath);
	}
	
	public int getProgId()
	{
		return progId;
	}
	
}
