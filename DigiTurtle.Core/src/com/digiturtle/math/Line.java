package com.digiturtle.math;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;

public class Line extends Path {
	
	private Vector2d a, b;
	
	private double length, angle;
	
	public Line(Vector2d a, Vector2d b) {
		this.a = a;
		this.b = b;
		length = new Vector2d(b).sub(a).length();
		Vector2d normalized = new Vector2d(a).sub(b).normalize();
		angle = Math.atan2(normalized.y, normalized.x);
//		Logger.debug(message, data);
	}
	
	public void translate(double dx, double dy) {
		a.add(dx, dy);
		b.add(dx, dy);
	}
	
	public double getAngle() {
		return angle;
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public Vector2d getPointAtLength(double length) {
		Vector2d point = new Vector2d();
		double t = length / this.length;
		a.lerp(b, t, point);
		return point;
	}
	
	public double getAngleAtLength(double length) {
		return angle;
	}
	
	public String toString() {
		return "<" + a.toString() + ", " + b.toString() + ">";
	}

}
