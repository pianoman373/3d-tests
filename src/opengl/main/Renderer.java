package opengl.main;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Renderer {
	private static FloatBuffer vertices;
	private static FloatBuffer normals;
	private static FloatBuffer uvs;
	private static int numVertices;
	private static boolean drawing;
	private static int vbo;
	private static int vao;
	public static int colorBuffer;
	public static int texBuffer;
	public static int normalBuffer;
	public static int indexBuffer;
	private static int shader;
	
	public static void init() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		vertices = BufferUtils.createFloatBuffer(1000000);
		
		long size = vertices.capacity() * Float.BYTES;
		glBufferData(GL_ARRAY_BUFFER, size, GL_STREAM_DRAW);
		
		//colors
		FloatBuffer colors = BufferUtils.createFloatBuffer(36 * 3);
		colors.put(Constants.colors);
		colors.flip();
		colorBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
		glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
		
		//texure coords
		uvs = BufferUtils.createFloatBuffer(1000000);
		texBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texBuffer);
		glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW);
		
		//normals
		normalBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
		normals = BufferUtils.createFloatBuffer(1000000);
		long normalSize = normals.capacity() * Float.BYTES;
		glBufferData(GL_ARRAY_BUFFER, normalSize, GL_STREAM_DRAW);
		
		glEnable(GL_CULL_FACE);
		
		numVertices = 0;
        drawing = false;
        
        shader = ShaderUtil.loadShader("resources/shaders/main.vsh", "resources/shaders/main.fsh");
        loadVertexAttrib();
	}
	
	public static void begin() {
		 if (drawing) {
	            throw new IllegalStateException("Renderer is already drawing!");
	    }
	    numVertices = 0;
	    drawing = true;
	}
	
	public static void addVertex(float x, float y, float z) {
		addVertexWithUV(x, y, z, 0, 0);
	}
	
	public static void addVertexWithUV(float x, float y, float z, float u, float v) {
		addVertexWithUVNormal(x, y, z, u, v, 0, 1, 0);
	}
	
	public static void addVertexWithUVNormal(float x, float y, float z, float u, float v, float normX, float normY, float normZ) {
		vertices.put(x).put(y).put(z);
		uvs.put(u).put(v);
		normals.put(normX).put(normY).put(normZ);
		//indices.put((short)numVertices);
		numVertices++;
	}
	
	public static void end() {
		if (!drawing) {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
	    flush();
	    drawing = false;
	}
	
	public static void flush() {
		if (numVertices > 0) {
			vertices.flip();
			normals.flip();
			uvs.flip();
			glBindVertexArray(vao);
			//program.use();
			
			int time = glGetUniformLocation(shader, "time");
			glUniform1f(time, Main.time);
		
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
			
			glBindBuffer(GL_ARRAY_BUFFER, texBuffer);
			glBufferSubData(GL_ARRAY_BUFFER, 0, uvs);
			
			glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
			glBufferSubData(GL_ARRAY_BUFFER, 0, normals);
			
			
			glDrawArrays(GL_TRIANGLES, 0, numVertices);
		
			numVertices = 0;
			vertices.clear();
			uvs.clear();
			normals.clear();
		}
	}
	
	public static void loadVertexAttrib() {
		//specify vertex attributes
		int floatSize = 4;
		int posAttrib = glGetAttribLocation(shader, "position");
		glEnableVertexAttribArray(posAttrib);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 3 * floatSize, 0);
		
		int colAttrib = glGetAttribLocation(shader, "color");
		glEnableVertexAttribArray(colAttrib);
		glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);
		glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 3 * floatSize, 0);
		
		int normalAttrib = glGetAttribLocation(shader, "normal");
		glEnableVertexAttribArray(normalAttrib);
		glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
		glVertexAttribPointer(normalAttrib, 3, GL_FLOAT, false, 3 * floatSize, 0);
		
		int texAttrib = glGetAttribLocation(shader, "vertexUV");
		glEnableVertexAttribArray(texAttrib);
		glBindBuffer(GL_ARRAY_BUFFER, texBuffer);
		glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 2 * floatSize, 0);
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		
		glClearColor(0f, 0f, 1f, 1f);
	}
	
	 public static void clear() {
	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	 }
}