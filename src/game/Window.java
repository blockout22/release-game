package game;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
	
	public static long window;
	
	private static int WIDTH;
	private static int HEIGHT;
	private static String title;
	
	private static long lastFPStime;
	private static long variableYieldTime;
	private static long lastTime;
	private static int fps;
	private static int fps_buffer;
	
	public static void createWindow()
	{
		createWindow(800, 600, "Game", true);
	}
	
	public static void createWindow(int width, int height) {
		createWindow(width, height, "Game", true);
	}

	public static void createWindow(int width, int height, String title) {
		createWindow(width, height, title, true);
	}

	public static void createWindow(int width, int height, boolean vsync) {
		createWindow(width, height, "Game", vsync);
	}
	
	public static void createWindow(int width, int height, String title, boolean vsync){
		Window.WIDTH = width;
		Window.HEIGHT = height;
		
		if(!GLFW.glfwInit()){
			throw new IllegalStateException();
		}
		
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		
		if(window == MemoryUtil.NULL){
			System.err.println("Window returned NULL");
			System.exit(-1);
			return;
		}
		
		GLFW.glfwMakeContextCurrent(window);
		
		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
		GL11.glClearColor(0, 1, 0, 1);
		GL11.glViewport(0, 0, width, height);
		lastFPStime = Time.getTime();
	}
	
	public static boolean isCloseRequested(){
		boolean closeStage = GLFW.glfwWindowShouldClose(window);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		Time.setDelta();
		if(Time.getTime() - lastFPStime >= 1000){
			lastFPStime += 1000;
			fps = fps_buffer;
			fps_buffer = 0;
		}
		fps_buffer++;
		return closeStage;
	}
	
	public static void sync(int fps){
		if(fps <= 0){
			return;
		}
		
		long sleepTime = 1000000000 / fps;
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000));
		long overSleep = 0;
		
		try {
			while(true){
				long t = System.nanoTime() - lastTime;
				
				if(t < sleepTime - yieldTime){
					Thread.sleep(1);
				}else if(t < sleepTime){
					Thread.yield();
				}else{
					overSleep = t - sleepTime;
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
			
			if(overSleep > variableYieldTime){
				variableYieldTime = Math.min(variableYieldTime + 200 * 1000, sleepTime);
			}else if(overSleep < variableYieldTime - 200 * 1000){
				variableYieldTime = Math.max(variableYieldTime - 2 * 1000, 0);
			}
		}
	}
	
	public static int getWidth() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, width, height);
		Window.WIDTH = width.get();

		return Window.WIDTH;
	}

	public static int getHeight() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, width, height);
		Window.HEIGHT = height.get();

		return Window.HEIGHT;
	}
	
	public static int getFPS()
	{
		return fps;
	}
	
	public static void setTitle(String title){
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public static void update(){
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public static void enableDepthBuffer(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void disableDepthBuffer() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public static void enableVSync() {
		GLFW.glfwSwapInterval(1);
	}
	
	public static void disableVSync() {
		GLFW.glfwSwapBuffers(0);
	}

	public static void close() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

}
