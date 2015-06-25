package opengl.main;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

public class Input {
	private static float movementSpeed = 0.5f;
	
	public static void handleInput() {
		double radians = Math.toRadians(Main.xRot);
		if (glfwGetKey(Main.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
    		glfwSetWindowShouldClose(Main.window, GL_TRUE);
    	}
		if (glfwGetKey( Main.window, GLFW_KEY_W ) == GLFW_PRESS) {
			Main.xPos -= Math.sin(radians) * movementSpeed;
			Main.zPos += Math.cos(radians) * movementSpeed;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_S ) == GLFW_PRESS) {
			Main.xPos += Math.sin(radians) * movementSpeed;
			Main.zPos -= Math.cos(radians) * movementSpeed;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_A ) == GLFW_PRESS) {
			Main.zPos += Math.sin(radians) * movementSpeed;
			Main.xPos += Math.cos(radians) * movementSpeed;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_D ) == GLFW_PRESS) {
			Main.zPos -= Math.sin(radians) * movementSpeed;
			Main.xPos -= Math.cos(radians) * movementSpeed;
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
		if (glfwGetKey( Main.window, GLFW_KEY_SPACE ) == GLFW_PRESS) {
			Main.yPos -= 0.5;
		}
		if (glfwGetKey( Main.window, GLFW_KEY_LEFT_SHIFT ) == GLFW_PRESS) {
			Main.yPos += 0.5;
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
