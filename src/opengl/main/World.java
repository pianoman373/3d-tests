package opengl.main;

import java.util.Random;

public class World {
	private static float[][] heightMap = new float[16][16];
	
	public static void generate() {
		Random rand = new Random();
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				float height = rand.nextFloat() / 4;
				heightMap[i][j] = height;
			}
		}
	}
	
	public static void render() {
		Renderer.begin();
	    Textures.loadTexture("resources/textures/grass.png");
	    
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				Vector3f vec1 = new Vector3f(i, heightMap[i][j], j);
				Vector3f vec2 = new Vector3f(i, heightMap[i][j + 1], j + 1);
				Vector3f vec3 = new Vector3f(i + 1, heightMap[i + 1][j + 1], j + 1);
				Vector3f vec4 = new Vector3f(i + 1, heightMap[i + 1][j], j);
				
				Vector3f edge1 = vec3.subtract(vec2);
				Vector3f edge2 = vec1.subtract(vec2);
				Vector3f normal = edge1.cross(edge2).normalize();
				
			    Renderer.addVertexWithUVNormal(vec2.x, vec2.y, vec2.z, 0f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec3.x, vec3.y, vec3.z, 1f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec1.x, vec1.y, vec1.z, 0f, 0f, normal.x, normal.y, normal.z);
			    
			    edge1 = vec3.subtract(vec1);
				edge2 = vec4.subtract(vec1);
				normal = edge1.cross(edge2).normalize();
			    
			    Renderer.addVertexWithUVNormal(vec1.x, vec1.y, vec1.z, 0f, 0f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec3.x, vec3.y, vec3.z, 1f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec4.x, vec4.y, vec4.z, 1f, 0f, normal.x, normal.y, normal.z);
			}
		}
		Renderer.end();
	}
}
