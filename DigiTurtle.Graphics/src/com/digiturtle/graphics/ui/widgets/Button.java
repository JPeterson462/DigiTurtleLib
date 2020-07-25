package com.digiturtle.graphics.ui.widgets;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseButtonEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseMoveEvent;
import com.digiturtle.input.InputAdapter.MouseButton;
import com.digiturtle.graphics.ui.Widget;

public class Button extends Widget {
	
	@FunctionalInterface
	public interface ClickListener {
		public void onClick(Button button);
	}
	
	private float[][] backgroundColor;
	
	private ClickListener listener = (button) -> {};
	
	protected int state = 0, overrideState = -1;
	
	private String action;
	
	private boolean rounded;
	
	protected boolean visible = true;

	public Button(String id) {
		super(id);
		rounded = false;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setOverrideState(int state) {
		overrideState = state;
	}
	
	public void clearOverrideState() {
		overrideState = -1;
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	public boolean isRounded() {
		return rounded;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}
	
	public void clearState() {
		state = 0;
	}
	
	public void setListener(ClickListener listener) {
		this.listener = listener;
	}
	
	public void setBackgroundColor(float[] up) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		this.backgroundColor = new float[][] { up, up, up, disabled };
	}
	public void setBackgroundColor(float[] up, float[] hover) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		this.backgroundColor = new float[][] { up, hover, hover, disabled };
	}
	public void setBackgroundColor(float[] up, float[] hover, float[] down) {
		float[] disabled = new float[up.length];
		System.arraycopy(up, 0, disabled, 0, up.length);
		disabled[3] *= 0.5f;
		this.backgroundColor = new float[][] { up, hover, down, disabled };
	}
	
	@Override
	public boolean processInput(InputEvent event) {
		if (!visible) {
			return false;
		}
		if (!isEnabled()) {
			return false;
		}
		if (event.containedIn(getBounds())) {
			if (event instanceof MouseMoveEvent) {
				state = 1;
			}
			if (event instanceof MouseButtonEvent) {
				state = (((MouseButtonEvent) event).down && ((MouseButtonEvent) event).button == MouseButton.LEFT) ? 2 : 1;
			}
			if (event instanceof ClickEvent) {
				listener.onClick(this);
			}
			return true;
		} else {
			if (event instanceof MouseMoveEvent) {
				state = 0;
			}
		}
		return false;
	}
	
	private int getButtonState() {
		if (!isEnabled()) {
			return 3;
		}
		return overrideState >= 0 ? overrideState : state;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (!visible) return;
		setHovered(getButtonState() > 0);
		if (rounded) {
			context.drawShape(getBounds(), backgroundColor[getButtonState()], camera, 
					Math.min(getBounds().getWidth(), getBounds().getHeight()) * 0.25);
		} else {
			context.drawShape(getBounds(), backgroundColor[getButtonState()], camera, 0);
		}
	}

}
