package com.digiturtle.graphics;

import org.joml.Vector2d;

public class Camera {
	
	private Vector2d translation = new Vector2d(0, 0);
	
	private double scale = 1;
	
	public void translate(double x, double y) {
		translation.add(x, y);
	}
	
	public Vector2d getTranslation() {
		return translation;
	}

	public void scale(double factor) {
		scale *= factor;
	}
	
	public double getScale() {
		return scale;
	}
	
}
