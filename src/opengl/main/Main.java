package opengl.main;

import javax.swing.JOptionPane;

import org.lwjgl.glfw.*; 
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main extends Thread {
	
	private static GLFWErrorCallback errorCallback = Callbacks.errorCallbackPrint(System.err);
	
	public static long window;
	public static int uniModel;
	public static int uniView;
	public static int uniProjection;
	public static float xPos = -10;
	public static float yPos = -1;
	public static float zPos = -10;
	public static float xRot = 0;
	public static float yRot = 0;
	public static float time = 0;
	
	public static void runGame() {
		new Main().startGame();
	}
	
	public void startGame() {
		init();
		gameLoop();
		dispose();
	}
	
	public void gameLoop() {
	    double lastTime = glfwGetTime();
	    int nbFrames = 0;

	    while (glfwWindowShouldClose(window) != GL_TRUE) {
	    	
	    	double currentTime = glfwGetTime();
	        nbFrames++;
	        if ( currentTime - lastTime >= 1.0 ) {
	        	System.out.println(String.format("%f fps", 1.0/(1.0/(double)nbFrames)));
	            nbFrames = 0;
	            lastTime += 1.0;
	        }
	        Input.handleInput();
	        update();
	        render();
	    }
	}
	
	public void update() {
		//System.out.println(time);
	}
	
	public void render() {
		Renderer.clear();
		
		Matrix4f viewMatrix = Matrix4f.rotate(yRot, 1f, 0f, 0f).multiply(Matrix4f.rotate(xRot, 0f, 1f, 0f)).multiply(Matrix4f.translate(xPos, yPos, zPos));
	    glUniformMatrix4fv(uniView, false, viewMatrix.getBuffer());
	    
	    World.render();
	    
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void dispose() {
		//cleanup after done
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
	}
	
	public void init() {
		glfwSetErrorCallback(errorCallback);
		
		//initialize GLFW
		if (glfwInit() != GL_TRUE) {
		    throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		//create window
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
		if (window == NULL) {
			JOptionPane.showMessageDialog(null, "Your graphics driver is incompatible with openGL 3.3");
		    glfwTerminate();
		    //throw new RuntimeException("Failed to create the GLFW window");
		}
		
		//create context
		glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		
		Renderer.init();
		World.generate();
	}
}
