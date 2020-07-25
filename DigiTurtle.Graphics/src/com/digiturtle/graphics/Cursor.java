package com.digiturtle.graphics;

import org.lwjgl.glfw.GLFW;

public enum Cursor {
	
	ARROW(GLFW.GLFW_ARROW_CURSOR),
	
	HAND(GLFW.GLFW_HAND_CURSOR),
	
	CROSSHAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
	
	HRESIZE(GLFW.GLFW_HRESIZE_CURSOR),
	
	IBEAM(GLFW.GLFW_IBEAM_CURSOR),
	
	VRESIZE(GLFW.GLFW_VRESIZE_CURSOR),
	
	;
	
	public final long handle;
	
	Cursor(int shape) {
		this.handle = GLFW.glfwCreateStandardCursor(shape);
	}

}
