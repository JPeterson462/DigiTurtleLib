package com.digiturtle.input;

public class InputAdapter {
	
	public enum MouseButton {
		LEFT, RIGHT
	}
	
	public static final int SHIFT = 1, CTRL = 2, ALT = 4, CAPS_LOCK = 8, NUM_LOCK = 16;
	
	public void characterTyped(int codepoint) {
		
	}
	
	public void mouseUp(MouseButton button, double x, double y) {
		
	}
	
	public void mouseMoved(double x, double y) {
		
	}

	public void mouseDown(MouseButton button, double x, double y) {
		
	}
	
	public void mouseScroll(double dx, double dy) {
		
	}
	
	public void drag(MouseButton button, double dx, double dy) {
		
	}
	
	public void click(MouseButton button, double x, double y, int mods) {
		
	}
	
	public void keyTyped(String key, int mods) {
		
	}
	
	public void keyUp(String key, int mods) {
		
	}
	
	public void keyDown(String key, int mods) {
		
	}

}
