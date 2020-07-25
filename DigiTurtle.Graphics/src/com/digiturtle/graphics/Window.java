package com.digiturtle.graphics;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.digiturtle.core.ManagedResource;
import com.digiturtle.input.InputProcessor;

public class Window extends ManagedResource {
	
	public static class WindowConfiguration {
		
		public int width, height;
		
		public String title;
		
		public boolean resizable;
		
		public float[] backgroundColor;
		
		public InputProcessor inputProcessor;
		
	}
	
	private WindowConfiguration configuration;
	
	private long window;
	
	public Window(WindowConfiguration configuration) {
		this.configuration = configuration;
		init();
	}
	
	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, configuration.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);           
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		window = GLFW.glfwCreateWindow(configuration.width, configuration.height, configuration.title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glClearColor(configuration.backgroundColor[0], configuration.backgroundColor[1], 
				configuration.backgroundColor[2], configuration.backgroundColor[3]);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GLFWInput.connect(window, configuration.inputProcessor);
	}
	
	public void show() {
		GLFW.glfwShowWindow(window);
	}
	
	public boolean isOpen() {
		return !GLFW.glfwWindowShouldClose(window);
	}
	
	public void pre() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void post() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	@Override
	public void dispose() {
		GLFW.glfwDestroyWindow(window);
	}

	@Override
	public long getPointer() {
		return window;
	}

}
