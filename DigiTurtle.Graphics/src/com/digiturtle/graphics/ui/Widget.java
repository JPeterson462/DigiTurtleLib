package com.digiturtle.graphics.ui;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.AnimatedComponent.AnimatedMovement;
import com.digiturtle.math.Shape;

public abstract class Widget implements InputEventListener {
	
	private Shape bounds;
	
	private String id;
	
	private int zIndex;
	
	protected Model model;

	private String binding;
	
	private boolean skipInput = false, hovered, enabled = true;
	
	private AnimatedComponent animatedComponent;
	
	public Widget(String id) {
		this.id = id;
		zIndex = 1;
		hovered = false;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public void setAnimatedComponent(AnimatedComponent component) {
		animatedComponent = component;
	}
	
	public AnimatedComponent getAnimatedComponent() {
		return animatedComponent;
	}
	
	public void setSkipInput(boolean skipInput) {
		this.skipInput = skipInput;
	}
	
	public boolean shouldSkipInput() {
		return skipInput;
	}
	
	public void setModel(Model model, String binding) {
		this.model = model;
		this.binding = binding;
	}
	
	public String getBinding() {
		return binding;
	}
	
	public String getModelValue(String binding) {
		if (model == null) {
			return null;
		}
		return model.getValue(binding);
	}
	
	public String getModelValue() {
		return getModelValue(binding);
	}
	
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public int getZIndex() {
		return zIndex;
	}
	
	public String getId() {
		return id;
	}
	
	public void setBounds(Shape bounds) {
		this.bounds = bounds;
	}
	
	public Shape getBounds() {
		return bounds;
	}
	
	public String toString() {
		return getClass().getSimpleName() + "#" + id;
	}
	
	public void update(float delta) {
		if (animatedComponent != null) {
			//Logger.debug("Widget::update()", this);
			animatedComponent.update(delta);
			if (animatedComponent instanceof AnimatedMovement) {
				Vector2d position = ((AnimatedMovement) animatedComponent).getPosition();
				getBounds().move(position.x, position.y);
			}
		}
	}

	public abstract void render(Camera camera, RenderingContext context);
	
}
