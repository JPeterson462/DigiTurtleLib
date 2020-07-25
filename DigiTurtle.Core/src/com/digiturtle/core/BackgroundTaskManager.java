package com.digiturtle.core;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundTaskManager {
	
	private Timer timer;
	
	private HashMap<String, Runnable> runnables;
	
	private HashMap<String, TimerTask> tasks;
	
	public BackgroundTaskManager() {
		timer = new Timer();
		runnables = new HashMap<>();
		tasks = new HashMap<>();
	}
	
	public void cancelAll() {
		timer.cancel();
	}
	
	public boolean cancelTask(String name) {
		TimerTask task = tasks.get(name);
		if (task.cancel()) {
			runnables.remove(name);
			tasks.remove(name);
			return true;
		} else {
			return false;
		}
	}
	
	public void scheduleRepeating(String name, TimeSpan interval, Runnable runnable) {
		runnables.put(name, runnable);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runnable.run();
			}
		};
		tasks.put(name, task);
		timer.scheduleAtFixedRate(task, 0, interval.getInSeconds() * 1000);
	}
	
	public void scheduleDelayed(String name, TimeSpan delay, Runnable runnable) {
		runnables.put(name, runnable);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runnable.run();
			}
		};
		tasks.put(name, task);
		timer.schedule(task, delay.getInSeconds() * 1000);
	}
	
	public String toString() {
		return getClass().getSimpleName() + ": " + runnables.keySet().toString();
	}

}
