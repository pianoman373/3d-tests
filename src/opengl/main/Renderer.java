package opengl.main;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;

public class Renderer {
	private static FloatBuffer vertices;
	private static FloatBuffer normals;
	private static FloatBuffer uvs;
	private static ShortBuffer indices;
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
		uvs = BufferUtils.createFloatBuffer(4096);
		texBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texBuffer);
		glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW);
		
		//normals
		normalBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
		normals = BufferUtils.createFloatBuffer(4096);
		long normalSize = normals.capacity() * Float.BYTES;
		glBufferData(GL_ARRAY_BUFFER, normalSize, GL_STREAM_DRAW);
		
		indices = BufferUtils.createShortBuffer(4096);
		indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
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
		vertices.put(x).put(y).put(z);
		uvs.put(u).put(v);
		normals.put(0).put(1).put(0);
		indices.put((short)numVertices);
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
			indices.flip();
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
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
			
			glDrawElements(GL_TRIANGLES, numVertices, GL_UNSIGNED_SHORT, 0);
		
			numVertices = 0;
			vertices.clear();
			indices.clear();
			uvs.clear();
			normals.clear();
		}
	}
	
	public static void renderObj(String file) {
		Path path = Paths.get(file);
		normals.put(Constants.normals);
		
		if (Files.exists(path)) 
		{
			try {
				BufferedReader in = Files.newBufferedReader(path);
				String line = null;
				
				while ((line = in.readLine()) != null) 
				{
					if (line.startsWith("v ")) {
						String[] values = line.split(" ");
						float x = Float.valueOf(values[1]);
						float y = Float.valueOf(values[2]);
						float z = Float.valueOf(values[3]);
						vertices.put(x);
						vertices.put(y);
						vertices.put(z);
						numVertices += 3;
					}
					if (line.startsWith("vn ")) {
						String[] values = line.split(" ");
						float x = Float.valueOf(values[1]);
						float y = Float.valueOf(values[2]);
						float z = Float.valueOf(values[3]);
						//normals.put(x);
						//normals.put(y);
						//normals.put(z);
					}
					else if (line.startsWith("f ")) {
						String[] triangles = line.split(" ");
						for (int i = 1; i < 4; i++) {
							String[] index = triangles[i].split("/");
							indices.put((short)(Short.valueOf(index[0])-1));
							//indices.put((short)(Short.valueOf(index[1])-1));
							//indices.put((short)(Short.valueOf(index[0])-1));
						}
					}
				}
				vertices.flip();
				indices.flip();
				normals.flip();
				
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
				
				//glBindBuffer(GL_ARRAY_BUFFER, normalBuffer);
				//glBufferSubData(GL_ARRAY_BUFFER, 0, normals);
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
				glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
				
				glDrawElements(GL_TRIANGLES, 10000, GL_UNSIGNED_SHORT, 0);
				
				vertices.clear();
				indices.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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