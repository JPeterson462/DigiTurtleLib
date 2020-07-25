package com.digiturtle.math;

public class Circle extends Ellipse {
	
	public void setRadius(double radius) {
		setRadius(radius, radius);
	}
	
	@Override
	public void setRadius(double xAxis, double yAxis) {
		if (xAxis != yAxis) {
			throw new IllegalArgumentException("Circles cannot have uneven radii");
		}
		super.setRadius(xAxis, yAxis);
	}

}
