package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.lwjgl.opengl.GL20;


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
}
