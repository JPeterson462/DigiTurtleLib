package com.digiturtle.core;

public class NumberFormatter {
	
	public static String simplifyNumber(int number) {
		if (number < 100_000) {
			return Integer.toString(number);
		}
		else if (number < 10_000_000) {
			return Integer.toString(number / 1_000) + "K";
		}
		else {
			return Integer.toString(number / 1_000_000) + "M";
		}
	}

}
