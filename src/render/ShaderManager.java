package render;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class ShaderManager {
	private int progId;
	private Matrix4f projection, view;
	
	
	public ShaderManager(int progId) {
		this.progId = progId;
		
		createMatrices();
		setUniform("sampler", 0);
	}
	
	public void render(GameObject o)
	{
		setMatrix("projection", projection);
		setMatrix("view", view);
		
		o.setModel(new Matrix4f().identity().translate(o.getPosition())
				.rotate(o.getRotation().x, new Vector3f(1, 0, 0))
				.rotate(o.getRotation().y, new Vector3f(0, 1, 0))
				.rotate(o.getRotation().z, new Vector3f(0, 0, 1))
				.scale(o.getScale()));
		
		setMatrix("model", o.getModel());
		o.Draw();
	}
	
	private void setMatrix(String name, Matrix4f value)
	{
		int location = GL30.glGetUniformLocation(progId, name);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		
		value.get(fb);
		
		GL30.glUniformMatrix4fv(location, false, fb);
	}
	
	private void setUniform(String name, int val)
	{
		int location = GL30.glGetUniformLocation(progId, name);
		GL30.glUniform1ui(location, val);
	}
	
	
	public void createMatrices()
	{
		projection = new Matrix4f().perspective(Camera.FOV, 
				Camera.aspect, Camera.near, 
				Camera.far);
		
		view = new Matrix4f().identity();
		view.rotate((float)Math.toRadians(Camera.getYaw()), new Vector3f(0, 1, 0));
		view.rotate((float)Math.toRadians(Camera.getPitch()), new Vector3f(1, 0, 0));
		
		view.translate(new Vector3f(-Camera.getPosition().x,
				-Camera.getPosition().y, -Camera.getPosition().z));
		
	}
}
