package com.digiturtle.graphics.ui;

import java.util.HashMap;

public class Model {
	
	private HashMap<String, String> values = new HashMap<>();
	
	private Model parentModel;
	
	public Model() {
		
	}
	
	public Model(Model parentModel) {
		this.parentModel = parentModel;
	}
	
	public String getValue(String id) {
		String value = values.get(id);
		if (value == null && parentModel != null) {
			value = parentModel.getValue(id);
		}
		return value;
	}
	
	public void setValue(String id, String value) {
		values.put(id, value);
	}
	
	public String toString() {
		return values.toString();
	}

}
