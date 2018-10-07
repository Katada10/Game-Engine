package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
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

import render.Combination;
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

	public static GameObject LoadModel(String path, String imagePath) {

		String src = "";
		try {
			Scanner scanner = new Scanner(new File("res/models/" + path));
			while (scanner.hasNextLine()) {
				src += scanner.nextLine() + "\n";
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// tag is <float_array></float_array>
		// tex Coords are in mesh-map, vertices are mesh-positions
		// <p> contains indices, first vertex, then normal, then texcoord

		// You Got This!
		
		Combination comb = ProcessMain(src);
		
		List<Vector3f> verts = comb.getVerts();
		List<Vector2f> textures = comb.getTex();
		List<Integer> ind = comb.getIndices();

		
		float[] vertices = ConvertVector(verts);
		float[] texCoords = ConvertVector2f(textures);

		Integer[] arr = new Integer[ind.size()];
		arr = ind.toArray(arr);
		
		int[] x = new int[arr.length];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = arr[i];
		}
		
		
		for (int i = 0; i < vertices.length; i++) {
			System.out.print(vertices[i] + ", ");
		}
		
		System.out.println();
		
		for (int i = 0; i < texCoords.length; i++) {
			System.out.print(texCoords[i] + ", ");
		}
		
		System.out.println();
		
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i] + ", ");
		}
		
		// CHANGE THIS WHEN DONE
		return new GameObject(vertices, texCoords, x, imagePath);
	}

	private static float[] ConvertVector2f(List<Vector2f> pg) {
		Vector2f[] arr = new Vector2f[pg.size()];
		arr = pg.toArray(arr);
		
		List<Float> tconverted = new ArrayList<>();
		

		for (int i = 0; i < arr.length; i++) {
			tconverted.add(arr[i].x);
			tconverted.add(arr[i].y);
		}
		
		Float[] converted = new Float[tconverted.size()];
		converted = tconverted.toArray(converted);
		
		float[] x = new float[converted.length];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = converted[i];
		}
		
		return x;
	}
	
	private static float[] ConvertVector(List<Vector3f> pg) {
		Vector3f[] arr = new Vector3f[pg.size()];
		arr = pg.toArray(arr);
		
		List<Float> tconverted = new ArrayList<>();
		

		for (int i = 0; i < arr.length; i++) {
			tconverted.add(arr[i].x);
			tconverted.add(arr[i].y);
			tconverted.add(arr[i].z);
		}
		
		Float[] converted = new Float[tconverted.size()];
		converted = tconverted.toArray(converted);
		
		float[] x = new float[converted.length];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = converted[i];
		}
		
		return x;
	}

	private static List<Vector3f> ProcessIndices(String src) {
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

	private static  Combination ProcessMain(String src) {

		// Array vertices
		// Array texcoords

		List<Vector3f> vertices = Process(src, "mesh-positions");
		List<Vector2f> texCoords = Process2f(src, "mesh-map");
		
		List<Vector3f> indices = ProcessIndices(src);
		
		List<Vector3f> vertsUnpacked = new ArrayList<>();
		List<Vector2f> texUnpacked = new ArrayList<>();
		List<Integer> indicesUnpacked = new ArrayList<>();
		
		Vector3f[] vertArr = new Vector3f[vertices.size()];
		vertArr = vertices.toArray(vertArr);
		
		Vector2f[] texArr = new Vector2f[texCoords.size()];
		texArr = texCoords.toArray(texArr);
		
		Vector3f[] indexTriplets = new Vector3f[indices.size()];
		indexTriplets = indices.toArray(indexTriplets);
		
		for(int i = 0; i < indexTriplets.length; i++)
		{
			Vector3f vertex = vertArr[(int)indexTriplets[i].x];
			vertsUnpacked.add(vertex);
			
			Vector2f tex = texArr[(int)indexTriplets[i].y];
			texUnpacked.add(tex);
			
			indicesUnpacked.add(indicesUnpacked.size());
		}
		

		return new Combination(vertsUnpacked, texUnpacked, indicesUnpacked);
		
		/*
		 * Read vertices from file, store in vertice, same for texcoords
		 * 
		 * Array vertsunpacked, texunpacked Array int indicesunpacked
		 * 
		 * 
		 * Loop STARTS HERE (every three pairs) 
		 * v1 is the index v1/vt1 
		 * Vector3 vertex1 = vertices[v1] 
		 * vertsunpacked.add(vertex1)
		 * 
		 * same for other two verts
		 * 
		 * 
		 * same as previous but for texcoords Vector2 tex1 = texcoords[vt1]
		 * texunpacked.add(tex1)
		 * 
		 * Maybe some trouble here indicesUnpacked.add(indicesUnpacked.size)
		 * indicesUnpacked.add(indicesUnpacked.size)
		 * indicesUnpacked.add(indicesUnpacked.size) LOOP ENDS HERE - NEXT ITERATION
		 */
	}
}
