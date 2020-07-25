package com.digiturtle.graphics;

import org.lwjgl.glfw.GLFW;

import com.digiturtle.input.InputProcessor;

public class GLFWInput {

	public static void connect(final long window, final InputProcessor processor) {
		GLFW.glfwSetCharCallback(window, (pointer, codepoint) -> {
			if (window == pointer) {
				processor.characterTyped(codepoint);
			}
		});
		GLFW.glfwSetCursorPosCallback(window, (pointer, x, y) -> {
			if (window == pointer) {
				processor.cursorPos(x, y);
			}
		});
		GLFW.glfwSetFramebufferSizeCallback(window, (pointer, width, height) -> {
			if (window == pointer) {
				// Ignore for now
			}
		});
		GLFW.glfwSetJoystickCallback((jid, event) -> {
			// Ignore for now
		});
		GLFW.glfwSetKeyCallback(window, (pointer, key, scancode, action, mods) -> {
			if (window == pointer) {
				processor.key(key, scancode, action, mods);
			}
		});
		GLFW.glfwSetMouseButtonCallback(window, (pointer, button, action, mods) -> {
			if (window == pointer) {
				processor.mouseButton(button, action, mods);
			}
		});
		GLFW.glfwSetScrollCallback(window, (pointer, dx, dy) -> {
			if (window == pointer) {
				processor.scroll(dx, dy);
			}
		});
	}

}
