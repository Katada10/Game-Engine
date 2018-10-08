package render;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import data.GameObject;
import data.Model;

public class ShaderManager extends MatrixManager {
	
	public ShaderManager(int progId) {
		super(progId);
	}
	
	public void render(GameObject o)
	{
		Model model = o.getModel();
		setMatrix("projection", projection);
		setMatrix("view", view);
		
		model.setModelMat(model.getModelMat().translate(model.getPosition()).
				rotate(model.getRotation().x, new Vector3f(1, 0, 0))
				.rotate(model.getRotation().y, new Vector3f(0, 1, 0))
				.rotate(model.getRotation().z, new Vector3f(0, 0, 1))
				.scale(model.getScale()));

		setMatrix("model", model.getModelMat());
		
		o.Draw();
	}
}
