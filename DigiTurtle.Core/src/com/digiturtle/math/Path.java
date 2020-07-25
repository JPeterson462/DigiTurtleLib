package com.digiturtle.math;

import org.joml.Vector2d;

public abstract class Path {
	
	public abstract double getLength();
	
	public abstract Vector2d getPointAtLength(double length);
	
	public Vector2d getPointAtNormalizedLength(double t) {
		return getPointAtLength(t * getLength());
	}

}
