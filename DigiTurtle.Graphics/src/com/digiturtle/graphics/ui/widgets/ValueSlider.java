package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.DragEvent;
import com.digiturtle.graphics.ui.InputEvent.DragStartEvent;
import com.digiturtle.graphics.ui.InputEvent.DragStopEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.MathUtils;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.Shape;

public class ValueSlider extends Widget {
	
	public enum Direction {
		HORIZONTAL(new Vector2d(1, 0)),
		VERTICAL(new Vector2d(0, 1));
		private Vector2d vector;
		Direction(Vector2d vector) {
			this.vector = vector;
		}
	}
	
	@FunctionalInterface
	public interface ValueListener {
		public void onValueChanged(ValueSlider slider);
	}
	
	private Direction direction;
	
	private Shape slider;
	
	protected double min, max;
	
	private double value, thickness;
	
	private float[] barColor;
	
	private float[][] sliderColor;
	
	private boolean inDrag = false;
	
	private double innerPadding;
	
	private boolean canDragValue;
	
	private int state;
	
	private ValueListener listener = (slider) -> {};
	
	public ValueSlider(String id) {
		super(id);
		canDragValue = true;
	}
	
	public double getMaximum() {
		return max;
	}
	
	public void setListener(ValueListener listener) {
		this.listener = listener;
	}
	
	public void setCanDragValue(boolean canDragValue) {
		this.canDragValue = canDragValue;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = MathUtils.clamp(min, value, max);
		listener.onValueChanged(this);
	}
	
	public void setInnerPadding(double innerPadding) {
		this.innerPadding = innerPadding;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setDefaultValue(double value) {
		this.value = value;
	}
	
	public void setSlider(Shape shape) {
		slider = shape;
	}
	
	public void setRange(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	
	public void setBarColor(float[] barColor) {
		this.barColor = barColor;
	}
	
	public void setSliderColor(float[] up) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		sliderColor = new float[][] { up, up, up, disabled };
	}
	
	public void setSliderColor(float[] up, float[] hover) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		sliderColor = new float[][] { up, hover, hover, disabled };	
	}
	
	public void setSliderColor(float[] up, float[] hover, float[] down) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		sliderColor = new float[][] { up, hover, down, disabled };
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (!isEnabled()) {
			state = 3;
			return false;
		}
		state = 0;
		if (event.containedIn(getBounds())) {
			state = 1;
			if (canDragValue) {
				Rectangle rectangle = (Rectangle) getBounds();
				double offset = (value - min) / (max - min);
				Vector2d sliderOffset = new Vector2d(1, 1).sub(direction.vector).mul(getBounds().getWidth(), getBounds().getHeight()).mul(0.5);
				Vector2d valueOffset = new Vector2d(direction.vector).mul(offset).mul(getBounds().getWidth() - innerPadding * 2, getBounds().getHeight() - innerPadding * 2);
				Vector2d inputOffset = sliderOffset.add(valueOffset).add(new Vector2d(direction.vector).mul(innerPadding));
				InputEvent relativeToBar = event.translate(-(rectangle.getX() + inputOffset.x), -(rectangle.getY() + inputOffset.y));
				if (relativeToBar instanceof DragStartEvent) {
					DragStartEvent start = (DragStartEvent) relativeToBar;
					if (slider.contains(start.x, start.y)) {
						inDrag = true;
						state = 2;
					}
				}
				else if (relativeToBar instanceof DragEvent && inDrag) {
					DragEvent drag = (DragEvent) relativeToBar;
					Vector2d change = new Vector2d(drag.dx, drag.dy).mul(direction.vector);
							//.div(getBounds().getWidth() - innerPadding * 2, getBounds().getHeight() - innerPadding * 2)
							//.mul(getBounds().getWidth(), getBounds().getHeight());
					value += (change.x + change.y);
					value = MathUtils.clamp(min, value, getMaximum());
				}
				else if (relativeToBar instanceof DragStopEvent && inDrag) {
					inDrag = false;
					state = 1;
					listener.onValueChanged(this);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Vector2d sliderOffset = new Vector2d(1, 1).sub(direction.vector).mul(getBounds().getWidth(), getBounds().getHeight()).mul(0.5);
		Vector2d sliderThickness = new Vector2d(1, 1).sub(direction.vector).mul(-0.5).mul(thickness);
		Vector2d sliderLong = new Vector2d(direction.vector).mul(getBounds().getWidth(), getBounds().getHeight());
		Vector2d sliderShort = new Vector2d(1, 1).sub(direction.vector).mul(thickness);
		Vector2d sliderSize = sliderLong.add(sliderShort);
		Rectangle bounds = (Rectangle) getBounds();
		camera.translate(bounds.getX(), bounds.getY());
		camera.translate(sliderOffset.x, sliderOffset.y);
		context.drawShape(new Rectangle(sliderThickness.x, sliderThickness.y, sliderSize.x, sliderSize.y), barColor, camera, 0);
		double offset = (value - min) / (max - min);
		Vector2d valueOffset = new Vector2d(direction.vector).mul(getBounds().getWidth() - innerPadding * 2 - slider.getWidth(), getBounds().getHeight() - innerPadding * 2 - slider.getHeight()).mul(offset);
		valueOffset = valueOffset.add(new Vector2d(direction.vector).mul(innerPadding));
		camera.translate(valueOffset.x, valueOffset.y);
		context.drawShape(slider, sliderColor[state], camera, 0);
		camera.translate(-valueOffset.x, -valueOffset.y);
		camera.translate(-sliderOffset.x, -sliderOffset.y);
		camera.translate(-bounds.getX(), -bounds.getY());
	}

}
