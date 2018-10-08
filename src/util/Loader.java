package util;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.lwjgl.stb.*;

import static org.lwjgl.stb.STBImage.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import data.GameObject;
import data.Model;
import data.Texture;
import data.util.Combination;
import data.util.DataList;
import data.util.DataPair;

public class Loader {
	public static int LoadShaders(String vert, String frag) {
		int progId;

		int vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		String vertSource = "", fragSource = "";

		try (BufferedReader br = new BufferedReader(new FileReader(new File("res/shaders/" + vert)))) {
			String line;
			while ((line = br.readLine()) != null) {
				vertSource += (line + '\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(new File("res/shaders/" + frag)))) {
			String line;
			while ((line = br.readLine()) != null) {
				fragSource += (line + '\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		GL20.glShaderSource(vertId, vertSource);
		GL20.glCompileShader(vertId);

		System.out.println(GL20.glGetShaderInfoLog(vertId));

		GL20.glShaderSource(fragId, fragSource);
		GL20.glCompileShader(fragId);

		System.out.println(GL20.glGetShaderInfoLog(fragId));

		progId = GL20.glCreateProgram();

		GL20.glAttachShader(progId, vertId);
		GL20.glAttachShader(progId, fragId);
		GL20.glLinkProgram(progId);

		GL20.glDetachShader(progId, vertId);
		GL20.glDetachShader(progId, fragId);

		GL20.glDeleteShader(vertId);
		GL20.glDeleteShader(fragId);

		return progId;
	}

	public static int LoadImage(String imagePath) {
		int[] width = new int[1], height = new int[1], nrChannels = new int[1];

		ByteBuffer data = stbi_load("res/images/"+ imagePath, width, height, nrChannels, 0);

		int texId = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texId);

		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);

		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGB, width[0], height[0], 0, GL30.GL_RGB,
				GL30.GL_UNSIGNED_BYTE, data);

		return texId;
	}
	

	private static List<Vector2f> Process2f(String src, String flag) {
		String[] lines = src.split("\n");

		for (String s : lines) {
			if (s.contains("<float_array") && s.contains(flag)) {
				// We found the line with vertices
				char[] characters = s.toCharArray();

				String nStr = "";
				// Remove Tags
				for (int b = 0; b <= characters.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					if (b <= s.indexOf('>')) {
						continue;
					} else {
						nStr += characters[b];
					}

				}
				// Removed First tag, remove second tag
				String vStr = "";
				char[] chara = nStr.toCharArray();
				for (int b = 0; b <= chara.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					vStr += chara[b];

					if (b == nStr.indexOf('<') - 1) {
						break;
					}
				}

				String[] vertStrings = vStr.split(" ");

				List<Vector2f> vertices = new ArrayList<>();
				int counter = 0;
				for (int i = 1; i < vertStrings.length; i++) {
					counter++;

					
					if (counter == 2) {
						
						Float x = Float.parseFloat(vertStrings[i-1]);
						Float y = Float.parseFloat(vertStrings[i]);
						
						vertices.add(new Vector2f(x, y));
						counter = 0;
					}
				}
				return vertices;
			}
		}
		return null;
	}
	
	private static List<Vector3f> Process(String src, String flag) {
		String[] lines = src.split("\n");

		for (String s : lines) {
			if (s.contains("<float_array") && s.contains(flag)) {
				// We found the line with vertices
				char[] characters = s.toCharArray();

				String nStr = "";
				// Remove Tags
				for (int b = 0; b <= characters.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					if (b <= s.indexOf('>')) {
						continue;
					} else {
						nStr += characters[b];
					}

				}
				// Removed First tag, remove second tag
				String vStr = "";
				char[] chara = nStr.toCharArray();
				for (int b = 0; b <= chara.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					vStr += chara[b];

					if (b == nStr.indexOf('<') - 1) {
						break;
					}
				}

				String[] vertStrings = vStr.split(" ");

				List<Vector3f> vertices = new ArrayList<>();
				int counter = 0;
				for (int i = 1; i < vertStrings.length; i++) {
					counter++;

					
					if (counter == 3) {
						
						Float x = Float.parseFloat(vertStrings[i-2]);
						Float y = Float.parseFloat(vertStrings[i-1]);
						Float z = Float.parseFloat(vertStrings[i]);
						
						vertices.add(new Vector3f(x, y, z));
						counter = 0;
					}
				}
				return vertices;
			}
		}
		return null;
	}
	
	private static List<Integer> GetIndices(String src)
	{
		String[] lines = src.split("\n");

		for (String s : lines) {
			if (s.contains("<p>")) {
				// We found the line with vertices
				char[] characters = s.toCharArray();

				String nStr = "";
				// Remove Tags
				for (int b = 0; b <= characters.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					if (b <= s.indexOf('>')) {
						continue;
					} else {
						nStr += characters[b];
					}

				}
				
				// Removed First tag, remove second tag
				String vStr = "";
				char[] chara = nStr.toCharArray();
				for (int b = 0; b <= chara.length - 1; b++) {
					// verts = verts.replace(characters[b], ' ');
					vStr += chara[b];

					if (b == nStr.indexOf('<') - 1) {
						break;
					}
				}

				List<Integer> indices = new ArrayList<>();
				
				for(int i = 1; i < vStr.split(" ").length; i++)
				{
					indices.add(Integer.parseInt(vStr.split(" ")[i].replace(" ", "")));
					
				}

				return indices;
			}
		}

		return null;
	}

	private static Combination ReadData(String src)
	{
		List<Vector3f> iVerts = Process(src, "positions");
		List<Vector3f> iNorms = Process(src, "normals");
		List<Vector2f> iTex = Process2f(src, "texcoord");
		List<Integer> indices = GetIndices(src);
		
		
		List<Vector3f> verts = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Integer> ind = new ArrayList<>();
		
		
		Vector3f[] vArr = Utils.ToVecArray(iVerts);
		Vector3f[] nArr = Utils.ToVecArray(iNorms);
		Vector2f[] tArr = Utils.ToVecArray2f(iTex);
		int[] cArr = Utils.ToArrayInt(indices);
		
		DataList d = new DataList();
		
		
		for (int i = 0; i < cArr.length; i++) {
			Vector3f vertex = vArr[cArr[i]];
			Vector3f normal = nArr[cArr[i]];
			Vector2f texture = tArr[cArr[i]];
			
			DataPair pair = new DataPair(vertex, normal, texture);
			
			if(!d.contains(pair))
			{
				d.add(pair);
				verts.add(vertex);
				normals.add(normal);
				textures.add(texture);
				ind.add(d.getKey(pair));
			}
			else
			{
				ind.add(d.getKey(pair));
			}
		}
		
		return new Combination(verts, normals, textures, ind);
	}
	
	public static GameObject LoadObj(String file, String image)
	{
		Scanner s;
		String src = "";
		try {
			s = new Scanner(new File("res/models/" + file + ".dae"));
			while(s.hasNextLine())
			{
				src += s.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Combination c = ReadData(src);
		
		
		List<Vector3f> vertices = c.getVertices();
		List<Vector3f> normals = c.getNormals();
		List<Vector2f> textures = c.getTextures();
		List<Integer> indices = c.getIndices();
		
		float[] vArr = Utils.VecToArr(vertices);
		float[] nArr = Utils.VecToArr(normals);
		float[] tArr = Utils.VecToArr2f(textures);
		int[] ind = Utils.ToArrayInt(indices);
		
		Texture t = new Texture(image);
		
		Model model = new Model(vArr, nArr, tArr, ind, t);
		return new GameObject(model);
	}
}
