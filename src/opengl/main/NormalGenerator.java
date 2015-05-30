package opengl.main;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class NormalGenerator {
	public static void main(String[] args) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(36 * 3);
		vertices.put(new float[] {
				-1.0f,-1.0f,-1.0f, // triangle 1 : begin
			    -1.0f,-1.0f, 1.0f,
			    -1.0f, 1.0f, 1.0f, // triangle 1 : end
			    1.0f, 1.0f,-1.0f, // triangle 2 : begin
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f,-1.0f, // triangle 2 : end
			    1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    -1.0f,-1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f, 1.0f,-1.0f,
			    -1.0f, 1.0f,-1.0f,
			    1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f
		});
		vertices.flip();
		
		while (true) {
			Vector3f vec1 = new Vector3f(vertices.get(), vertices.get(), vertices.get());
			Vector3f vec2 = new Vector3f(vertices.get(), vertices.get(), vertices.get());
			Vector3f vec3 = new Vector3f(vertices.get(), vertices.get(), vertices.get());
			
			Vector3f edge1 = vec2.subtract(vec1);
			Vector3f edge2 = vec3.subtract(vec1);
			Vector3f normal = edge1.cross(edge2).normalize();
			
			System.out.println(normal.x+"f, "+normal.y+"f, "+normal.z+"f,");
			System.out.println(normal.x+"f, "+normal.y+"f, "+normal.z+"f,");
			System.out.println(normal.x+"f, "+normal.y+"f, "+normal.z+"f,");
		}
	}
}
