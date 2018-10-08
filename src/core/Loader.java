package core;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import Util.Combination;
import Util.DataList;
import Util.DataPair;
import Util.Utils;
import render.GameObject;

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

		ByteBuffer data = stbi_load(imagePath, width, height, nrChannels, 0);

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
				for (int i = 0; i < vertStrings.length; i++) {
					counter++;

					if (counter == 2) {
						vertices.add(new Vector2f(Float.parseFloat(vertStrings[i - 1]),
								Float.parseFloat(vertStrings[i])));
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
				for (int i = 0; i < vertStrings.length; i++) {
					counter++;

					if (counter == 3) {
						vertices.add(new Vector3f(Float.parseFloat(vertStrings[i - 2]),
								Float.parseFloat(vertStrings[i - 1]), Float.parseFloat(vertStrings[i])));
						counter = 0;
					}
				}
				return vertices;
			}
		}
		return null;
	}
	
	private static List<Vector3f> GetIndices(String src)
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

				List<Vector3f> indices = new ArrayList<>();
				
				// Below separates vertindices

				//int[] indices = new int[vStr.split(" ").length];
				int count = 0;
				for(int i = 0; i < vStr.split(" ").length; i++)
				{
					count++;
					if(count == 3)
					{
						indices.add(new Vector3f(
							Integer.parseInt(vStr.split(" ")[i - 2]), 
							Integer.parseInt(vStr.split(" ")[i - 1]),
							Integer.parseInt(vStr.split(" ")[i])));
						count = 0;
					}
					
				}
				// Process the actual vertices
				return indices;
			}
		}

		return null;
	}

	private static Combination ReadData(String src)
	{
		List<Vector3f> iVerts = Process(src, "mesh-positions");
		List<Vector2f> iTex = Process2f(src, "mesh-map");
		List<Vector3f> indices = GetIndices(src);
		
		for (Vector3f v : iVerts) {
			System.out.println(v);
		}
		
		List<Vector3f> vUnpacked = new ArrayList<>();
		List<Vector2f> tUnpacked = new ArrayList<>();
		List<Integer> iUnpacked = new ArrayList<>();
		
		Vector3f[] vArr = Utils.ToArray(iVerts);
		Vector2f[] tArr = Utils.ToArray2f(iTex);
		Vector3f[] cArr = Utils.ToArray(indices);
		
		DataList d = new DataList();
		
		for (int i = 0; i < cArr.length; i++) {
			Vector3f vertex = vArr[(int)cArr[i].x];
			
			Vector2f texture = tArr[(int)cArr[i].z];
			
			DataPair pair = new DataPair(vertex, texture);
			
			//Whatevs
			if(!d.contains(pair))
			{
				d.add(pair);
			}
			//DataPair pair = new DataPair(vertex, texture, counter);
			//FINALLY
			//If the pair already exists, add the pairs index to the draw list
			//FIGURE THIS OUT!
		}
		
		for(DataPair pair : d.getPairs())
		{
			vUnpacked.add(pair.getVert());
			tUnpacked.add(pair.getTex());
			iUnpacked.add(d.getKey(pair));
		}
		
		return new Combination(vUnpacked, tUnpacked, iUnpacked);
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
		List<Vector2f> textures = c.getTextures();
		List<Integer> indices = c.getIndices();
		
		Vector3f[] vArr = Utils.ToArray(vertices);
		Vector2f[] tArr = Utils.ToArray2f(textures);
		int[] ind = Utils.ToArrayInt(indices);
		
		FloatBuffer buf = BufferUtils.createFloatBuffer(vArr.length * 3);
		
		for (int i = 0; i < vArr.length; i++) {
			buf.put(vArr[i].x);
			buf.put(vArr[i].y);
			buf.put(vArr[i].z);
		}
		
		buf.flip();
		float[] verts = new float[buf.limit()];
		buf.get(verts);
		


		FloatBuffer b = BufferUtils.createFloatBuffer(tArr.length * 2);
		
		for (int i = 0; i < tArr.length; i++) {
			b.put(tArr[i].x);
			b.put(tArr[i].y);
		}
		
		b.flip();
		float[] tex = new float[b.limit()];
		b.get(tex);
		
		return new GameObject(verts, tex, ind, image);
	}
}
