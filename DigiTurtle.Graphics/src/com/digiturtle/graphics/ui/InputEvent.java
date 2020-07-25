package com.digiturtle.graphics.ui;

import com.digiturtle.input.InputAdapter.MouseButton;
import com.digiturtle.math.Shape;

public abstract class InputEvent {
	
	public interface ShiftEvent {
		
	}

	public abstract boolean containedIn(Shape shape);

	public abstract InputEvent translate(double offsetX, double offsetY);
	
	public abstract InputEvent scale(double scale);

	public static class ScrollEvent extends InputEvent {
		
		public final double dx, dy;
		
		public ScrollEvent(double dx, double dy) {
			this.dx = dx;
			this.dy = dy;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return true;
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new ScrollEvent(dx, dy);
		}
		
		@Override
		public InputEvent scale(double scale) {
			return new ScrollEvent(dx * scale, dy * scale);
		}
		
	}
	
	public static class ClickEvent extends InputEvent {
		
		public final double x, y;
		
		public final MouseButton button;
		
		public final int mods;
		
		public ClickEvent(MouseButton button, double x, double y, int mods) {
			this.button = button;
			this.x = x;
			this.y = y;
			this.mods = mods;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(x, y);
		}
		
		public String toString() {
			return "ClickEvent[(" + x + ", " + y + "); " + button.toString() + "]";
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new ClickEvent(button, x + offsetX, y + offsetY, mods);
		}

		@Override
		public InputEvent scale(double scale) {
			return new ClickEvent(button, x * scale, y * scale, mods);
		}
		
	}
	
	public static class DragStartEvent extends InputEvent implements ShiftEvent {
		
		public final double x, y;

		public final MouseButton button;
		
		public DragStartEvent(MouseButton button, double x, double y) {
			this.button = button;
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(x, y);
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new DragStartEvent(button, x + offsetX, y + offsetY);
		}

		@Override
		public InputEvent scale(double scale) {
			return new DragStartEvent(button, x * scale, y * scale);
		}

		public String toString() {
			return "DragStartEvent[x = " + x + ", y = " + y + ", " + button + "]";
		}
		
	}
	
	public static class DragEvent extends InputEvent implements ShiftEvent {
		
		public final double ix, iy;

		public final double dx, dy;
		
		public final double cx, cy;

		public final MouseButton button;
		
		public DragEvent(MouseButton button, double ix, double iy, double dx, double dy, double cx, double cy) {
			this.button = button;
			this.ix = ix;
			this.iy = iy;
			this.dx = dx;
			this.dy = dy;
			this.cx = cx;
			this.cy = cy;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(ix, iy);
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new DragEvent(button, ix + offsetX, iy + offsetY, dx, dy, cx + offsetX, cy + offsetY);
		}

		@Override
		public InputEvent scale(double scale) {
			return new DragEvent(button, ix * scale, iy * scale, dx * scale, dy * scale, cx * scale, cy * scale);
		}
		
		public String toString() {
			return "DragEvent[ix = " + ix + ", iy = " + iy + ", dx = " + dx + ", dy = " + dy + ", button = " + button + "]";
		}
		
	}
	
	public static class DragStopEvent extends InputEvent implements ShiftEvent {

		public final double x, y;

		public final MouseButton button;
		
		public DragStopEvent(MouseButton button, double x, double y) {
			this.button = button;
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(x, y);
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new DragStopEvent(button, x + offsetX, y + offsetY);
		}

		@Override
		public InputEvent scale(double scale) {
			return new DragStopEvent(button, x * scale, y * scale);
		}
		
		public String toString() {
			return "DragStopEvent[x = " + x + ", y = " + y + ", " + button + "]";
		}
		
	}
	
	public static class MouseMoveEvent extends InputEvent {
		
		public final double x, y;
		
		public MouseMoveEvent(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(x, y);
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new MouseMoveEvent(x + offsetX, y + offsetY);
		}

		@Override
		public InputEvent scale(double scale) {
			return new MouseMoveEvent(x * scale, y * scale);
		}
		
	}
	
	public static class MouseButtonEvent extends InputEvent {
		
		public final MouseButton button;
		
		public final boolean down;
		
		public final double x, y;
		
		public MouseButtonEvent(MouseButton button, boolean down, double x, double y) {
			this.button = button;
			this.down = down;
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return shape.contains(x, y);
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new MouseButtonEvent(button, down, x + offsetX, y + offsetY);
		}

		@Override
		public InputEvent scale(double scale) {
			return new MouseButtonEvent(button, down, x * scale, y * scale);
		}
		
	}
	
	public static class KeyEvent extends InputEvent {
		
		public final String text;
		
		public final int mods;
		
		public KeyEvent(String text, int mods) {
			this.text = text;
			this.mods = mods;
		}

		@Override
		public boolean containedIn(Shape shape) {
			return true;
		}

		@Override
		public InputEvent translate(double offsetX, double offsetY) {
			return new KeyEvent(text, mods);
		}

		@Override
		public InputEvent scale(double scale) {
			return new KeyEvent(text, mods);
		}
		
	}
	
}
