package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.graphics.ui.widgets.ValueSlider.Direction;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.Shape;

public class ScrollableWidget extends Widget {
	
	private Widget contents;
	
	private Shape contentBounds;
	
	private ValueSlider horizontal, vertical;
	
	private boolean horizontalScroll, verticalScroll;
	
	private double scrollSize = 16;

	public ScrollableWidget(String id, double scrollSize) {
		super(id);
		this.scrollSize = scrollSize;
		horizontal = new ValueSlider(id + ".Horizontal");
		vertical = new ValueSlider(id + ".Vertical");
		horizontal.setDefaultValue(0);
		vertical.setDefaultValue(0);
		horizontal.setDirection(Direction.HORIZONTAL);
		vertical.setDirection(Direction.VERTICAL);
		horizontal.setThickness(scrollSize);
		vertical.setThickness(scrollSize);
	}
	
	public void setContents(Widget contents) {
		this.contents = contents;
	}
	
	public Widget getContents() {
		return contents;
	}
	
	public void setBounds(Shape bounds) {
		super.setBounds(bounds);
		//Vector2d topLeft = bounds.getTopLeft();
		layout();
	}
	
	public void layout() {
		contentBounds = new Rectangle(0, 0, getBounds().getWidth() - scrollSize, getBounds().getHeight() - scrollSize);
		//Logger.debug("ScrollableWidget.layout()", contents.getBounds().getHeight(), contentBounds.getHeight());
		horizontalScroll = contentBounds.getWidth() < contents.getBounds().getWidth();
		verticalScroll = contentBounds.getHeight() + (horizontalScroll ? 0 : scrollSize) < contents.getBounds().getHeight();	
		horizontal.setRange(0, contents.getBounds().getWidth() - contentBounds.getWidth());
		vertical.setRange(0, contents.getBounds().getHeight() - contentBounds.getHeight());
		horizontal.setBounds(new Rectangle(0, 0, contentBounds.getWidth(), scrollSize));
		horizontal.setSlider(new Rectangle(0, -scrollSize * 0.5, 
				(1 -(contentBounds.getWidth() / contents.getBounds().getWidth())) * contentBounds.getWidth(), scrollSize));
		vertical.setBounds(new Rectangle(0, 0, scrollSize, contentBounds.getHeight()));
		vertical.setSlider(new Rectangle(-scrollSize * 0.5, 0, scrollSize, (contentBounds.getHeight() / contents.getBounds().getHeight()) * contentBounds.getHeight()));
		horizontal.setInnerPadding(0);
		vertical.setInnerPadding(0);
	}
	
	public void setScrollbarColor(float[] background, float[] up, float[] hover, float[] down) {
		horizontal.setBarColor(background);
		horizontal.setSliderColor(up, hover, down);
		vertical.setBarColor(background);
		vertical.setSliderColor(up, hover, down);
	}
	
	private Vector2d computeScroll() {
		double scrollX = horizontal.getValue(), scrollY = vertical.getValue();
		return new Vector2d(scrollX, scrollY);
	}

	@Override
	public boolean processInput(InputEvent event) {
		//if (event.containedIn(getBounds())) {
			Vector2d offset = getBounds().getTopLeft();
			event = event.translate(-offset.x, -offset.y);
			InputEvent eventV = event.translate(-contentBounds.getWidth(), 0);
			InputEvent eventH = event.translate(0, -contentBounds.getHeight());
			if (verticalScroll && vertical.processInput(eventV)) {
				return true;
			}
			if (horizontalScroll && horizontal.processInput(eventH)) {
				return true;
			}
			Vector2d scroll = computeScroll();
			event = event.translate(scroll.x, scroll.y);
			return contents.processInput(event);
		//}
		//return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Vector2d scroll = computeScroll();
		double scrollX = scroll.x, scrollY = scroll.y;
		Vector2d offset = getBounds().getTopLeft();
		camera.translate(offset.x, offset.y);
		context.setScissor(contentBounds, camera);
		camera.translate(-scrollX, -scrollY);
		contents.render(camera, context);
		camera.translate(scrollX, scrollY);
		context.clearScissor();
		camera.translate(contentBounds.getWidth(), 0);
		if (verticalScroll) {
			vertical.render(camera, context);
		}
		camera.translate(-contentBounds.getWidth(), contentBounds.getHeight());
		if (horizontalScroll) {
			horizontal.render(camera, context);
		}
		camera.translate(0, -contentBounds.getHeight());
		camera.translate(-offset.x, -offset.y);
	}

}
