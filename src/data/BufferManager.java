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
	
	public static List<Model> modList = new ArrayList<>();
	public static Map<Integer, Model> models = new HashMap<>();
	public static Map<Model, Integer> ids = new HashMap<>();
	
	
	
	public static boolean equals(Model f, Model m)
	{
		for (int i = 0; i < f.getTexCoords().length; i++) {
			if(m.getTexCoords()[i] == f.getTexCoords()[i])
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static int getModelId(Model model)
	{
		for (Model m : modList) {
			if(equals(m, model))
			{
				return ids.get(m);
			}
		}
		return 0;
	}
	
	public static boolean containsModel(Model m)
	{
		for (Model model : modList) {
			for (int i = 0; i < model.getTexCoords().length; i++) {
				if(m.getTexCoords()[i] == model.getTexCoords()[i])
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	
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
