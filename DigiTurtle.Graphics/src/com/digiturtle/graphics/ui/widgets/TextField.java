package com.digiturtle.graphics.ui.widgets;

import java.util.Arrays;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Cursor;
import com.digiturtle.graphics.Font;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.TextAlign;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.InputEvent.ClickEvent;
import com.digiturtle.graphics.ui.InputEvent.DragEvent;
import com.digiturtle.graphics.ui.InputEvent.DragStartEvent;
import com.digiturtle.graphics.ui.InputEvent.KeyEvent;
import com.digiturtle.graphics.ui.InputEvent.MouseMoveEvent;
import com.digiturtle.input.InputAdapter;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.DataUtils;
import com.digiturtle.math.MathUtils;
import com.digiturtle.math.Rectangle;

public class TextField extends Widget {
	
	private String fontFace;
	
	private int fontSize;
	
	private float[] backgroundColor, selectionColor, textColor;
	
	//private StringBuffer buffer = new StringBuffer();
	private InputBuffer buffer = new InputBuffer();
	
	private int cursor = 0;
	
	private boolean active = false;
	
	private long timestamp = 0;
	
	private Selection selection = null;
	
	private boolean inTextbox = false;
	
	private double shift = 0;
	
	private Rectangle visibleRegion;
	
	private char mask = 0;
	
	public class Selection {
		int start, end;
	}
	
	public class InputBuffer {
		
		private StringBuffer unmasked = new StringBuffer();
		private StringBuffer masked = new StringBuffer();
		
		private String repeat(char c, int times) {
			char[] str = new char[times];
			Arrays.fill(str, c);
			return new String(str);
		}

		public void delete(int min, int max) {
			unmasked.delete(min, max);
			masked.delete(min, max);
		}

		public void insert(int cursor, String text) {
			unmasked.insert(cursor, text);
			masked.insert(cursor, mask != 0 ? repeat(mask, text.length()) : text);
		}

		public int length() {
			return masked.length();
		}
		
		public String toString() {
			return unmasked.toString();
		}

		public String substring(int min, int max) {
			return masked.substring(min, max);
		}
		
	}
	
	// Cached for input processing
	private RenderingContext context;
	private Camera camera;

	public TextField(String id) {
		super(id);
	}
	
	public void setMask(char c) {
		mask = c;
	}
	
	public void setBackgroundColor(float[] backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setSelectionColor(float[] selectionColor) {
		this.selectionColor = selectionColor;
	}
	
	public void setFont(String fontFace, int fontSize) {
		this.fontFace = fontFace;
		this.fontSize = fontSize;
	}
	
	public void setTextColor(float[] textColor) {
		this.textColor = textColor;
	}
	
	private void insert(String text) {
		if (selection != null) {
			buffer.delete(Math.min(selection.start, selection.end), Math.max(selection.start, selection.end));
			if (cursor > Math.min(selection.start, selection.end)) {
				cursor = Math.min(selection.start, selection.end);
			}
			selection = null;
		}
		buffer.insert(cursor, text);
		cursor += text.length();
	}
	
	private String getSelection() {
		if (selection != null) {
			return buffer.substring(Math.min(selection.start, selection.end), Math.max(selection.start, selection.end));
		}
		return "";
	}
	
	private int getBufferPosition(double x) {
		int position = 0;
		for (position = 0; position <= buffer.length(); position++) {
			if (getCursorOffset(position) > x) {
				position--;
				break;
			}
		}
		return position;
	}

	@Override
	public boolean processInput(InputEvent event) {
		if (event instanceof MouseMoveEvent) {
			boolean nowInTextbox = event.containedIn(getBounds());
			if (inTextbox ^ nowInTextbox) {
				context.setCursor(nowInTextbox ? Cursor.IBEAM : Cursor.ARROW);
			}
			inTextbox = nowInTextbox;
		}
		if (active) {
			if (event instanceof DragStartEvent) {
				event = event.translate(-shift, 0);
				selection = new Selection();
				selection.start = getBufferPosition(((DragStartEvent) event).x);
				selection.end = selection.start;
			}
			if (event instanceof DragEvent) {
				event = event.translate(-shift, 0);
				selection.end = getBufferPosition(((DragEvent) event).cx);
				cursor = selection.end;
			}
			if (event instanceof ClickEvent) {
				if (!event.containedIn(getBounds())) {
					active = false;
					selection = null;
				} else {
					Vector2d topLeft = getBounds().getTopLeft();
					event = event.translate(-shift, 0);
					event = event.translate(-topLeft.x, -topLeft.y);
					double offset = (getBounds().getHeight() - fontSize) * 0.5;
					event = event.translate(-offset, -offset);
					double x = ((ClickEvent) event).x;
					selection = null;
					int position = getBufferPosition(x);
					cursor = position;
					timestamp = System.currentTimeMillis();
				}
			}
			if (event instanceof KeyEvent) {
				KeyEvent keyEvent = (KeyEvent) event;
				String key = keyEvent.text;
				if (key == null) {
					return false;
				}
				int mods = keyEvent.mods;
				//System.out.println("KEY: " + key);
				if (key.length() == 1 && (mods & InputAdapter.CTRL) == 0) {
					insert(key);
				}
				else if ((mods & InputAdapter.CTRL) != 0) {
					if (key.equalsIgnoreCase("V")) {
						insert(DataUtils.getClipboard());
						selection = null;
					}
					else if (key.equalsIgnoreCase("C")) {
						DataUtils.setClipboard(getSelection());
						selection = null;
					}
					else if (key.equalsIgnoreCase("X")) {
						DataUtils.setClipboard(getSelection());
						insert("");
						selection = null;
					}
				}
				else if (key.equalsIgnoreCase("+LEFT")) {
					cursor = MathUtils.clamp(0, cursor - 1, buffer.length());
					if ((mods & (InputAdapter.SHIFT)) != 0) {
						if (selection != null) {
							selection.end = cursor;
						} else {
							selection = new Selection();
							selection.start = MathUtils.clamp(0, cursor + 1, buffer.length());
							selection.end = cursor;
						}
					}
				} 
				else if (key.equalsIgnoreCase("+RIGHT")) {
					cursor = MathUtils.clamp(0, cursor + 1, buffer.length());
					if ((mods & (InputAdapter.SHIFT)) != 0) {
						if (selection != null) {
							selection.end = cursor;
						} else {
							selection = new Selection();
							selection.start = MathUtils.clamp(0, cursor - 1, buffer.length());
							selection.end = cursor;
						}
					}
				}
				else if (key.equalsIgnoreCase("+BACKSPACE")) {
					if (selection != null) {
						insert("");
					} else {
						cursor = MathUtils.clamp(0, cursor - 1, buffer.length() - 1);
						buffer.delete(cursor, cursor + 1);
					}
				}
				else if (key.equalsIgnoreCase("+DELETE")) {
					if (selection != null) {
						insert("");
					} else {
						buffer.delete(cursor, cursor + 1);
					}
				}
			}
		} else {
			if (event.containedIn(getBounds())) {
				if (event instanceof ClickEvent) {
					active = true;
					timestamp = System.currentTimeMillis();
				}
			}
		}
		return false;
	}
	
	private double getCursorOffset(int position) {
		return Math.max(0, context.getTextBounds(buffer.substring(0, Math.min(buffer.length(), position)), fontFace, fontSize, camera).x
				- context.getTextBounds("|", fontFace, fontSize, camera).x);
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		this.camera = camera;
		this.context = context;
		model.setValue(getBinding(), buffer.toString());
		Font font = Font.findFont(fontFace);
		font.tryCreate(context);
		context.drawShape(getBounds(), backgroundColor, camera, 0);
		Vector2d topLeft = getBounds().getTopLeft();
		double offset = (getBounds().getHeight() - fontSize) * 0.5;
		visibleRegion = new Rectangle(0, 0, getBounds().getWidth() - 2 * offset, getBounds().getHeight() - 2 * offset);
		camera.translate(topLeft.x, topLeft.y);
		camera.translate(offset, offset);
		context.setScissor(visibleRegion, camera);
		if (selection != null) {
			double x1 = getCursorOffset(selection.start), x2 = getCursorOffset(selection.end);
			Rectangle region = new Rectangle(Math.min(x1, x2), 0, Math.abs(x2 - x1), fontSize);
			context.drawShape(region, selectionColor, camera, 0);
		}
		double cursorOffset = getCursorOffset(cursor);
		if (cursorOffset - shift > getBounds().getWidth() - 2 * offset) {
			shift = (getBounds().getWidth() - 2 * offset) - (cursorOffset + offset);
		}
		else if (cursorOffset - shift < 0) {
			shift = cursorOffset + offset;
		}
		double stored_shift = shift;
		camera.translate(stored_shift, 0);
		context.drawText(buffer.masked.toString(), camera, new Vector2d(0, 0), textColor, fontFace, fontSize, TextAlign.LEFT_TOP);
		if (active) {
			if (((System.currentTimeMillis() - timestamp) % 1000) < 500 || selection != null) {
				context.drawText("|", camera, new Vector2d(cursorOffset, 0), textColor, fontFace, fontSize, TextAlign.LEFT_TOP);
			}
		}
		context.clearScissor();
		camera.translate(-stored_shift, 0);
		camera.translate(-offset, -offset);
		camera.translate(-topLeft.x, -topLeft.y);
	}

}
