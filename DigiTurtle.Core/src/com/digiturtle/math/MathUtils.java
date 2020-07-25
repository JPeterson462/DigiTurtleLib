package com.digiturtle.math;

public final class MathUtils {
	
	// value in [start, end)
	public static boolean valueInRange(double value, double start, double end) {
		return value >= start && value < end;
	}
	public static boolean valueInRange(int value, int start, int end) {
		return value >= start && value < end;
	}
	
	// value -> [min, max]
	public static double clamp(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}
	public static int clamp(int min, int value, int max) {
		return Math.max(min, Math.min(value, max));
	}

}
