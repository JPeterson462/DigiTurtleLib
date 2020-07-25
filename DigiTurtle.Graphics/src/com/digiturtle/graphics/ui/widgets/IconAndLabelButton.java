package com.digiturtle.graphics.ui.widgets;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Font;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.TextAlign;

public class IconAndLabelButton extends Button {
	
	private FontAwesome icon;
	
	private String label, fontFace;
	
	private int fontSize;
	
	private float[] textColor;

	public IconAndLabelButton(String id) {
		super(id);
	}

	public void setEnabled(boolean enabled) {
		boolean wasEnabled = isEnabled();
		super.setEnabled(enabled);
		if (wasEnabled ^ enabled) {
			textColor[3] *= enabled ? 2f : 0.5f;
		}
	}
	
	public void setIcon(FontAwesome icon) {
		this.icon = icon;
	}
	
	public FontAwesome getIcon() {
		return icon;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setFontFace(String fontFace) {
		this.fontFace = fontFace;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setTextColor(float[] textColor) {
		this.textColor = textColor;
	}
	
	public void render(Camera camera, RenderingContext context) {
		if (!visible) return;
		super.render(camera, context);
		Font labelFont = Font.findFont(fontFace);
		Font iconFont = Font.findFont(FontAwesome.NAME);
		if (labelFont != null) {
			labelFont.tryCreate(context);
		}
		iconFont.tryCreate(context);
		Vector2d iconBounds = icon != null ? context.getTextBounds(icon.text, FontAwesome.NAME, fontSize, camera) : new Vector2d(0, 0);
		Vector2d spaceBounds = new Vector2d(4, 0);
		Vector2d labelBounds = label != null ? context.getTextBounds(label, fontFace, fontSize, camera) : new Vector2d(0, 0);
		if (icon != null && label != null) {
			double width = iconBounds.x + spaceBounds.x + labelBounds.x;
			Vector2d leftMidline = new Vector2d(getBounds().getCenter()).sub(getBounds().getWidth() * 0.5, 0);
			leftMidline = leftMidline.add(Math.floor((getBounds().getWidth() - width) * 0.5), 0);
			context.drawText(icon.text, camera, leftMidline, textColor, FontAwesome.NAME, fontSize, TextAlign.LEFT_MIDDLE);
			leftMidline = leftMidline.add(iconBounds.x + spaceBounds.x, 0);
			context.drawText(label, camera, leftMidline, textColor, fontFace, fontSize, TextAlign.LEFT_MIDDLE);
		} else {
			if (icon != null) {
				context.drawText(icon.text, camera, new Vector2d(getBounds().getCenter()), textColor, FontAwesome.NAME, fontSize, TextAlign.CENTER_MIDDLE);
			}
			if (label != null) {
				context.drawText(label, camera, new Vector2d(getBounds().getCenter()), textColor, fontFace, fontSize, TextAlign.CENTER_MIDDLE);
			}
		}
	}

}
