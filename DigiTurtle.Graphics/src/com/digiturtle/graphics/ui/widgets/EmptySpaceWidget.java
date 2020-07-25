package com.digiturtle.graphics.ui.widgets;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;

public class EmptySpaceWidget extends Widget {
	
	private double strokeWidth = 2;
	
	private float[] stroke;
	
	private boolean visible = true;

	public EmptySpaceWidget(String id) {
		super(id);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setStroke(float[] stroke) {
		this.stroke = stroke;
	}

	@Override
	public boolean processInput(InputEvent event) {
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (!visible) {
			return;
		}
		context.traceBoundary(getBounds(), stroke, camera, strokeWidth, 
				Math.min(getBounds().getWidth(), getBounds().getHeight()) * 0.25);
	}

}
