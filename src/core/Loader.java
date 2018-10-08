package core;

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
	
}
