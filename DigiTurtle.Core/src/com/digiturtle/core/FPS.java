package com.digiturtle.core;

public class FPS {
	
	private long lastSecond;
	
	private int framesLastSecond, framesThisSecond;
	
	public FPS() {
		framesLastSecond = 0;
		framesThisSecond = 0;
	}
	
	public void tick() {
		if (System.currentTimeMillis() - lastSecond > 1000) {
			framesLastSecond = framesThisSecond;
			framesThisSecond = 0;
			lastSecond = System.currentTimeMillis();
		}
		framesThisSecond++;
	}
	
	public int getFPS() {
		return framesLastSecond;
	}

}
