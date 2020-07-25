package com.digiturtle.math;

import java.util.ArrayList;
import org.joml.Vector2d;

public class RegularPolygon extends Shape {
	
	private ArrayList<Vector2d> points = new ArrayList<>();
	
	private double cx, cy, radius;
	
	private final double ANGLE_DELTA;
	
	public RegularPolygon(double cx, double cy, double radius, int sides) {
		ANGLE_DELTA = 2 * Math.PI / (double) sides;
		double angle = ANGLE_DELTA * 0.5;
		for (int i = 0; i < sides; i++) {
			points.add(new Vector2d(Math.cos(angle) * radius + cx, Math.sin(angle) * radius + cy));
			angle += ANGLE_DELTA;
		}
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
	}
	
	public int getNumberOfSides() {
		return points.size();
	}
	
	public Vector2d getPoint(int index) {
		return points.get(index);
	}
	
	public double getCenterX() {
		return cx;
	}
	
	public double getCenterY() {
		return cy;
	}

	@Override
	public double getWidth() {
		return radius * 2;
	}

	@Override
	public double getHeight() {
		return radius * 2;
	}

	@Override
	public boolean contains(double px, double py) {
		boolean inside = true;
		double TEST = -radius;
		for (int i = 0; i < points.size(); i++) {
			Vector2d p1 = points.get(i);
			Vector2d p2 = points.get((i + 1) % points.size());
			Vector2d u = new Vector2d(p2).sub(p1);
			Vector2d ap = new Vector2d(px, py).sub(p1);
			double crossProduct = (ap.x * u.y) - (ap.y * u.x);
			double d = crossProduct / u.length();
			//System.out.println(d);
			if (d > 0) {
				inside = false;
			}
			TEST = Math.max(TEST, d);
		}
		//System.out.println(TEST);
		return inside;
	}

	@Override
	public Vector2d getCenter() {
		return new Vector2d(cx, cy);
	}

	@Override
	public void translate(double dx, double dy) {
		throw new IllegalStateException("Cannot translate() RegularPolygon instances");
	}

	@Override
	public void move(double x, double y) {
		throw new IllegalStateException("Cannot move() RegularPolygon instances");
	}

}
