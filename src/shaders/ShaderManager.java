package shaders;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import data.BufferManager;
import data.GameObject;
import data.Model;

public class ShaderManager extends MatrixManager {
	
	public ShaderManager(int progId) {
		super(progId);
	}
	
	public void setViewMat(Camera cam)
	{
		Matrix4f view = MatrixManager.createView(cam);
		setMatrix("view", view);
	}
	
	public void render(GameObject o)
	{
		setModelMat(o);
		setMatrix("model", o.getModelMat());
	}
	
	public void render(Camera cam)
	{
		view = createView(cam);
		
		GL20.glUseProgram(progId);
		
		setMatrix("view", view);
		setMatrix("projection", projection);
	}
	
	public void setModelMat(GameObject o)
	{	
		o.setModelMat(new Matrix4f());
		o.setModelMat(o.getModelMat().translate(o.getPosition())
				.rotate((float)Math.toDegrees(o.getRotation().x), new Vector3f(1, 0, 0))
				.rotate((float)Math.toDegrees(o.getRotation().y), new Vector3f(0, 1, 0))
				.rotate((float)Math.toDegrees(o.getRotation().z), new Vector3f(0, 0, 1))
				.scale(o.getScale()));
	}
}
