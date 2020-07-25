package com.digiturtle.graphics.ui.widgets;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.graphics.ui.widgets.ValueSlider.Direction;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.Shape;

public class ZoomSlider extends Widget {
	
	@FunctionalInterface
	public interface ZoomListener {
		public void onZoom(ZoomSlider slider);
	}
	
	private IconAndLabelButton zoomIn, zoomOut;
	
	private ValueSlider zoomSlider;
	
	private int jumps;
	
	private ZoomListener listener = (slider) -> {};

	public ZoomSlider(String id) {
		super(id);
		zoomIn = new IconAndLabelButton(id + "_ZoomIn");
		zoomIn.setIcon(FontAwesome.PLUS);
		zoomIn.setListener((button) -> {
			zoomSlider.setValue(zoomSlider.getValue() + (zoomSlider.max - zoomSlider.min) / (double) jumps);
		});
		zoomOut = new IconAndLabelButton(id + "_ZoomOut");
		zoomOut.setIcon(FontAwesome.MINUS);
		zoomOut.setListener((button) -> {
			zoomSlider.setValue(zoomSlider.getValue() - (zoomSlider.max - zoomSlider.min) / (double) jumps);
		});
		zoomSlider = new ValueSlider(id + "_ZoomSlider");
		zoomSlider.setThickness(6);
		zoomSlider.setCanDragValue(false);
	}
	
	public void setListener(ZoomListener listener) {
		this.listener = listener;
		zoomSlider.setListener((slider) -> this.listener.onZoom(this));
	}
	
	public void setDefaultValue(double value) {
		zoomSlider.setDefaultValue(value);
	}
	
	public void setDirection(Direction direction) {
		zoomSlider.setDirection(direction);
	}
	
	public void setRange(double min, double max) {
		zoomSlider.setRange(min, max);
	}
	
	public void setJumps(int jumps) {
		this.jumps = jumps;
	}
	
	public double getZoom() {
		return zoomSlider.getValue();
	}
	
	public void setBounds(Shape bounds) {
		final double sliderGaps = 10;
		if (bounds instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) bounds;
			zoomOut.setBounds(new Rectangle(0, 0, rectangle.getHeight(), rectangle.getHeight()));
			zoomIn.setBounds(new Rectangle(rectangle.getWidth() - rectangle.getHeight(), 0, rectangle.getHeight(), rectangle.getHeight()));
			zoomSlider.setBounds(new Rectangle(rectangle.getHeight() + sliderGaps, 0, rectangle.getWidth() - 2 * (sliderGaps + rectangle.getHeight()), rectangle.getHeight()));
			zoomSlider.setInnerPadding(rectangle.getHeight() * 0.5 * 0.5);
			zoomSlider.setSlider(new Rectangle(0, 0, rectangle.getHeight() * 0.5, rectangle.getHeight()).center());
			zoomOut.setFontSize((int) rectangle.getHeight() - 3);
			zoomIn.setFontSize((int) rectangle.getHeight() - 3);
			super.setBounds(bounds);
		} else {
			throw new IllegalStateException("ZoomSlider bounds must be rectangular");
		}
	}
	
	public void setBarColor(float[] barColor) {
		zoomSlider.setBarColor(barColor);
	}
	
	public void setSliderColor(float[] up) {
		zoomSlider.setSliderColor(up);
	}

	public void setSliderColor(float[] up, float[] hover) {
		zoomSlider.setSliderColor(up, hover);
	}

	public void setSliderColor(float[] up, float[] hover, float[] down) {
		zoomSlider.setSliderColor(up, hover, down);
	}
	
	public void setButtonBackgroundColor(float[] up) {
		zoomIn.setBackgroundColor(up);
		zoomOut.setBackgroundColor(up);
	}
	
	public void setButtonBackgroundColor(float[] up, float[] hover) {
		zoomIn.setBackgroundColor(up, hover);
		zoomOut.setBackgroundColor(up, hover);
	}
	
	public void setButtonBackgroundColor(float[] up, float[] hover, float[] down) {
		zoomIn.setBackgroundColor(up, hover, down);
		zoomOut.setBackgroundColor(up, hover, down);
	}
	
	public void setButtonTextColor(float[] textColor) {
		zoomIn.setTextColor(textColor);
		zoomOut.setTextColor(textColor);
	}

	@Override
	public boolean processInput(InputEvent event) {
		Rectangle rectangle = (Rectangle) getBounds();
		if (event.containedIn(rectangle)) {
			InputEvent translated = event.translate(-rectangle.getX(), -rectangle.getY());
			if (zoomIn.processInput(translated)) {
				return true;
			}
			if (zoomOut.processInput(translated)) {
				return true;
			}
			if (zoomSlider.processInput(translated)) {
				return true;
			}
			return true;
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Rectangle rectangle = (Rectangle) getBounds();
		camera.translate(rectangle.getX(), rectangle.getY());
		zoomIn.render(camera, context);
		zoomOut.render(camera, context);
		zoomSlider.render(camera, context);
		camera.translate(-rectangle.getX(), -rectangle.getY());
	}

}
