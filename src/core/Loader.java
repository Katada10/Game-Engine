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
	public static int LoadShaders(String vert, String frag)
	{
		int progId;
		
		int vertId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		
		String vertSource = "", fragSource = "";
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(new File("res/shaders/" +vert))))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				vertSource += (line + '\n');
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(new File("res/shaders/" + frag))))
		{
			String line;
			while((line = br.readLine()) != null)
			{
				fragSource += (line + '\n');
			}
		}
		catch(Exception e)
		{
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
		int[] width =new int[1], height = new int[1], nrChannels = new int[1];
		
		ByteBuffer data = stbi_load(imagePath ,width, height,nrChannels,0);
		
		int texId = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texId);
		
		
		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGB, width[0],
			    height[0], 0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE, data);
		
		return texId;
	}


	public static GameObject LoadObj(String path, String imagePath)
	{
		
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
		
		
		
		float[] vertices = ProcessVerts(src);
		float[] texCoords = ProcessTex(src);
		
		
		int[] indices =  indexVBO(src);
		
		return new GameObject(vertices, texCoords, indices, imagePath);
	}


	private static int[] indexVBO(String src)
	{
		String[] lines = src.split("\n");
		String toRet = "";
		
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].toCharArray()[0] == 'f')
			{
				toRet += lines[i].replace("f ", " ").replace(" ", ",");
			}
		}
		
		char[] x = toRet.toCharArray();
		x[0] = ' ';
		
		
		toRet = new String(x);

		String[] pairs = toRet.split(",");
		
		List<Integer> vertIndices = new ArrayList<>();
		List<Integer> texIndices = new ArrayList<>();
		
		for(int i = 0; i < pairs.length; i++)
		{
			String p = pairs[i].replace(" ", "");
			
			String[] split = p.split("/");
			
			vertIndices.add(Integer.parseInt(split[0]));
			texIndices.add(Integer.parseInt(split[1]));
		}
		
		
		HashMap<Integer, Vertex> map = new HashMap<>();
	
		 lines = src.split("\n");
		 toRet = "";
		
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].toCharArray()[0] == 'v' && lines[i].toCharArray()[1] == ' ')
			{
				toRet += lines[i].replace("v ", "");
				toRet += ",";
			}
		}
		
		char[] arr = toRet.toCharArray();
		arr[arr.length - 1] = ' ';
		
		toRet = new String(arr);
		
		String[] myarr = toRet.split(",");
		
		for(int i = 0 ; i < myarr.length; i++)
		{
			String a = myarr[i];
			
			String[] str = a.split(" ");

			Vertex vert = new Vertex(Float.parseFloat(str[0]), Float.parseFloat(str[1]), 
					Float.parseFloat(str[2]));
			
			vert.setKey(i);
			map.put(i, vert);
		}

		int[] ind = new int[vertIndices.size()];
		
		for(int i = 0; i < vertIndices.size(); i++)
		{
			ind[i] = vertIndices.get(i);
		}
		
		return ind;
	}

	private static float[] ProcessVerts(String src) {
		float[] verts;

		String[] lines = src.split("\n");
		String toRet = "";
		
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].toCharArray()[0] == 'v' && lines[i].toCharArray()[1] == ' ')
			{
				toRet += lines[i].replace("v", "").replace("\n", ",").replace(" ", ",");
			}
		}
		
		
		char[] b= toRet.toCharArray();
		b[0] = ' ';
		
		toRet = new String(b);


		String[] floats = toRet.split(","); 
		verts = new float[floats.length];
		
		
		for (int i = 0; i < verts.length; i++) {
			verts[i] = Float.parseFloat(floats[i]);
		}
		
		
		return verts;
	}

	
	private static float[] ProcessTex(String src)
	{
		float[] tex;

		String[] lines = src.split("\n");
		String toRet = "";
		
		for (int i = 0; i < lines.length; i++) {
			if(lines[i].toCharArray()[0] == 'v' && lines[i].toCharArray()[1] == 't')
			{
				toRet += lines[i].replace("vt", "").replace("\n", ",").replace(" ", ",");
			}
		}
		
		
		char[] b= toRet.toCharArray();
		b[0] = ' ';
		
		toRet = new String(b);


		String[] floats = toRet.split(","); 
		tex = new float[floats.length];
		
		
		for (int i = 0; i < tex.length; i++) {
			tex[i] = Float.parseFloat(floats[i]);
		}
		
		
		return tex;
	}

}
