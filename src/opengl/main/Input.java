package opengl.main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

public class Input {
	public static void handleInput() {
		double radians = Math.toRadians(Main.xRot);
		if (glfwGetKey(Main.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
    		glfwSetWindowShouldClose(Main.window, GL_TRUE);
    	}
		if (glfwGetKey( Main.window, GLFW_KEY_W ) == GLFW_PRESS) {
			Main.xPos -= Math.sin(radians) * 0.1;
			Main.zPos += Math.cos(radians) * 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_S ) == GLFW_PRESS) {
			Main.xPos += Math.sin(radians) * 0.1;
			Main.zPos -= Math.cos(radians) * 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_A ) == GLFW_PRESS) {
			Main.zPos += Math.sin(radians) * 0.1;
			Main.xPos += Math.cos(radians) * 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_D ) == GLFW_PRESS) {
			Main.zPos -= Math.sin(radians) * 0.1;
			Main.xPos -= Math.cos(radians) * 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_R ) == GLFW_PRESS) {
			Main.yPos -= 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_F ) == GLFW_PRESS) {
			Main.yPos += 0.1;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_LEFT ) == GLFW_PRESS) {
			Main.time += 0.5;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_RIGHT ) == GLFW_PRESS) {
			Main.time -= 0.5;
		}
		if (glfwGetMouseButton(Main.window, 0) == GLFW_PRESS) {
			float mouseSpeed = 0.04f;
			DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
			glfwGetCursorPos(Main.window, xpos, ypos);
			double mouseX = xpos.get();
			double mouseY = ypos.get();
			Main.xRot -= mouseSpeed * (640/2 - mouseX);
			Main.yRot -= mouseSpeed * (480/2 - mouseY);
			glfwSetCursorPos(Main.window, 640/2, 480/2);
			glfwSetInputMode(Main.window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		}
		if (glfwGetMouseButton(Main.window, 0) == GLFW_RELEASE) {
			glfwSetInputMode(Main.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
}
