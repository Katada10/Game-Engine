package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javafx.scene.shape.Path;

public class Utils {

	
	public static float[] VecToArr2f(List<Vector2f> v)
	{

		List<Float> floats = new ArrayList<>();
		
		for (Vector2f vec : v) {
			floats.add(vec.x);
			floats.add(vec.y);
		}
		
		
		float[] fin = ToArray(floats);
		return fin;
	}
	
	public static float[] VecToArr(List<Vector3f> v)
	{

		List<Float> floats = new ArrayList<>();
		
		for (Vector3f vec : v) {
			floats.add(vec.x);
			floats.add(vec.y);
			floats.add(vec.z);
		}
		
		
		float[] fin = ToArray(floats);
		return fin;
	}
	
	
	public static Vector3f[] ToVecArray(List<Vector3f> v)
	{
		
		Vector3f[] b = new Vector3f[v.size()];
		b = v.toArray(b);

		return b;
	}
	
	public static Vector2f[] ToVecArray2f(List<Vector2f> v)
	{
		
		Vector2f[] b = new Vector2f[v.size()];
		b = v.toArray(b);

		return b;
	}
	
	
	public static float[] ToArray(List<Float> v)
	{
		
		Float[] b = new Float[v.size()];
		b = v.toArray(b);
		
		
		float[] fin = new float[b.length];
		
		for (int i = 0; i < fin.length; i++) {
			fin[i] = b[i];
		}
		return fin;
	}
	
	public static int[] ToArrayInt(List<Integer> v)
	{
		Integer[] arr = new Integer[v.size()];
		arr = v.toArray(arr);
		
		int[] ret = new int[arr.length];
		
		for (int i = 0; i < ret.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}
}
