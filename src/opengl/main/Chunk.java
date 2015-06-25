package opengl.main;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

public class Chunk {
	private static float[][] heightMap = new float[64][64];
	private static NoiseGeneratorSimplex noise = new NoiseGeneratorSimplex(new Random(100));
	private int xPos;
	private int zPos;
	private int lod;
	
	public Chunk(int x, int z, int lod) {
		xPos = x;
		zPos = z;
		this.lod = lod;
	}
	
	public void generate() {
		Random rand = new Random();
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				float height = (float) noise(new Vector3f(i + (xPos * 63), 0, j + (zPos * 63)), 3, 0.02f, 0.8f) * 10;
				heightMap[i][j] = height;
			}
		}
	}
	
	public void render() {
		generate();
	    Textures.loadTexture("resources/textures/grass.png");
	    
		for (int i = 0; i < 63 / lod; i++) {
			for (int j = 0; j < 63 / lod; j++) {
				int x = i * lod;
				int y = j * lod;
				Vector3f vec1 = new Vector3f(x, heightMap[x][y], y);
				Vector3f vec2 = new Vector3f(x, heightMap[x][y + lod], y + lod);
				Vector3f vec3 = new Vector3f(x + lod, heightMap[x + lod][y + lod], y + lod);
				Vector3f vec4 = new Vector3f(x + lod, heightMap[x + lod][y], y);
				
				Vector3f edge1 = vec3.subtract(vec2);
				Vector3f edge2 = vec1.subtract(vec2);
				Vector3f normal = edge1.cross(edge2).normalize();
				
				Renderer.begin();
			    Renderer.addVertexWithUVNormal(vec2.x + (xPos * 63), vec2.y, vec2.z + (zPos * 63), 0f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec3.x + (xPos * 63), vec3.y, vec3.z + (zPos * 63), 1f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec1.x + (xPos * 63), vec1.y, vec1.z + (zPos * 63), 0f, 0f, normal.x, normal.y, normal.z);
			    
			    edge1 = vec3.subtract(vec1);
				edge2 = vec4.subtract(vec1);
				normal = edge1.cross(edge2).normalize();
			    
			    Renderer.addVertexWithUVNormal(vec1.x + (xPos * 63), vec1.y, vec1.z + (zPos * 63), 0f, 0f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec3.x + (xPos * 63), vec3.y, vec3.z + (zPos * 63), 1f, 1f, normal.x, normal.y, normal.z);
			    Renderer.addVertexWithUVNormal(vec4.x + (xPos * 63), vec4.y, vec4.z + (zPos * 63), 1f, 0f, normal.x, normal.y, normal.z);
			    Renderer.end();
			}
		}
	}
	
	private static float noise(Vector3f position, int octaves, float frequency, float persistence) {
		float total = 0.0f;
	    float maxAmplitude = 0.0f;
	    float amplitude = 1.0f;
	    
	    for (int i = 0; i < octaves; i++) {

	        // Get the noise sample
	        total += noise.func_151605_a(position.x * frequency, position.z * frequency) * amplitude;

	        // Make the wavelength twice as small
	        frequency *= 2.0;

	        // Add to our maximum possible amplitude
	        maxAmplitude += amplitude;

	        // Reduce amplitude according to persistence for the next octave
	        amplitude *= persistence;
	    }

	    // Scale the result by the maximum amplitude
	    return total / maxAmplitude;
	}
}
