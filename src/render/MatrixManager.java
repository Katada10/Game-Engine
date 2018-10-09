package render;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class MatrixManager{
	
	protected static Matrix4f projection;
	protected static int progId;
	
	public MatrixManager(int progId) {
		MatrixManager.progId = progId;
		
		projection = createMatrices(projection);
	}
	
	
	protected static void setVec3(String name, Vector3f value) {
		int location = GL30.glGetUniformLocation(progId, name);

		FloatBuffer fb = BufferUtils.createFloatBuffer((int)value.length() * 3);
		value.get(fb);

		GL30.glUniform3f(location, value.x, value.y, value.z);
	}
	
	protected static void setMatrix(String name, Matrix4f value) {
		int location = GL30.glGetUniformLocation(progId, name);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);

		value.get(fb);

		GL30.glUniformMatrix4fv(location, false, fb);
	}

	protected static Matrix4f createMatrices(Matrix4f projection) {
		projection = new Matrix4f().perspective(Camera.FOV, Camera.aspect, Camera.near, Camera.far);
		return projection;
	}
}
