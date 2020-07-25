package com.digiturtle.math;

import java.util.Arrays;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;

public class MultiLine extends Path {
	
	private Line[] lines;
	
	private double totalLength;
	
	public MultiLine(Vector2d... points) {
		totalLength = 0;
		lines = new Line[points.length - 1];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = new Line(points[i], points[i + 1]);
			totalLength += lines[i].getLength();
		}
	}
	
	public void translate(double dx, double dy) {
		for (int i = 0; i < lines.length; i++) {
			lines[i].translate(dx, dy);
		}
	}

	@Override
	public double getLength() {
		return totalLength;
	}

	@Override
	public Vector2d getPointAtLength(double length) {
		int i = 0;
		for (i = 0; i < lines.length; i++) {
			if (length < lines[i].getLength()) {
				break;
			} else {
				length -= lines[i].getLength();
			}
		}
		if (i == lines.length) {
			return lines[lines.length - 1].getPointAtNormalizedLength(1);
		}
		return lines[i].getPointAtLength(length);
	}
	
	public double getAngleAtLength(double length) {
		int i = 0;
		for (i = 0; i < lines.length; i++) {
			if (length < lines[i].getLength()) {
				break;
			} else {
				length -= lines[i].getLength();
			}
		}
		if (i == lines.length) {
			i--;
		}
		//Logger.debug("MultiLine", i);
		return lines[i].getAngle();
	}
	
	public String toString() {
		return Arrays.deepToString(lines);
	}

}
