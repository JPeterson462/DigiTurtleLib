package com.digiturtle.graphics.ui.widgets;

import java.util.ArrayList;

import org.joml.Vector2d;

import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.InputEvent;
import com.digiturtle.graphics.ui.Widget;
import com.digiturtle.math.Rectangle;

public class DataTable extends Widget {
	
	private ArrayList<Widget[]> widgets;
	
	private double[] columnWidths;
	
	private double rowHeight;
	
	private float[][] backgroundColors;

	public DataTable(String id) {
		super(id);
		widgets = new ArrayList<>();
	}
	
	public void clear() {
		widgets.clear();
	}
	
	public void setDimensions(double rowHeight, double[] columnWidths) {
		this.rowHeight = rowHeight;
		this.columnWidths = columnWidths;
	}
	
	public void setBackgroundColors(float[][] backgroundColors) {
		this.backgroundColors = backgroundColors;
	}
	
	public void addRow(Widget... row) {
		widgets.add(row);
		Rectangle rectangle = (Rectangle) getBounds();
		rectangle.height = rowHeight * widgets.size();
	}

	@Override
	public boolean processInput(InputEvent event) {
		Vector2d topLeft = getBounds().getTopLeft();
		event = event.translate(-topLeft.x, -topLeft.y);
		double offsetX = 0, offsetY = 0;
		for (int row = 0; row < widgets.size(); row++) {
			offsetX = 0;
			Widget[] list = widgets.get(row);
			for (int column = 0; column < list.length; column++) {
				if (list[column] != null) {
					if (list[column].processInput(event.translate(-offsetX, -offsetY))) {
						return true;
					}
				}
				offsetX += getBounds().getWidth() * columnWidths[column];
			}
			offsetY += rowHeight;
		}
		return false;
	}
	
	private float[] getBackgroundColor(int row) {
		return backgroundColors[row % backgroundColors.length];
	}

	@Override
	public void render(Camera camera, RenderingContext context) {
		Vector2d topLeft = getBounds().getTopLeft();
		camera.translate(topLeft.x, topLeft.y);
		double offsetX = 0, offsetY = 0;
		for (int row = 0; row < widgets.size(); row++) {
			offsetX = 0;
			Widget[] list = widgets.get(row);
			context.drawShape(new Rectangle(0, offsetY, getBounds().getWidth(), rowHeight), getBackgroundColor(row), camera, 0);
			for (int column = 0; column < list.length; column++) {
				if (list[column] != null) {
					camera.translate(offsetX, offsetY);
					list[column].render(camera, context);
					camera.translate(-offsetX, -offsetY);
				}
				offsetX += getBounds().getWidth() * columnWidths[column];
			}
			offsetY += rowHeight;
		}
		camera.translate(-topLeft.x, -topLeft.y);
	}

}
