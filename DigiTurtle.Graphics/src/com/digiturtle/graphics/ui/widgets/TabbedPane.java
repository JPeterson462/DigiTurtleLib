package com.digiturtle.graphics.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.RightTrapezoid;

public class TabbedPane extends Widget {
	
	private ArrayList<Button> tabButtons;
	
	private HashMap<String, Container> tabContents;
	
	private Rectangle containerBounds;
	
	private String activeTab;
	
	private double tabHeight, slant, tabsSoFar = 0;
	
	private String fontFace;
	
	private int fontSize;

	public TabbedPane(String id) {
		super(id);
		tabButtons = new ArrayList<>();
		tabContents = new HashMap<>();
		containerBounds = new Rectangle(0, 0, 1, 1);
	}
	
	public Collection<Container> getContents() {
		return tabContents.values();
	}
	
	public void setTabSize(double tabHeight, double slant) {
		this.tabHeight = tabHeight;
		this.slant = slant;
	}
	
	public void setFont(String fontFace, int fontSize) {
		this.fontFace = fontFace;
		this.fontSize = fontSize;
	}
	
	public void addTab(FontAwesome icon, String text, double tabWidth, Container container, float[] up, float[] hover, float[] down, float[] textColor) {
		if (tabButtons.size() == 0) {
			activeTab = text;
		}
		IconAndLabelButton button = new IconAndLabelButton(getId() + ".Tab[" + text + "]");
		button.setListener((b) -> {
			for (int i = 0; i < tabButtons.size(); i++) {
				if (b == tabButtons.get(i)) {
					tabButtons.get(i).setOverrideState(2);
				} else {
					tabButtons.get(i).clearOverrideState();
				}
			}
			activeTab = ((IconAndLabelButton) b).getLabel();
		});
		if (tabButtons.size() == 0) {
			button.setOverrideState(2);
		}
		button.setBackgroundColor(up, hover, down);
		button.setBounds(new RightTrapezoid(tabsSoFar, 0, tabHeight, tabWidth * slant, tabWidth));
		System.out.println(button.getBounds());
		Logger.debug("addTab", up, hover, down);
		button.setFontFace(fontFace);
		button.setFontSize(fontSize);
		if (icon != null) {
			button.setIcon(icon);
		}
		button.setLabel(text);
		button.setTextColor(textColor);
		tabButtons.add(button);
		tabsSoFar += tabWidth;
		containerBounds.width = Math.max(containerBounds.width, container.getBounds().getWidth());
		containerBounds.height = Math.max(containerBounds.height, container.getBounds().getHeight());
		tabContents.put(text, container);
	}

	@Override
	public boolean processInput(InputEvent event) {
		Vector2d topLeft = getBounds().getTopLeft();
		InputEvent translated1 = event.translate(-topLeft.x, -topLeft.y);
		for (int i = 0; i < tabButtons.size(); i++) {
			if (tabButtons.get(i).processInput(translated1)) {
				return true;
			}
		}
		if (translated1.containedIn(containerBounds)) {
			return tabContents.get(activeTab).processInput(translated1.translate(0, -tabHeight));
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Vector2d topLeft = getBounds().getTopLeft();
		camera.translate(topLeft.x, topLeft.y);
		for (int i = 0 ; i < tabButtons.size(); i++) {
			tabButtons.get(i).render(camera, context);
		}
		camera.translate(0, tabHeight);
		tabContents.get(activeTab).render(camera, context);
		camera.translate(0, -tabHeight);
		camera.translate(-topLeft.x, -topLeft.y);
	}

}
