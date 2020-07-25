package com.digiturtle.input;

import org.lwjgl.glfw.GLFW;

public class GLFWKeyMapper implements InputProcessor.KeyMapper {

	public String mapKeycode(int keycode, boolean shiftUpper) {
		switch (keycode) {
		// Numbers
		case GLFW.GLFW_KEY_0: return shiftUpper ? ")" : "0";
		case GLFW.GLFW_KEY_1: return shiftUpper ? "!" : "1";
		case GLFW.GLFW_KEY_2: return shiftUpper ? "@" : "2";
		case GLFW.GLFW_KEY_3: return shiftUpper ? "#" : "3";
		case GLFW.GLFW_KEY_4: return shiftUpper ? "$" : "4";
		case GLFW.GLFW_KEY_5: return shiftUpper ? "%" : "5";
		case GLFW.GLFW_KEY_6: return shiftUpper ? "^" : "6";
		case GLFW.GLFW_KEY_7: return shiftUpper ? "&" : "7";
		case GLFW.GLFW_KEY_8: return shiftUpper ? "*" : "8";
		case GLFW.GLFW_KEY_9: return shiftUpper ? "(" : "9";
		// Letters
		case GLFW.GLFW_KEY_A: return shiftUpper ? "A" : "a";
		case GLFW.GLFW_KEY_B: return shiftUpper ? "B" : "b";
		case GLFW.GLFW_KEY_C: return shiftUpper ? "C" : "c";
		case GLFW.GLFW_KEY_D: return shiftUpper ? "D" : "d";
		case GLFW.GLFW_KEY_E: return shiftUpper ? "E" : "e";
		case GLFW.GLFW_KEY_F: return shiftUpper ? "F" : "f";
		case GLFW.GLFW_KEY_G: return shiftUpper ? "G" : "g";
		case GLFW.GLFW_KEY_H: return shiftUpper ? "H" : "h";
		case GLFW.GLFW_KEY_I: return shiftUpper ? "I" : "i";
		case GLFW.GLFW_KEY_J: return shiftUpper ? "J" : "j";
		case GLFW.GLFW_KEY_K: return shiftUpper ? "K" : "k";
		case GLFW.GLFW_KEY_L: return shiftUpper ? "L" : "l";
		case GLFW.GLFW_KEY_M: return shiftUpper ? "M" : "m";
		case GLFW.GLFW_KEY_N: return shiftUpper ? "N" : "n";
		case GLFW.GLFW_KEY_O: return shiftUpper ? "O" : "o";
		case GLFW.GLFW_KEY_P: return shiftUpper ? "P" : "p";
		case GLFW.GLFW_KEY_Q: return shiftUpper ? "Q" : "q";
		case GLFW.GLFW_KEY_R: return shiftUpper ? "R" : "r";
		case GLFW.GLFW_KEY_S: return shiftUpper ? "S" : "s";
		case GLFW.GLFW_KEY_T: return shiftUpper ? "T" : "t";
		case GLFW.GLFW_KEY_U: return shiftUpper ? "U" : "u";
		case GLFW.GLFW_KEY_V: return shiftUpper ? "V" : "v";
		case GLFW.GLFW_KEY_W: return shiftUpper ? "W" : "w";
		case GLFW.GLFW_KEY_X: return shiftUpper ? "X" : "x";
		case GLFW.GLFW_KEY_Y: return shiftUpper ? "Y" : "y";
		case GLFW.GLFW_KEY_Z: return shiftUpper ? "Z" : "z";
		// Punctuation
		case GLFW.GLFW_KEY_LEFT_BRACKET: return shiftUpper ? "{" : "[";
		case GLFW.GLFW_KEY_RIGHT_BRACKET: return shiftUpper ? "}" : "]";
		case GLFW.GLFW_KEY_BACKSLASH: return shiftUpper ? "|" : "\\";
		case GLFW.GLFW_KEY_SEMICOLON: return shiftUpper ? ":" : ";";
		case GLFW.GLFW_KEY_APOSTROPHE: return shiftUpper ? "\"" : "'";
		case GLFW.GLFW_KEY_COMMA: return shiftUpper ? "<" : ",";
		case GLFW.GLFW_KEY_PERIOD: return shiftUpper ? ">" : ".";
		case GLFW.GLFW_KEY_SLASH: return shiftUpper ? "?" : "/";
		case GLFW.GLFW_KEY_MINUS: return shiftUpper ? "_" : "-";
		case GLFW.GLFW_KEY_EQUAL: return shiftUpper ? "+" : "=";
		case GLFW.GLFW_KEY_GRAVE_ACCENT: return shiftUpper ? "~" : "`";
		// Num Pad
		case GLFW.GLFW_KEY_KP_0: return shiftUpper ? null : "0";
		case GLFW.GLFW_KEY_KP_1: return shiftUpper ? null : "1";
		case GLFW.GLFW_KEY_KP_2: return shiftUpper ? null : "2";
		case GLFW.GLFW_KEY_KP_3: return shiftUpper ? null : "3";
		case GLFW.GLFW_KEY_KP_4: return shiftUpper ? null : "4";
		case GLFW.GLFW_KEY_KP_5: return shiftUpper ? null : "5";
		case GLFW.GLFW_KEY_KP_6: return shiftUpper ? null : "6";
		case GLFW.GLFW_KEY_KP_7: return shiftUpper ? null : "7";
		case GLFW.GLFW_KEY_KP_8: return shiftUpper ? null : "8";
		case GLFW.GLFW_KEY_KP_9: return shiftUpper ? null : "9";
		case GLFW.GLFW_KEY_KP_DIVIDE: return shiftUpper ? null : "/";
		case GLFW.GLFW_KEY_KP_MULTIPLY: return shiftUpper ? null : "*";
		case GLFW.GLFW_KEY_KP_SUBTRACT: return shiftUpper ? null : "-";
		case GLFW.GLFW_KEY_KP_ADD: return shiftUpper ? null : "+";
		case GLFW.GLFW_KEY_KP_DECIMAL: return shiftUpper ? null : ".";
		// Function Keys
		case GLFW.GLFW_KEY_F1: return "+F1";
		case GLFW.GLFW_KEY_F2: return "+F2";
		case GLFW.GLFW_KEY_F3: return "+F3";
		case GLFW.GLFW_KEY_F4: return "+F4";
		case GLFW.GLFW_KEY_F5: return "+F5";
		case GLFW.GLFW_KEY_F6: return "+F6";
		case GLFW.GLFW_KEY_F7: return "+F7";
		case GLFW.GLFW_KEY_F8: return "+F8";
		case GLFW.GLFW_KEY_F9: return "+F9";
		case GLFW.GLFW_KEY_F10: return "+F10";
		case GLFW.GLFW_KEY_F11: return "+F11";
		case GLFW.GLFW_KEY_F12: return "+F12";
		// Action Keys
		case GLFW.GLFW_KEY_TAB: return "+TAB";
		case GLFW.GLFW_KEY_LEFT_SHIFT: return "+LEFT_SHIFT";
		case GLFW.GLFW_KEY_CAPS_LOCK: return "+CAPS_LOCK";
		case GLFW.GLFW_KEY_LEFT_CONTROL: return "+LEFT_CONTROL";
		case GLFW.GLFW_KEY_LEFT_ALT: return "+LEFT_ALT";
		case GLFW.GLFW_KEY_RIGHT_ALT: return "+RIGHT_ALT";
		case GLFW.GLFW_KEY_RIGHT_CONTROL: return "+RIGHT_CONTROL";
		case GLFW.GLFW_KEY_RIGHT_SHIFT: return "+RIGHT_SHIFT";
		case GLFW.GLFW_KEY_ENTER: return "+ENTER";
		case GLFW.GLFW_KEY_BACKSPACE: return "+BACKSPACE";
		case GLFW.GLFW_KEY_INSERT: return "+INSERT";
		case GLFW.GLFW_KEY_PRINT_SCREEN: return "+PRINT_SCREEN";
		case GLFW.GLFW_KEY_DELETE: return "+DELETE";
		case GLFW.GLFW_KEY_PAGE_UP: return "+PAGE_UP";
		case GLFW.GLFW_KEY_PAGE_DOWN: return "+PAGE_DOWN";
		case GLFW.GLFW_KEY_HOME: return "+HOME";
		case GLFW.GLFW_KEY_END: return "+END";
		// Others
		case GLFW.GLFW_KEY_LEFT: return "+LEFT";
		case GLFW.GLFW_KEY_RIGHT: return "+RIGHT";
		case GLFW.GLFW_KEY_UP: return "+UP";
		case GLFW.GLFW_KEY_DOWN: return "+DOWN";
		case GLFW.GLFW_KEY_SPACE: return " ";
		case GLFW.GLFW_KEY_ESCAPE: return "+ESCAPE";
		}
		return null;
	}
	
}
