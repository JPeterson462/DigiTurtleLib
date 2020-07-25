package com.digiturtle.graphics.ui.widgets;

import java.io.InputStream;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.Texture;
import com.digiturtle.graphics.ui.AnimatedComponent;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseMoveEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.DataUtils;
import com.digiturtle.math.Rectangle;

public class Image extends Widget {
	
	@FunctionalInterface
	public interface ClickListener {
		public void onClick(Image image);
	}

	private Texture texture;
	
	private int pointer = 0;
	
	private boolean rounded = false, visible = true;
	
	private String loadedSource = null;
	
	private float alpha = 1, rotation = 0;
	
	private ClickListener listener = (image) -> {};

	public Image(String id) {
		super(id);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setListener(ClickListener listener) {
		this.listener = listener;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	public void setSource(InputStream stream) {
		if (texture != null) {
			texture.doDispose();
			pointer = 0;
		}
		texture = new Texture(stream, false);
	}

	public void setSourceRepeated(InputStream stream, float scale) {
		if (texture != null) {
			texture.doDispose();
			pointer = 0;
		}
		texture = new Texture(stream, false).setRepeat(true, scale);
	}
	
	public void setRotation(float angle) {
		this.rotation = angle;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public Texture getSource() {
		return texture;
	}
	
	public int getSourceWidth() {
		return texture.getWidth();
	}

	public int getSourceHeight() {
		return texture.getHeight();
	}
	
	public void update(float delta) {
		super.update(delta);
		if (getAnimatedComponent() != null) {
			AnimatedComponent component = getAnimatedComponent();
			if (component instanceof AnimatedComponent.AnimatedMovement) {
				AnimatedComponent.AnimatedMovement movement = (AnimatedComponent.AnimatedMovement) component;
				double angleAnimated = movement.getAngle();
				//Logger.debug("Image.update", this, angleAnimated);
				setRotation((float) angleAnimated - (float) Math.PI * 0.5f);
			}
		}
	}
	
	@Override
	public boolean processInput(InputEvent event) {
		if (!visible) return false;
		if (event.containedIn(getBounds())) {
			if (event instanceof ClickEvent) {
				listener.onClick(this);
			}
			if (event instanceof MouseMoveEvent) {
				setHovered(true);
			}
		} else {
			if (event instanceof MouseMoveEvent) {
				setHovered(false);
			}
		}
		return event.containedIn(getBounds());
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (!visible) return;
		String binding = getModelValue();
		if (texture == null && binding != null && (loadedSource == null || !binding.contentEquals(loadedSource))) {
			setSource(DataUtils.getResource(binding));
			loadedSource = binding;
		}
		if (pointer == 0) {
			if (!texture.isGLCreated()) {
				texture.createGL();
			}
			pointer = context.createImageHandle(texture);
		}
		context.drawImage((Rectangle) getBounds(), this, camera,
				rounded ? Math.min(getBounds().getWidth(), getBounds().getHeight()) * 0.25 : 0, alpha);
	}
	
	public int getPointer() {
		return pointer;
	}

}
