package com.digiturtle.screens;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.digiturtle.core.Logger;
import com.digiturtle.core.Named;
import com.digiturtle.graphics.ui.BroadcastListener;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEventListener;
import com.digiturtle.graphics.ui.ModalRegistry;
import com.digiturtle.graphics.ui.Model;
import com.digiturtle.graphics.ui.UIBuilder;
import com.digiturtle.math.DataUtils;

public class ScreenManager implements InputEventListener, BroadcastListener {
	
	private HashMap<String, Screen> screens;
	
	private String currentScreenName;
	
	private ModalRegistry modalRegistry;
	
	private Model model;
	
	public ScreenManager(InputStream modalRegistryJson, InputStream paletteJson) {
		screens = new HashMap<>();
		UIBuilder builder = null;
		try {
			builder = new UIBuilder(this, new JSONObject(DataUtils.readToString(paletteJson, 1024)));
		} catch (JSONException | IOException e) {
			Logger.error("ScreenManager()", e);
		}
		modalRegistry = builder.buildModalRegistry(modalRegistryJson);
		model = new Model();
	}
	
	public Model getSharedModel() {
		return model;
	}
	
	public ModalRegistry getModalRegistry() {
		return modalRegistry;
	}
	
	public void addScreen(Screen screen, boolean readonly) {
		String name = (readonly ? "Readonly" : "") + screen.getClass().getAnnotation(Named.class).name();
		screen.setManager(this);
		screen.setup(this);
		screens.put(name, screen);
	}
	
	public void changeScreen(String name) {
		currentScreenName = name;
		getCurrentScreen().show();
	}
	
	public Screen getCurrentScreen() {
		return screens.get(currentScreenName);
	}

	@Override
	public boolean processInput(InputEvent event) {
		return getCurrentScreen().processInput(event);
	}

	@Override
	public void onBroadcast(String name) {
		getCurrentScreen().onBroadcast(name);
	}

}
