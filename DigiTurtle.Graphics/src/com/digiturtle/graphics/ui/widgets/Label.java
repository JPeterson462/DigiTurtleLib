package com.digiturtle.graphics.ui.widgets;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Font;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.TextAlign;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;

public class Label extends Widget {
	
	private String text;
	
	private float[] textColor;
	
	private String fontFace;
	
	private int fontSize;
	
	private TextAlign align;
	
	private Pattern iconReplacementRegex;

	public Label(String id) {
		super(id);
		iconReplacementRegex = Pattern.compile("(icon:[A-Za-z-]+)");
	}
	
	public void setFont(String fontFace, int fontSize) {
		this.fontFace = fontFace;
		this.fontSize = fontSize;
	}
	
	public void setAlignment(TextAlign align) {
		this.align = align;
	}
	
	public void setPosition(double x, double y) {
		setBounds(new Rectangle(x, y, 1, 1));
	}
	
	public void setTextColor(float[] textColor) {
		this.textColor = textColor;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	@Override
	public boolean processInput(InputEvent event) {
		setHovered(event.containedIn(getBounds()));
		return false;
	}
	
	private String replaceIcons(String text) {
		ArrayList<String> replacements = new ArrayList<>();
		Matcher matcher = iconReplacementRegex.matcher(text);
		while (matcher.find()) {
			replacements.add(matcher.group(1));
		}
		for (int i = 0; i < replacements.size(); i++) {
			String match = replacements.get(i);
			String icon = FontAwesome.valueOf(match.substring(match.indexOf(':') + 1).replace('-', '_').toUpperCase()).text;
			text = text.replace(match, icon);
		}
		return text;
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Font font = Font.findFont(fontFace);
		font.tryCreate(context);
		String bindingValue = getModelValue();
		String resultText = replaceIcons(bindingValue != null ? bindingValue : text);
		context.drawText(resultText, camera, ((Rectangle) getBounds()).getXY(), textColor, fontFace, fontSize, align);
		Rectangle rectangle = (Rectangle) getBounds();
		Vector2d size = context.getTextBounds(resultText, fontFace, fontSize, camera);
		rectangle.resize(size.x, size.y);
	}

}
