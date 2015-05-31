package opengl.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModelLoader {
	public static void renderObj(String file) {
		Path path = Paths.get(file);
		
		if (Files.exists(path)) 
		{
			try {
				BufferedReader in = Files.newBufferedReader(path);
				String line = null;
				while ((line = in.readLine()) != null) 
				{
					if (line.startsWith("v ")) {
						//System.out.println("found a vertex");
						String[] values = line.split(" ");
						float x = Float.valueOf(values[1]);
						float y = Float.valueOf(values[2]);
						float z = Float.valueOf(values[3]);
						Renderer.addVertex(x, y, z);
						//System.out.println(String.format("vertex: %f, %f, %f", x, y, z));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	public static void main(String[] args) {
		renderObj("resources/models/sphere.obj");
	}
}
