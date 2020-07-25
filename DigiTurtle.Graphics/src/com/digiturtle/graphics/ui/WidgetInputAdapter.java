package com.digiturtle.graphics.ui;

import com.digiturtle.core.Logger;
import com.digiturtle.input.InputAdapter;

public class WidgetInputAdapter extends InputAdapter {
	
	private InputEventListener listener;
	
	private boolean inDragLeft = false;
	
	private double x, y, dragX, dragY, mouseDownX, mouseDownY;
	
	public WidgetInputAdapter(InputEventListener listener) {
		this.listener = listener;
	}
	
	public void mouseMoved(double x, double y) {
		double dx = x - this.x, dy = y - this.y;
		this.x = x;
		this.y = y;
		listener.processInput(new InputEvent.MouseMoveEvent(x, y));
		if (inDragLeft) {
			listener.processInput(new InputEvent.DragEvent(MouseButton.LEFT, dragX, dragY, dx, dy, x, y));
		}
	}
	
	public void click(MouseButton button, double x, double y, int mods) {
		//Logger.debug("CLICK", button, x, y);
		if (Math.abs(mouseDownX - x) > 2 || Math.abs(mouseDownY - y) > 2) {
			return; // Ignore clicks that are probably from dragging
		}
		listener.processInput(new InputEvent.ClickEvent(button, x, y, mods));
		
	}
	
	public void keyTyped(String key, int mods) {
		//Logger.debug("KEY TYPED", key);
		listener.processInput(new InputEvent.KeyEvent(key, mods));
	}
	
	public void drag(MouseButton button, double dx, double dy) {
		//Logger.debug("DRAG", dx, dy);
//		if (button == MouseButton.LEFT) { 
//			if (readyForDragLeft && !inDragLeft) {
//				inDragLeft = true;
//				listener.processInput(new InputEvent.DragStartEvent(button, x, y));
//				dragX = x;
//				dragY = y;
//			}
//			listener.processInput(new InputEvent.DragEvent(button, dragX, dragY, dx, dy));
//		}
	}
	
	public void scroll(double dx, double dy) {
		Logger.debug("SCROLL", dx, dy);
		listener.processInput(new InputEvent.ScrollEvent(dx, dy));
	}

	public void mouseUp(MouseButton button, double x, double y) {
		if (button == MouseButton.LEFT) {
			if (inDragLeft) {
				listener.processInput(new InputEvent.DragStopEvent(button, x, y));
				inDragLeft = false;
			}
		}
		listener.processInput(new InputEvent.MouseButtonEvent(button, false, x, y));
	}

	public void mouseDown(MouseButton button, double x, double y) {
		if (button == MouseButton.LEFT) {
			if (!inDragLeft) {
				inDragLeft = true;
				listener.processInput(new InputEvent.DragStartEvent(button, x, y));
				dragX = x;
				dragY = y;
			}
			mouseDownX = x;
			mouseDownY = y;
		}
		listener.processInput(new InputEvent.MouseButtonEvent(button, true, x, y));
	}
	
}
