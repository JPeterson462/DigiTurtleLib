package com.digiturtle.math;

import org.joml.Vector2d;

public abstract class Shape {

	public abstract double getWidth();
	
	public abstract double getHeight();
	
	public abstract void translate(double dx, double dy);
	
	public abstract void move(double x, double y);
	
	public abstract boolean contains(double px, double py);
	
	public abstract Vector2d getCenter();
	
	public Vector2d getTopLeft() {
		return new Vector2d(getCenter()).sub(getWidth() * 0.5, getHeight() * 0.5);
	}

	public Vector2d getBottomLeft() {
		return new Vector2d(getCenter()).sub(getWidth() * 0.5, -1 * getHeight() * 0.5);
	}
	
}
