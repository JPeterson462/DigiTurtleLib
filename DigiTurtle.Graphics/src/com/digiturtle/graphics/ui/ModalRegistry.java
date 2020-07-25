package com.digiturtle.graphics.ui;

import java.util.HashMap;

import com.digiturtle.graphics.ui.widgets.Modal;

public class ModalRegistry {
	
	private HashMap<String, Modal> registry;
	
	public ModalRegistry() {
		registry = new HashMap<>();
	}
	
	public void registerModal(Modal modal) {
		registry.put(modal.getId(), modal);
	}
	
	public Modal getModal(String id) {
		return registry.get(id);
	}

}
