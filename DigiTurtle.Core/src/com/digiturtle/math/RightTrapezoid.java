package com.digiturtle.math;

import org.joml.Vector2d;

public class RightTrapezoid extends Shape {
	
	private double x, y;
	
	private double height, topWidth, bottomWidth;
	
	public RightTrapezoid(double x, double y, double height, double topWidth, double bottomWidth) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.topWidth = topWidth;
		this.bottomWidth = bottomWidth;
	}
	
	public String toString() {
		return "RightTrapezoid[(" + x + ", " + y + ") " + height + "x" + "{" + topWidth + ", " + bottomWidth + "}]";
	}

	@Override
	public double getWidth() {
		return Math.max(topWidth, bottomWidth);
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
	}

	@Override
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean contains(double px, double py) {
		px -= x;
		py -= y;
		return MathUtils.valueInRange(py, 0, height) && MathUtils.valueInRange(px, 0, Math.max(topWidth, bottomWidth));
	}

	@Override
	public Vector2d getCenter() {
		return new Vector2d(x + (topWidth + bottomWidth) * 0.25, y + height * 0.5);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getTopWidth() {
		return topWidth;
	}

	public double getBottomWidth() {
		return bottomWidth;
	}

}
