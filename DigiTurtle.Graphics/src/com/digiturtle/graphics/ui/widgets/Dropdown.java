package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;
import org.json.JSONArray;
import org.json.JSONObject;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Font;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.TextAlign;
import com.digiturtle.graphics.ui.InputConsumer;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseMoveEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.Shape;

public class Dropdown extends Widget implements InputConsumer {
	// Note: Dropdown options container will not be scrollable now, but eventually can wrap container in ScrollableWidget
	
	// [ A        ]
	// [ B        ]
	// [ C        ]
	// [ D        ]
	// [ Selected ][^]
	
	// [ A        ] = Shape under text in Container
	// [ Selected ] = Shape under text
	// [^] = IconAndLabelButton
	// Options are stored in JSONArray in model binding
	
	public enum Direction {
		UP,
		DOWN
	}
	
	@FunctionalInterface
	public interface DropdownListener {
		public void onSelected(Dropdown dropdown, String value);
	}
	
	private Direction direction = Direction.DOWN;
	
	private DropdownListener listener = (dropdown, value) -> {};
	
	private String fontFace;
	
	private int fontSize;
	
	private JSONObject selected;
	
	private float[] backgroundColor, textColor, hoveredColor;
	
	private IconAndLabelButton openCloseOptions;
	
	private boolean rounded = false;
	
	private boolean optionsVisible = false;
	
	private Rectangle optionsBounds, hoverBounds;
	
	private int hoveredIndex = -1;
	
	private String action;

	public Dropdown(String id) {
		super(id);
		openCloseOptions = new IconAndLabelButton(id + ".OpenClose");
		openCloseOptions.setIcon(FontAwesome.CHEVRON_UP);
		openCloseOptions.setRounded(false);
		openCloseOptions.setListener((b) -> {
			toggleOptions();
		});
	}
	
	private FontAwesome getIcon(boolean isOpen) {
		switch (direction) {
		case DOWN:
			return isOpen ? FontAwesome.CHEVRON_UP : FontAwesome.CHEVRON_DOWN;
		case UP:
			return isOpen ? FontAwesome.CHEVRON_DOWN : FontAwesome.CHEVRON_UP;
		}
		return null;
	}
	
	public void setListener(DropdownListener listener) {
		this.listener = listener;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
		openCloseOptions.setIcon(getIcon(optionsVisible));
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
		openCloseOptions.setRounded(rounded);
	}
	
	public void toggleOptions() {
		hoveredIndex = -1;
		if (optionsVisible) {
			optionsVisible = false;
			openCloseOptions.setIcon(getIcon(optionsVisible));			
		} else {
			optionsVisible = true;
			openCloseOptions.setIcon(getIcon(optionsVisible));			
		}
	}
	
	private double getRadius() {
		return rounded ? Math.min(getBounds().getWidth(), getBounds().getHeight()) * 0.25 : 0;
	}
	
	public void setBounds(Shape bounds) {
		super.setBounds(bounds);
		double width = bounds.getWidth(), height = bounds.getHeight();
		double buttonSize = height;
		openCloseOptions.setBounds(new Rectangle(width - buttonSize - (height - buttonSize) * 0.5, (height - buttonSize) * 0.5, buttonSize, buttonSize));
		openCloseOptions.setFontSize((int) buttonSize - 6);
		optionsBounds = new Rectangle(getRadius(), 0, width - getRadius() * 2, height);
		hoverBounds = new Rectangle(getRadius(), 0, width - getRadius() * 2, bounds.getHeight());
	}
	
	public void setBackgroundColor(float[] backgroundColor, float[] hoveredColor) {
		this.backgroundColor = backgroundColor;
		this.hoveredColor = hoveredColor;
	}
	
	public void setTextColor(float[] textColor) {
		this.textColor = textColor;
	}
	
	public void setButtonColor(float[] up, float[] hover, float[] down) {
		openCloseOptions.setBackgroundColor(up, hover, down);
	}
	
	public void setButtonTextColor(float[] textColor) {
		openCloseOptions.setTextColor(textColor);
	}
	
	public void setFont(String fontFace, int fontSize) {
		this.fontFace = fontFace;
		this.fontSize = fontSize;
	}
	
	private String getSelectedText() {
		return selected.getString("text");
	}
	
	public String getSelectedValue() {
		return selected.getString("value");
	}
	
	private double getOptionsContainerOffset() {
		if (direction.equals(Direction.UP)) {
			return -optionsBounds.getHeight();
		}
		else if (direction.equals(Direction.DOWN)) {
			return getBounds().getHeight();
		}
		else {
			return 0;
		}
	}
	
	public void setSelectedOption(int index) {
		selected = new JSONArray(getModelValue()).getJSONObject(index);
		listener.onSelected(this, getSelectedValue());
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (event.containedIn(getBounds())) {
			Vector2d topLeft = getBounds().getTopLeft();
			event = event.translate(-topLeft.x, -topLeft.y);
			boolean buttonResult = openCloseOptions.processInput(event);
			return buttonResult;
		} else {
			Vector2d topLeft = getBounds().getTopLeft();
			event = event.translate(-topLeft.x, -topLeft.y);
			event = event.translate(0, -getOptionsContainerOffset());
			if (optionsVisible && event.containedIn(optionsBounds)) {
				double y = -1;
				if (event instanceof ClickEvent) {
					y = ((ClickEvent) event).y;
				}
				if (event instanceof MouseMoveEvent) {
					y = ((MouseMoveEvent) event).y;
				}
				if (y > -1) {
					hoveredIndex = (int) Math.floor(y / getBounds().getHeight());
					if (event instanceof ClickEvent) {
						setSelectedOption(hoveredIndex);
						toggleOptions();
					}
				} else {
					hoveredIndex = -1;
				}
				return true;
			}
			else if (optionsVisible && event instanceof ClickEvent) {
				toggleOptions();
			}
		}
		return false;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		JSONArray choices = new JSONArray(getModelValue());
		{
			optionsBounds.height = getBounds().getHeight() * choices.length();
		}
		if (selected == null) {
			setSelectedOption(0);
		}
		Font font = Font.findFont(fontFace);
		font.tryCreate(context);
		Vector2d topLeft = getBounds().getTopLeft();
		context.drawShape(getBounds(), backgroundColor, camera, getRadius());
		camera.translate(topLeft.x, topLeft.y);
		double offset = (getBounds().getHeight() - fontSize) * 0.5;
		camera.translate(offset, offset);
		context.drawText(getSelectedText(), camera, new Vector2d(), textColor, fontFace, fontSize, TextAlign.LEFT_TOP);
		camera.translate(-offset, -offset);
		openCloseOptions.render(camera, context);
		if (optionsVisible) {
			double offsetY = getOptionsContainerOffset();
			camera.translate(0, offsetY);
			context.drawShape(optionsBounds, backgroundColor, camera, 0);
			double optionOffset = 0;
			for (int i = 0; i < choices.length(); i++) {
				camera.translate(0, optionOffset);
				if (hoveredIndex == i) {
					context.drawShape(hoverBounds, hoveredColor, camera, 0);
				}
				camera.translate(offset, offset);
				context.drawText(choices.getJSONObject(i).getString("text"), camera, new Vector2d(), textColor, fontFace, fontSize, TextAlign.LEFT_TOP);
				camera.translate(-offset, -(optionOffset + offset));
				optionOffset += getBounds().getHeight();
			}
			camera.translate(0, -offsetY);
		}
		camera.translate(-topLeft.x, -topLeft.y);
	}

}
