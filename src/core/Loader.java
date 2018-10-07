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


	public static GameObject LoadModel(String path, String imagePath)
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
		
		
		// tag is <float_array></float_array>
		//tex Coords are in mesh-map, vertices are mesh-positions
		//<p> contains indices, first vertex, then normal, then texcoord
		
		//You Got This!
		float[] vertices = {-1, -1, 0, 1, -1 ,0 ,-1 ,1, 0 ,1, 1, 0};
		float[] texCoords = {0, 0 ,1 ,0.9999999f, 0, 1, 0 ,
				0, 0.9999999f, 0 ,1 ,0};
		
		int[] indices = {1 ,2  ,0 , 1 ,3 ,2};
		
		return new GameObject(vertices, texCoords, indices, imagePath);
	}


}
