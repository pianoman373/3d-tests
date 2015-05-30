package opengl.main;

import java.nio.file.Paths;

public class Launcher {
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", Paths.get("lwjgl/native").toAbsolutePath().toString());
		Main.runGame();
	}
}
