package com.digiturtle.input;

import org.lwjgl.glfw.GLFW;

import com.digiturtle.input.InputAdapter.MouseButton;

public class InputProcessor {
	
	public interface KeyMapper {
		
		public String mapKeycode(int keycode, boolean shiftUpper);
		
	}
	
	private InputAdapter adapter;
	
	private double x, y;
	
	private KeyMapper mapper;

	public InputProcessor(InputAdapter adapter) {
		this.adapter = adapter;
		mapper = new GLFWKeyMapper();
	}
	
	public void characterTyped(int letter) {
		adapter.characterTyped(letter);
	}
	
	public void cursorPos(double x, double y) {
		this.x = x;
		this.y = y;
		adapter.mouseMoved(x, y);
	}
	
	private int convertModifiers(int in) {
		int out = 0;
		out |= (in & GLFW.GLFW_MOD_ALT) != 0 ? InputAdapter.ALT : 0;
		out |= (in & GLFW.GLFW_MOD_CAPS_LOCK) != 0 ? InputAdapter.CAPS_LOCK : 0;
		out |= (in & GLFW.GLFW_MOD_CONTROL) != 0 ? InputAdapter.CTRL : 0;
		out |= (in & GLFW.GLFW_MOD_NUM_LOCK) != 0 ? InputAdapter.NUM_LOCK : 0;
		out |= (in & GLFW.GLFW_MOD_SHIFT) != 0 ? InputAdapter.SHIFT : 0;
		return out;
	}
	
	public void key(int key, int scancode, int action, int mods) {
		boolean shift = (mods & GLFW.GLFW_MOD_SHIFT) != 0;
		boolean capsLock = (mods & GLFW.GLFW_MOD_CAPS_LOCK) != 0;
		boolean shiftUpper = shift ^ capsLock;
		if (action == GLFW.GLFW_PRESS) {
			adapter.keyDown(mapper.mapKeycode(key, shiftUpper), convertModifiers(mods));
		}
		else if (action == GLFW.GLFW_REPEAT) {
			adapter.keyTyped(mapper.mapKeycode(key, shiftUpper), convertModifiers(mods));
		}
		else if (action == GLFW.GLFW_RELEASE) {
			adapter.keyTyped(mapper.mapKeycode(key, shiftUpper), convertModifiers(mods));
			adapter.keyUp(mapper.mapKeycode(key, shiftUpper), convertModifiers(mods));
		}
	}
	
	public void mouseButton(int button, int action, int mods) {
		if (action == GLFW.GLFW_PRESS) { 
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				adapter.mouseDown(MouseButton.LEFT, x, y);
			}
			else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				adapter.mouseDown(MouseButton.RIGHT, x, y);
			}
		}
		else if (action == GLFW.GLFW_RELEASE) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				adapter.click(MouseButton.LEFT, x, y, convertModifiers(mods));
				adapter.mouseUp(MouseButton.LEFT, x, y);
			}
			else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				adapter.click(MouseButton.RIGHT, x, y, convertModifiers(mods));
				adapter.mouseUp(MouseButton.RIGHT, x, y);
			}
		}
	}
	
	public void scroll(double dx, double dy) {
		adapter.mouseScroll(dx, dy);
	}

}
