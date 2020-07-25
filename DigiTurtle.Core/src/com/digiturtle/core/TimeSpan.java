package com.digiturtle.core;

public class TimeSpan {

	private int seconds;
	
	public TimeSpan(int hours, int minutes, int seconds) {
		this.seconds = seconds + (minutes * 60) + (hours * 60 * 60);
	}
	
	public static TimeSpan fromHours(int hours) {
		return new TimeSpan(hours, 0, 0);
	}
	
	public static TimeSpan fromMinutes(int minutes) {
		return new TimeSpan(0, minutes, 0);
	}
	
	public static TimeSpan fromSeconds(int seconds) {
		return new TimeSpan(0, 0, seconds);
	}
	
	public int getInSeconds() {
		return seconds;
	}
	
}
