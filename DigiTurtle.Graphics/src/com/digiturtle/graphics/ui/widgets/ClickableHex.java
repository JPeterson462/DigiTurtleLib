package com.digiturtle.graphics.ui.widgets;

import java.io.InputStream;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.Texture;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.ShiftEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.RegularPolygon;

public class ClickableHex extends Widget {
	
	@FunctionalInterface
	public interface ClickListener {
		public void onClick(ClickableHex hex, double x, double y);
	}
	
	private float[] fill;
	
	private int terrainSwatchPointer;
	
	private Texture terrainSwatch;
	
	private ClickListener listener = (hex, x, y) -> {};
	
	public ClickableHex(String id) {
		super(id);
	}
	
	public void setTerrainSwatch(InputStream stream) {
		terrainSwatch = new Texture(stream, false);
	}

	public void setFill(float[] fill) {
		this.fill = fill;
	}
	
	public void setListener(ClickListener listener) {
		this.listener = listener;
	}
	
	public void setBounds(double cx, double cy, double radius) {
		setBounds(new RegularPolygon(cx, cy, radius, 6));
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (event.containedIn(getBounds())) {
			if (event instanceof ClickEvent) {
				listener.onClick(this, ((ClickEvent) event).x, ((ClickEvent) event).y);
			}
			if (event instanceof ShiftEvent) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (!terrainSwatch.isGLCreated()) {
			terrainSwatch.createGL();
			terrainSwatchPointer = context.createImageHandle(terrainSwatch);
		}
		if (terrainSwatch != null) {
			context.drawPolygon((RegularPolygon) getBounds(), camera, terrainSwatchPointer, new float[] { 0, 0, 0, 1 });
		} else {
			context.drawPolygon((RegularPolygon) getBounds(), camera, fill, new float[] { 0, 0, 0, 1 });
		}
	}

}
