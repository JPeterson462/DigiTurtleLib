package com.digiturtle.screens;

import java.util.ArrayList;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.BroadcastListener;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEventListener;
import com.digiturtle.graphics.ui.UserInterface;
import com.digiturtle.graphics.ui.widgets.Modal;

public abstract class Screen implements InputEventListener, BroadcastListener {
	
	private UserInterface overlay;
	
	public abstract void render(Camera camera, RenderingContext context);
	
	public abstract boolean forwardEvent(InputEvent event);
	
	public abstract void setup(ScreenManager screenManager);
	
	public abstract void show();
	
	private ArrayList<Modal> modalsShown;
	
	private ScreenManager screenManager;
	
	public Screen() {
		modalsShown = new ArrayList<>();
	}
	
	public UserInterface getOverlay() {
		return overlay;
	}
	
	public void setManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	public ScreenManager getScreenManager() {
		return screenManager;
	}
	
	public Modal getTopModal() {
		if (modalsShown.size() > 0) {
			return modalsShown.get(modalsShown.size() - 1);
		}
		return null;
	}
	
	public void showModal(Modal modal) {
		modal.setVisible(true);
		modal.setListener((m) -> {
			modalsShown.remove(modalsShown.size() - 1);
		});
		modalsShown.add(modal);
	}
	
	public void setOverlay(UserInterface overlay) {
		this.overlay = overlay;
	}
	
	public void doRender(Camera camera, RenderingContext context) {
		render(camera, context);
		for (int i = 0; i < modalsShown.size(); i++) {
			modalsShown.get(i).render(camera, context);
		}
		overlay.render(camera, context);
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (overlay.processInput(event)) {
			return true;
		}
		for (int i = modalsShown.size() - 1; i >= 0; i--) {
			if (modalsShown.get(i).shouldConsumeInput()) {
				return modalsShown.get(i).processInput(event);
			}
			if (modalsShown.get(i).processInput(event)) {
				return true;
			}
		}
		return forwardEvent(event);
	}

}
