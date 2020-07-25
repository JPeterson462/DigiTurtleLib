package com.digiturtle.math;

import org.joml.Vector2d;

public class Rectangle extends Shape {
	
	public double x, y, width, height;

	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(Vector2d position, Vector2d size) {
		x = position.x;
		y = position.y;
		width = size.x;
		height = size.y;
	}
	
	public Rectangle(Shape bounds) {
		Vector2d topLeft = new Vector2d(bounds.getCenter()).fma(-0.5, new Vector2d(bounds.getWidth(), bounds.getHeight()));
		x = topLeft.x;
		y = topLeft.y;
		width = bounds.getWidth();
		height = bounds.getHeight();
	}

	public Rectangle center() {
		x = x - width * 0.5;
		y = y - height * 0.5;
		return this;
	}
	
	public boolean insideOf(Shape shape) {
		// All shapes are assumed to be convex polygons (or ellipses), so just check if all four corners are contained in shape
		return shape.contains(x, y) && shape.contains(x + width, y) &&
				shape.contains(x, y + height) && shape.contains(x + width, y + height);
	}

	public boolean contains(double px, double py) {
		return MathUtils.valueInRange(px - x, 0, width) && MathUtils.valueInRange(py - y, 0, height);
	}
	
	public void resize(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	public String toString() {
		return "Rectangle[" + x + ", " + y + " of " + width + "x" + height + "]";
	}

	public Vector2d getXY() {
		return new Vector2d(x, y);
	}

	@Override
	public Vector2d getCenter() {
		return new Vector2d(x + 0.5 * width, y + 0.5 * height);
	}

	public boolean overlaps(Shape shape) {
		// All shapes are assumed to be convex polygons (or ellipses), so just check if any of the four corners are contained in shape
		return shape.contains(x, y) || shape.contains(x + width, y) ||
				shape.contains(x, y + height) || shape.contains(x + width, y + height);
	}

}
