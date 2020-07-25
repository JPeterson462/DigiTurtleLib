package com.digiturtle.graphics.ui.widgets;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.math.Rectangle;

public class ImageButton extends Button {
	
	private Image image;

	public ImageButton(String id) {
		super(id);
	}
	
	public void setEnabled(boolean enabled) {
		boolean wasEnabled = isEnabled();
		super.setEnabled(enabled);
		if (wasEnabled ^ enabled) {
			image.setAlpha(image.getAlpha() * (enabled ? 2f : 0.5f));
		}
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void render(Camera camera, RenderingContext context) {
		if (!visible) return;
		super.render(camera, context);
		Rectangle rectangle = (Rectangle) getBounds();
		camera.translate(rectangle.x, rectangle.y);
		image.render(camera, context);
		camera.translate(-rectangle.x, -rectangle.y);
	}

}
