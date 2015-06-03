package opengl.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ModelLoader {
	public static void renderObj(String file) {
		Path path = Paths.get(file);
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> uvs = new ArrayList<Vector3f>();
		
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
						vertices.add(new Vector3f(x, y, z));
					}
					else if (line.startsWith("vn ")) {
						String[] values = line.split(" ");
						float x = Float.valueOf(values[1]);
						float y = Float.valueOf(values[2]);
						float z = Float.valueOf(values[3]);
						normals.add(new Vector3f(x, y, z));
					}
					else if (line.startsWith("vt ")) {
						String[] values = line.split(" ");
						float u = Float.valueOf(values[1]);
						float v = Float.valueOf(values[2]);
						uvs.add(new Vector3f(u, 0, v));
					}
					else if (line.startsWith("f ")) {
						String[] triangles = line.split(" ");
						for (int i = 1; i < 4; i++) {
							String[] index = triangles[i].split("/");
							
							Vector3f indexVertex = vertices.get(Integer.valueOf(index[0]) - 1);
							Vector3f indexUV = uvs.get(Integer.valueOf(index[1]) - 1);
							Vector3f indexNormal = normals.get(Integer.valueOf(index[2]) - 1);
							Renderer.addVertexWithUVNormal(indexVertex.x, indexVertex.y, indexVertex.z, indexUV.x, indexUV.z, indexNormal.x, indexNormal.y, indexNormal.z);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
