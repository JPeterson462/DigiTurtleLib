package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;

public class Tooltip extends Widget {
	
	private Widget target;
	
	private Container contents;

	public Tooltip(String id) {
		super(id);
	}
	
	public void setTarget(Widget widget) {
		Logger.debug("Tooltip:Target", widget);
		target = widget;
	}
	
	public void setContents(Container contents) {
		this.contents = contents;
	}

	@Override
	public boolean processInput(InputEvent event) {
		Vector2d offset = target.getBounds().getBottomLeft();
		return contents.processInput(event.translate(-offset.x, -offset.y));
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Vector2d offset = target.getBounds().getBottomLeft();
		if (target.isHovered()) {
			camera.translate(offset.x, offset.y);
			contents.render(camera, context);
			camera.translate(-offset.x, -offset.y);
		}
	}

}
