package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.Draggable;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.DragEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.MathUtils;

public class DraggableSurface extends Widget implements Draggable {
	
	private Widget surface;
	
	private double offsetX, offsetY;
	
	public DraggableSurface(String id) {
		super(id);
	}
	
	public Vector2d getOffset() {
		return new Vector2d(offsetX, offsetY);
	}
	
	public void centerSurface() {
		offsetX = 1 * Math.max(0, (surface.getBounds().getWidth() - getBounds().getWidth()) * 0.5);
		offsetY = 1 * Math.max(0, (surface.getBounds().getHeight() - getBounds().getHeight()) * 0.5); 
	}
	
	public void setSurface(Widget surface) {
		this.surface = surface;
	}
	
	public Widget getSurface() {
		return surface;
	}
	
	public void update(float delta) {
		surface.update(delta);
	}
	
	@Override
	public void render(Camera camera, RenderingContext context) {
		camera.translate(-offsetX, -offsetY);
		surface.render(camera, context);
		camera.translate(offsetX, offsetY);
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (event.containedIn(getBounds())) {
			boolean response = surface.processInput(event.translate(offsetX, offsetY));
			if (event instanceof DragEvent && !response) {
				DragEvent dragEvent = (DragEvent) event;
				//Logger.debug("DRAG IN SURFACE", dragEvent.dx, dragEvent.dy);
				offsetX = MathUtils.clamp(0, offsetX - dragEvent.dx, (surface.getBounds().getWidth() - getBounds().getWidth()));
				offsetY = MathUtils.clamp(0, offsetY - dragEvent.dy, (surface.getBounds().getHeight() - getBounds().getHeight()));
			}
			return response;
		}
		return false;
	}

}
