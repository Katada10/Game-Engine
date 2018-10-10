package render;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
	
	
	public void render(Camera cam, Model m)
	{
		view = createView(cam);
		setModelMat(m);
		
		
		GL20.glUseProgram(progId);
		setMatrix("model", m.getModelMat());
		setMatrix("view", view);
		setMatrix("projection", projection);
	}
	
	public void setModelMat(Model model)
	{	
		//PROBLEM WAS NOT SETTING IDENTITY
		model.setModelMat(new Matrix4f());
		model.setModelMat(model.getModelMat().translate(model.getPosition())
				.rotate((float)Math.toDegrees(model.getRotation().x), new Vector3f(1, 0, 0))
				.rotate((float)Math.toDegrees(model.getRotation().y), new Vector3f(0, 1, 0))
				.rotate((float)Math.toDegrees(model.getRotation().z), new Vector3f(0, 0, 1))
				.scale(model.getScale()));
	}
}
