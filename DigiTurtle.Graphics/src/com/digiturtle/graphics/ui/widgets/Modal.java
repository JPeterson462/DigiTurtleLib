package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Font;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.TextAlign;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Model;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;

public class Modal extends Widget {
	
	private String title;

	private boolean visible = false;
	
	private float[] backgroundColor, textColor;
	
	private String fontFace;
	
	private int fontSize;
	
	private Container container;
	
	private IconAndLabelButton closeButton;
	
	private String closeReason;
	
	private CloseListener listener = (modal) -> {};
	
	private boolean consumeInput = false;
	
	@FunctionalInterface
	public interface CloseListener {
		public void onClose(Modal modal);
	}
	
	public Modal(String id) {
		super(id);
		closeButton = new IconAndLabelButton(id + "_Close");
		closeButton.setBounds(new Rectangle(0, 0, 0, 0));
		closeButton.setIcon(FontAwesome.REMOVE);
		closeButton.setTextColor(new float[] { 1, 1, 1, 1 });
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setConsumeInput(boolean consumeInput) {
		this.consumeInput = consumeInput;
	}
	
	public boolean shouldConsumeInput() {
		return consumeInput;
	}
	
	public void setListener(CloseListener listener) {
		this.listener = listener;
	}
	
	public String getCloseReason() {
		return closeReason;
	}
	
	public double getTitleBarHeight() {
		return 2 * 5 + fontSize;
	}
	
	public void setContainer(Container container) {
		this.container = container;
	}
	
	public Container getContainer() {
		return container;
	}
	
	public void setFont(String fontFace, int fontSize) {
		this.fontFace = fontFace;
		this.fontSize = fontSize;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setSize(double width, double height) {
		height += fontSize + 2 * 5;
		if (getBounds() != null) {
			((Rectangle) getBounds()).resize(width, height);
		} else {
			setBounds(new Rectangle(0, 0, width, height));
		}
	}
	
	public void setPosition(double x, double y) {
		if (getBounds() != null) {
			((Rectangle) getBounds()).move(x, y);
		} else {
			setBounds(new Rectangle(x, y, 0, 0));
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setBackgroundColor(float[] backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public float[] getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setTextColor(float[] textColor) {
		this.textColor = textColor;
	}
	
	public float[] getTextColor() {
		return textColor;
	}
	
	public void close(String closeReason) {
		setVisible(false);
		this.closeReason = closeReason;
		closeButton.clearState();
		listener.onClose(this);
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (!visible) return false;
		
		if (event.containedIn(getBounds())) {
			Rectangle bounds = (Rectangle) getBounds();
			InputEvent newEvent = event.translate(-bounds.getX(), -bounds.getY());
			if (closeButton.processInput(newEvent)) {
				return true;
			}
			newEvent = newEvent.translate(0, -(2 * 5 + fontSize));
			container.processInput(newEvent);
			return true;
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		if (visible) {
			Font font = Font.findFont(fontFace);
			font.tryCreate(context);
			Rectangle bounds = (Rectangle) getBounds();
			{
				closeButton.setBackgroundColor(new float[] { 1, 0, 0, 1 });
				closeButton.setBounds(new Rectangle(bounds.getWidth() - 1 * 5 - fontSize, 5, fontSize, fontSize));
				closeButton.setListener((button) -> {
					if (button.getId().equals(getId() + "_Close")) {
						Modal.this.close("Close");
					}
				});
				closeButton.setFontSize(fontSize - 2);
				closeButton.setBackgroundColor(new float[] { 1, 0, 0, 1 }, new float[] { .9f, 0, 0, 1 }, new float[] { .8f, 0, 0, 1 });
			}
			context.drawShape(new Rectangle(bounds.getX(), bounds.getY(), 
					bounds.getWidth(), bounds.getHeight()), backgroundColor, camera, 0);
			context.drawText(title, camera, new Vector2d(bounds.getXY()).add((bounds.getWidth() - fontSize - 2 * 5) * 0.5, 5),
					textColor, fontFace, fontSize, TextAlign.CENTER_TOP);
			camera.translate(bounds.getX(), bounds.getY());
			camera.translate(0, fontSize + 2 * 5);
			container.render(camera, context);
			camera.translate(0, -(fontSize + 2 * 5));
			closeButton.render(camera, context);
			camera.translate(-bounds.getX(), -bounds.getY());
		}
	}

}
