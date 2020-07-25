package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseMoveEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.input.InputAdapter;
import com.digiturtle.math.Rectangle;

public class MoveableWidget extends Widget {
	
	@FunctionalInterface
	public interface BoundsCheck {
		public boolean inside(String id, Rectangle rectangle);
	}

	private Widget widget, emptySpace;
	
	private BoundsCheck bounds;
	
	private boolean inMotion = false;
	
	private Vector2d mouseOffset;
	
	public MoveableWidget(String id) {
		super(id);
	}
	
	public void setEmptySpaceWidget(Widget widget) {
		emptySpace = widget;
	}
	
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	public Widget getWidget() {
		return widget;
	}
	
	public void setRestrictedBounds(BoundsCheck bounds) {
		this.bounds = bounds;
	}
	
	private boolean moveTo(Vector2d position) {
		Rectangle emptyBounds = (Rectangle) emptySpace.getBounds();
		Rectangle widgetBounds = (Rectangle) widget.getBounds();
		double x = emptyBounds.x, y = emptyBounds.y;
		emptyBounds.move(position.x - mouseOffset.x, position.y - mouseOffset.y);
		if (!bounds.inside(widget.getId(), emptyBounds)) {
			emptyBounds.x = x;
			emptyBounds.y = y;
			return false;
		}
		widgetBounds.move(position.x - mouseOffset.x, position.y - mouseOffset.y);
		return true;
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (inMotion) {
			if (event instanceof MouseMoveEvent) {
				if (!moveTo(new Vector2d(((MouseMoveEvent) event).x, ((MouseMoveEvent) event).y))) {
					inMotion = false;
					return true;
				}
			}
			if (event instanceof ClickEvent) {
				inMotion = false;
				return true;
			}
		} else {
			if (event.containedIn(widget.getBounds())) {
				if (event instanceof ClickEvent && (((ClickEvent) event).mods & InputAdapter.CTRL) != 0) {
					mouseOffset = new Vector2d(((ClickEvent) event).x, ((ClickEvent) event).y).sub(new Rectangle(widget.getBounds()).getXY());
					inMotion = true;
					return true;
				}
			}
		}
		return widget.processInput(event);
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (inMotion) {
			emptySpace.render(camera, context);
		} else {
			widget.render(camera, context);
		}
	}

}
