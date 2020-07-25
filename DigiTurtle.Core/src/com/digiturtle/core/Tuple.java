package com.digiturtle.core;

public class Tuple<U, V> {
	
	private U u;
	
	private V v;
	
	public Tuple(U u, V v) {
		this.u = u;
		this.v = v;
	}
	
	public U getFirst() {
		return u;
	}
	
	public V getSecond() {
		return v;
	}

}
