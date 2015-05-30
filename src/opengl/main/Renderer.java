package opengl.main;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Renderer {
	private static FloatBuffer vertices;
	private static int numVertices;
	private static boolean drawing;
	private static int vbo;
	private static int vao;
	public static int colorBuffer;
	public static int texBuffer;
	public static int normalBuffer;
	private static int shader;
	
	public static void init() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		vertices = BufferUtils.createFloatBuffer(4096);
		
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
		FloatBuffer texCoords = BufferUtils.createFloatBuffer(36 * 2);
		texCoords.put(Constants.texCoords);
		texCoords.flip();
		texBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texBuffer);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		
		//normals
		FloatBuffer normals = BufferUtils.createFloatBuffer(36 * 3);
		normals.put(Constants.normals);
		normals.flip();
		normalBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		
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
		vertices.put(x).put(y).put(z);
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
			glBindVertexArray(vao);
			//program.use();
		
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
			glDrawArrays(GL_TRIANGLES, 0, numVertices);
		
			vertices.clear();
			numVertices = 0;
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
		
		glClearColor(0f, 0f, 0.4f, 1f);
	}
	
	 public static void clear() {
	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	 }
}