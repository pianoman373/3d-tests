package opengl.main;

import static org.lwjgl.glfw.GLFW.*;

public class TimerUtil {
	double lastLoopTime;
	float timeCount;
	int fps;
	int fpsCount;
	int ups;
	int upsCount;

	public TimerUtil() {
	    lastLoopTime = getTime();
	}
	
	public double getTime() {
	    return glfwGetTime();
	}
	
	public float getDelta() {
	    double time = getTime();
	    float delta = (float) (time - lastLoopTime);
	    lastLoopTime = time;
	    timeCount += delta;
	    return delta;
	}
	
	public void updateFPS() {
	    fpsCount++;
	}

	public void updateUPS() {
	    upsCount++;
	}
	
	public void update() {
	    if (timeCount > 1f) {
	        fps = fpsCount;
	        fpsCount = 0;

	        ups = upsCount;
	        upsCount = 0;

	        timeCount -= 1f;
	    }
	}
}
