package com.digiturtle.graphics.ui.widgets;

import java.util.ArrayList;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputConsumer;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Model;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;

public class Container extends Widget implements InputConsumer {
	
	private ArrayList<Widget> children;
	
	private float[] backgroundColor;
	
	private double zoom;
	
	private boolean rounded = false;
	
	public Container(String id) {
		super(id);
		children = new ArrayList<Widget>();
		zoom = 1;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Widget findChildById(String childId) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getId().equalsIgnoreCase(childId)) {
				return children.get(i);
			}
		}
		return null;
	}
	
	public Widget removeChildById(String childId) {
		for (int i = children.size() - 1; i >= 0; i--) {
			if (children.get(i).getId().equalsIgnoreCase(childId)) {
				return children.remove(i);
			}
		}
		return null;
	}
	
	public boolean removeChildByAction(String actionRegex) {
		int removed = 0;
		for (int i = children.size() - 1; i >= 0; i--) {
			if (children.get(i) instanceof Button && ((Button) children.get(i)).getAction().matches(actionRegex)) {
				children.remove(i);
				removed++;
			}
		}
		return removed > 0;
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public void setBackgroundColor(float[] backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void addChild(Widget widget) {
		children.add(widget);
		children.sort((w1, w2) -> Integer.compare(w1.getZIndex(), w2.getZIndex()));
		//Logger.debug("Container.Sort", children.get(0).getZIndex(), children.get(children.size() - 1).getZIndex());
	}
	
	public ArrayList<Widget> getChildren() {
		return children;
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (event.containedIn(getBounds())) {
			Rectangle rectangle = (Rectangle) getBounds();
			InputEvent translated = event.scale(1.0 / zoom).translate(-rectangle.getX(), -rectangle.getY());
			for (int i = children.size() - 1; i >= 0; i--) {
				if (children.get(i).shouldSkipInput()) {
					continue;
				}
				boolean result = children.get(i).processInput(translated);
//				if (result) {
//					return true;
//				}
			}
			return false;
		} else {
			Rectangle rectangle = (Rectangle) getBounds();
			InputEvent translated = event.scale(1.0 / zoom).translate(-rectangle.getX(), -rectangle.getY());
			for (int i = children.size() - 1; i >= 0; i--) {
				if (children.get(i) instanceof InputConsumer) {
					boolean result = children.get(i).processInput(translated);
					if (result) {
						//Logger.debug("Event consumed.", children.get(i), event);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void update(float delta) {
		super.update(delta);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).update(delta);
		}
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		//Logger.debug("Draw", getId());
		Rectangle rectangle = (Rectangle) getBounds();
		context.drawShape((Rectangle) getBounds(), backgroundColor, camera, 
				rounded ? Math.min(getBounds().getWidth(), getBounds().getHeight()) * 0.25 : 0);
		camera.translate(rectangle.getX(), rectangle.getY());
		camera.scale(zoom);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).render(camera, context);
		}
		camera.scale(1f / zoom);
		camera.translate(-rectangle.getX(), -rectangle.getY());
	}

}
