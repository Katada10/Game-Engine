package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL30;

import util.Loader;

public class BufferManager {

	public static int vao;
	public static int first = 0;
	
	public static Map<Integer, Texture> textures = new HashMap<>();
	public static Map<String, Texture> names = new HashMap<>();
	
	public static Map<Integer, Model> models = new HashMap<>();
	
	public static void init() {
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
	}

	public static void start() {
		GL30.glBindVertexArray(vao);
	}

	public static void cleanup() {
		GL30.glDeleteVertexArrays(vao);
	}
}
