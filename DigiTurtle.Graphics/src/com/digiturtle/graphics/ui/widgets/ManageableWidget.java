package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.ShiftEvent;
import com.digiturtle.graphics.ui.Widget;

public class ManageableWidget extends Widget {
	
	private Widget widget, manager;
	
	private boolean managerVisible = false;

	public ManageableWidget(String id) {
		super(id);
	}
	
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	public Widget getWidget() {
		return widget;
	}
	
	public void setManager(Widget manager) {
		this.manager = manager;
	}
	
	public Widget getManager() {
		return manager;
	}
	
	private Vector2d getManagerOffset() {
		Vector2d offset = widget.getBounds().getCenter();
		offset.x -= manager.getBounds().getWidth() * 0.5;
		offset.y += widget.getBounds().getHeight() * 0.5;
		return offset;
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (managerVisible) {
			Vector2d offset = getManagerOffset();
			InputEvent translated = event.translate(-offset.x, -offset.y);
			if (translated.containedIn(manager.getBounds())) {
				return manager.processInput(translated);
			}
			if (event.containedIn(widget.getBounds())) {
				return true;
			}
		}
		if (event instanceof ClickEvent && ((ClickEvent) event).mods == 0) {
			if (event.containedIn(widget.getBounds())) {
				if (!managerVisible) {
					managerVisible = true;
				}
				return true;
			} else {
				if (managerVisible) {
					managerVisible = false;
					return true;
				}
			}
			return false;
		}
		if (event instanceof ShiftEvent && !managerVisible) {
			return false;
		}
		boolean widgetConsumed = widget.processInput(event);
		return widgetConsumed;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		widget.render(camera, context);
		if (managerVisible) {
			Vector2d offset = getManagerOffset();
			camera.translate(offset.x, offset.y);
			manager.render(camera, context);
			camera.translate(-offset.x, -offset.y);
		}
	}

}
