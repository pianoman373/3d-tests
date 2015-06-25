package opengl.main;

public class World {
	private static Chunk chunk1 = new Chunk(0, 0, 1);
	
	public static void generate() {
		chunk1.generate();
	}
	
	public static void render() {
		chunk1.render();
	}
}
