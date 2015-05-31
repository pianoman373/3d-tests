package opengl.main;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*; 
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main extends Thread {
	
	private static GLFWErrorCallback errorCallback = Callbacks.errorCallbackPrint(System.err);
	
	public static long window;
	public static int uniModel;
	public static int uniView;
	public static int uniProjection;
	public static float xPos = 0;
	public static float yPos = 0;
	public static float zPos = -2;
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
		System.out.println(time);
	}
	
	public void render() {
		Renderer.clear();
		
		Matrix4f viewMatrix = Matrix4f.rotate(yRot, 1f, 0f, 0f).multiply(Matrix4f.rotate(xRot, 0f, 1f, 0f)).multiply(Matrix4f.translate(xPos, yPos, zPos));
	    glUniformMatrix4fv(uniView, false, viewMatrix.getBuffer());
	    
	    Renderer.begin();
	    Renderer.addVertexWithUV(-100.0f, 0.0f, 100.0f, 0f, 10f);
	    Renderer.addVertexWithUV(100.0f, 0, 100.0f, 10f, 10f);
	    Renderer.addVertexWithUV(-100.0f, 0.0f, -100.0f, 0f, 0f);
	    
	    Renderer.addVertexWithUV(-100.0f, 0.0f, -100.0f, 0f, 0f);
	    Renderer.addVertexWithUV(100.0f, 0f, 100.0f, 10f, 10f);
	    Renderer.addVertexWithUV(100.0f, 0f, -100.0f, 10f, 0f);
	    Renderer.end();
		
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
		
		//glEnable(GL_CULL_FACE);
		
		try {
			loadTextures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadTextures() throws IOException {
		int handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);
		
		InputStream in = new FileInputStream("resources/textures/grass.png");
		BufferedImage image = ImageIO.read(in);
		
		AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
		transform.translate(0, -image.getHeight());
		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = operation.filter(image, null);
		
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		for (int y = 0; y < height; y++) {
		    for (int x = 0; x < width; x++) {
		        /* Pixel as RGBA: 0xAARRGGBB */
		        int pixel = pixels[y * width + x];

		        /* Red component 0xAARRGGBB >> (4 * 4) = 0x0000AARR */
		        buffer.put((byte) ((pixel >> 16) & 0xFF));

		        /* Green component 0xAARRGGBB >> (2 * 4) = 0x00AARRGG */
		        buffer.put((byte) ((pixel >> 8) & 0xFF));

		        /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
		        buffer.put((byte) (pixel & 0xFF));

		        /* Alpha component 0xAARRGGBB >> (6 * 4) = 0x000000AA */
		        buffer.put((byte) ((pixel >> 24) & 0xFF));
		    }
		}
		buffer.flip();
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
}
