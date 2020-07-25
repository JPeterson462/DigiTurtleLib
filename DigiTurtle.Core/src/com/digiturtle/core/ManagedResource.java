package com.digiturtle.core;

import java.util.ArrayList;

public abstract class ManagedResource {
	
	private static ArrayList<ManagedResource> resources = new ArrayList<ManagedResource>();
	
	private boolean disposed;
	
	public ManagedResource() {
		disposed = false;
	}
	
	public static void disposeAll() {
		for (int i = 0; i < resources.size(); i++) {
			resources.get(i).doDispose();
		}
		resources.clear();
	}
	
	public abstract long getPointer();
	
	public abstract void dispose();
	
	public void doDispose() {
		if (!disposed) {
			dispose();
			disposed = true;
		}
	}

}
