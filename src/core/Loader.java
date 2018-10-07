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
import java.util.Scanner;

import org.lwjgl.stb.*;

import static org.lwjgl.stb.STBImage.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import render.GameObject;
import render.Vertex;

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
		float[] vertices = Process(src, "mesh-positions");

		float[] texCoords = Process(src, "mesh-map");

		int[] indices = ProcessIndices(src);

		System.out.println("Indices: ");
		for(int i : indices)
		{
			System.out.print(i + ", ");
		}
		return new GameObject(vertices, texCoords, indices, imagePath);
	}

	private static int[] ProcessIndices(String src)
	{	
		String[] lines = src.split("\n");

		for (String s : lines) {
			if (s.contains("<p>")) {
				// We found the line with vertices
				char[] characters = s.toCharArray();

				String nStr = "";
				//Remove Tags
				for(int b = 0; b <= characters.length - 1; b++)
				{
					//verts = verts.replace(characters[b], ' ');
					if(b <= s.indexOf('>'))
					{
						continue;
					}
					else
					{
						nStr += characters[b];
					}
					
				}	
				
				//Removed First tag, remove second tag
				String vStr = "";
				char[] chara = nStr.toCharArray();
				for(int b = 0; b <= chara.length - 1; b++)
				{
					//verts = verts.replace(characters[b], ' ');
					vStr += chara[b];
					
					if(b == nStr.indexOf('<') - 1)
					{
						break;
					}
				}	
				
				//Now we separated the vertices
				
				//Process the actual vertices
				
				String[] tess = vStr.split(" ");


				List<Integer> intList = new ArrayList<>();
				
				for (int i = 0; i < tess.length; i += 3) {
					intList.add(Integer.parseInt(tess[i]));
				}
				
				int[] indices = new int[intList.size()];
				
				for(int i = 0; i < indices.length; i++)
				{
					indices[i] = intList.get(i);
				}
			return indices;
			}
		}
		
		return null;
	}
	
	private static float[] Process(String src, String flag) {
		String[] lines = src.split("\n");

		for (String s : lines) {
			if (s.contains("<float_array") && s.contains(flag)) {
				// We found the line with vertices
				char[] characters = s.toCharArray();

				String nStr = "";
				//Remove Tags
				for(int b = 0; b <= characters.length - 1; b++)
				{
					//verts = verts.replace(characters[b], ' ');
					if(b <= s.indexOf('>'))
					{
						continue;
					}
					else
					{
						nStr += characters[b];
					}
					
				}	
				
				//Removed First tag, remove second tag
				String vStr = "";
				char[] chara = nStr.toCharArray();
				for(int b = 0; b <= chara.length - 1; b++)
				{
					//verts = verts.replace(characters[b], ' ');
					vStr += chara[b];
					
					if(b == nStr.indexOf('<') - 1)
					{
						break;
					}
				}	
				
				//Process the actual vertices
				
				String[] tess = vStr.split(" ");
			
				float[] vertArr = new float[tess.length];
				for (int i = 0; i < tess.length; i++) {
					vertArr[i] = Float.parseFloat(tess[i]);
				}
				return vertArr;
			}
		}
		

		return null;
	}

}
