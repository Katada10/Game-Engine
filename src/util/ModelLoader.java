package util;

import static org.lwjgl.assimp.Assimp.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AITexture;

import data.GameObject;
import data.Model;
import data.Texture;

public class ModelLoader {
	
	public static GameObject[] Load(String path, String tex)
	{
		AIScene scene = aiImportFile("res/models/" + path, 8 | 2);
		if(scene == null)
		{
			System.out.println("Wrong model path!");
		}
		
		
		GameObject[] objects = processNode(scene.mRootNode(), scene, tex);
		
		return objects;
	}

	private static GameObject[] processNode(AINode node, AIScene scene, String tex) {
		
		GameObject[] objects = new GameObject[scene.mNumMeshes()];
		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
			GameObject object = processMesh(mesh, scene, tex);
			objects[i] = object;
		}
		
		return objects;
	}

	private static GameObject processMesh(AIMesh mesh, AIScene scene, String path) {
		List<Float> vertices = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Float> textures = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		for(int i = 0; i < mesh.mNumVertices(); i++)
	    {
	        Vector3f vertex = new Vector3f();
	        
	        vertex.x = mesh.mVertices().get(i).x();
	        vertex.y = mesh.mVertices().get(i).y();
	        vertex.z = mesh.mVertices().get(i).z();
	        
	        vertices.add(vertex.x);
	        vertices.add(vertex.y);
	        vertices.add(vertex.z);
	        
	        Vector3f normal = new Vector3f();
	        
	        normal.x = mesh.mNormals().get(i).x();
	        normal.y = mesh.mNormals().get(i).y();
	        normal.z = mesh.mNormals().get(i).z();
	        
	        normals.add(normal.x);
	        normals.add(normal.y);
	        normals.add(normal.z);
	        
	        textures.add(mesh.mTextureCoords(0).get(i).x());
	        textures.add(mesh.mTextureCoords(0).get(i).y());
	    }
		
		for (int i = 0; i < mesh.mNumFaces(); i++) {
		    AIFace face = mesh.mFaces().get(i);
		    for (int j = 0; j < face.mNumIndices(); j++) {
		        int index = face.mIndices().get(j);
		        indices.add(index);
		    }           
		}
		
		float[] verts = Utils.ToArray(vertices);
		
		float[] norms = Utils.ToArray(normals);
		
		float[] tex = Utils.ToArray(textures);

		int[] ind = Utils.ToArrayInt(indices);
		
		Texture t = new Texture(path);

		
		Model model = new Model(verts, norms, tex, ind, t);
		GameObject o = new GameObject(model);
		return o;
	}
}