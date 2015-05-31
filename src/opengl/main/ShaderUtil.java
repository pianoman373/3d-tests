package opengl.main;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ShaderUtil 
{	
	public static int loadShader(String vshPath, String fshPath) {
		String vertexSource = loadShaderFile(vshPath);
		String fragmentSource = loadShaderFile(fshPath);
		
		//compile shaders
				int vertexShader = glCreateShader(GL_VERTEX_SHADER);
				glShaderSource(vertexShader, vertexSource);
				glCompileShader(vertexShader);

				int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
				glShaderSource(fragmentShader, fragmentSource);
				glCompileShader(fragmentShader);
				
				//check shaders
				int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
				if (status != GL_TRUE) {
				    throw new RuntimeException(glGetShaderInfoLog(vertexShader));
				}
				int status2 = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
				if (status2 != GL_TRUE) {
				    throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
				}
				
				//create shader program
				int shaderProgram = glCreateProgram();
				glAttachShader(shaderProgram, vertexShader);
				glAttachShader(shaderProgram, fragmentShader);
				glBindFragDataLocation(shaderProgram, 0, "fragColor");
				glLinkProgram(shaderProgram);
				glUseProgram(shaderProgram);
				
				
				
				//specify uniforms
				Main.uniModel = glGetUniformLocation(shaderProgram, "model");
				Matrix4f model = new Matrix4f();
				glUniformMatrix4fv(Main.uniModel, false, model.getBuffer());

				Main.uniView = glGetUniformLocation(shaderProgram, "view");
				Matrix4f view = new Matrix4f();
				glUniformMatrix4fv(Main.uniView, false, view.getBuffer());

				Main.uniProjection = glGetUniformLocation(shaderProgram, "projection");
				Matrix4f projection = Matrix4f.perspective(45, 4f / 3f, 0.1f, 1000f);
				glUniformMatrix4fv(Main.uniProjection, false, projection.getBuffer());
				
				int time = glGetUniformLocation(shaderProgram, "time");
				glUniform1f(Main.uniProjection, Main.time);
				
				return shaderProgram;
	}
	
	public static String loadShaderFile(String path) {
		String shader = "";
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(path));
			String line = null;
			while ((line = reader.readLine()) != null) {
				shader += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return shader;
	}
}
