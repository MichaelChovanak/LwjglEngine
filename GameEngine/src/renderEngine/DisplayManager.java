package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPSCap = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3,3)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setLocation(0, 0);
			Display.create(new PixelFormat().withSamples(8), attribs);
			Display.setTitle("LWJGL 2 Game");
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		Mouse.setGrabbed(true);
		GL11.glViewport(0,0,WIDTH,HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay()
	{
		Display.sync(FPSCap);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay()
	{
		Display.destroy();
	}
	
	public static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
