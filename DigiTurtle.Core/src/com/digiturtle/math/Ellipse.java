package com.digiturtle.math;

import org.joml.Vector2d;

public class Ellipse extends Shape {
	
	// ((x - x0) / a)^2 + ((y - y0) / b)^2 = 1
	
	private double cx, cy, a, b;
	
	public void setCenter(double x, double y) {
		cx = x;
		cy = y;
	}
	
	public void setRadius(double xAxis, double yAxis) {
		a = xAxis;
		b = yAxis;
	}

	@Override
	public double getWidth() {
		return 2 * a;
	}

	@Override
	public double getHeight() {
		return 2 * b;
	}

	@Override
	public boolean contains(double px, double py) {
		double dx = (px - cx) / a;
		double dy = (py - cy) / b;
		return dx * dx + dy * dy <= 1;
	}

	@Override
	public Vector2d getCenter() {
		return new Vector2d(cx, cy);
	}

	@Override
	public void translate(double dx, double dy) {
		cx += dx;
		cy += dy;
	}

	@Override
	public void move(double x, double y) {
		cx = x;
		cy = y;
	}

}
