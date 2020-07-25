package com.digiturtle.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.joml.Matrix3x2f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL11;

import com.digiturtle.core.Logger;
import com.digiturtle.core.ManagedResource;
import com.digiturtle.graphics.ui.widgets.Image;
import com.digiturtle.math.Ellipse;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.RegularPolygon;
import com.digiturtle.math.RightTrapezoid;
import com.digiturtle.math.Shape;

public class RenderingContext extends ManagedResource {
	
	private long nanoVG3Context;
	
	private HashMap<Image, NVGPaint> paints = new HashMap<>();
	private HashMap<RegularPolygon, NVGColor> polygonPaints = new HashMap<>();
	
	private HashMap<Integer, Vector2i> textureSizes = new HashMap<>();
	
	private long windowHandle;
	
	public RenderingContext(long windowHandle) {
		//nanoVG3Context = NanoVGGL3.nvgCreate(0 | NanoVGGL3.NVG_ANTIALIAS);
		if (GL11.glGetString(GL11.GL_VERSION).startsWith("2")) {
			nanoVG3Context = NanoVGGL2.nvgCreate(0 | NanoVGGL2.NVG_ANTIALIAS);
		} else {
			nanoVG3Context = NanoVGGL3.nvgCreate(0 | NanoVGGL3.NVG_ANTIALIAS);
		}
		this.windowHandle = windowHandle;
	}
	
	public void setScissor(Shape region, Camera camera) {
		Vector2d position = region.getTopLeft();
		Vector2d size = new Vector2d(region.getWidth(), region.getHeight());
		translateAndScale(position, size, camera);
		NanoVG.nvgScissor(nanoVG3Context, (float) position.x, (float) position.y, (float) size.x, (float) size.y);
	}
	
	public void clearScissor() {
		NanoVG.nvgResetScissor(nanoVG3Context);
	}
	
	public void setCursor(Cursor cursor) {
		GLFW.glfwSetCursor(windowHandle, cursor.handle);
	}
	
	public int createImageHandle(Texture texture) {
		int handle = NanoVG.nvgCreateImageRGBA(nanoVG3Context, texture.getWidth(), texture.getHeight(), 
				NanoVG.NVG_IMAGE_GENERATE_MIPMAPS | NanoVG.NVG_IMAGE_PREMULTIPLIED | 
				(texture.isRepeated() ? (NanoVG.NVG_IMAGE_REPEATX | NanoVG.NVG_IMAGE_REPEATY) : 0),
				texture.getData());
		texture.wasCreated();
		if (texture.isRepeated()) {
			Vector2i size = new Vector2i((int) (texture.getScale() * texture.getWidth()),
					(int) (texture.getScale() * texture.getHeight()));
			textureSizes.put(handle, size);
		}
		return handle;
	}
	
	public int createFont(ByteBuffer buffer, String name) {
		return NanoVG.nvgCreateFontMem(nanoVG3Context, name, buffer, 1);
	}
	
	public void start(float windowWidth, float windowHeight) {
		NanoVG.nvgBeginFrame(nanoVG3Context, windowWidth, windowHeight, 1);
		NanoVG.nvgGlobalCompositeBlendFunc(nanoVG3Context, NanoVG.NVG_SRC_ALPHA, NanoVG.NVG_ONE_MINUS_SRC_ALPHA);
	}
	
	public void end() {
		NanoVG.nvgEndFrame(nanoVG3Context);
	}
	
	private void translateAndScale(Vector2d position, Vector2d bounds, Camera camera) {
		position.add(camera.getTranslation()).mul(camera.getScale());
		bounds.mul(camera.getScale());
		position.round();
		bounds.round();
	}
	
	private void rotate(float angle, Vector2f point) {
		//Logger.debug("RenderingContext.rotate()", angle, point);
		NanoVG.nvgTranslate(nanoVG3Context, point.x, point.y);
		NanoVG.nvgRotate(nanoVG3Context, angle);
//		NanoVG.nvgTranslate(nanoVG3Context, -point.x, -point.y);
	}
	
	public void drawImage(Rectangle rectangle, Image image, Camera camera, double radius) {
		drawImage(rectangle, image, camera, radius, 1);
	}
	
	public void drawImage(Rectangle rectangle, Image image, Camera camera, double radius, float alpha) {
		if (!paints.containsKey(image)) {
			NVGPaint paint = NVGPaint.create();
			paints.put(image, paint);
		}
		Vector2d position = new Vector2d(rectangle.getX(), rectangle.getY());
		Vector2d size = new Vector2d(rectangle.getWidth(), rectangle.getHeight());
		translateAndScale(position, size, camera);
		NVGPaint imgPaint = paints.get(image);
		Vector2i textureSize = textureSizes.get(image.getPointer());
		if (textureSize != null) { // Tiled texture using overridden image pattern size
			NanoVG.nvgImagePattern(nanoVG3Context, (float) position.x, (float) position.y,
					(float) textureSize.x, (float) textureSize.y, image.getRotation(), image.getPointer(), alpha, imgPaint);
		} else {
			NanoVG.nvgImagePattern(nanoVG3Context, (float) position.x, (float) position.y,
					(float) size.x, (float) size.y, image.getRotation(), image.getPointer(), alpha, imgPaint);	
		}
		NanoVG.nvgBeginPath(nanoVG3Context);
		Vector2d center = rectangle.getCenter();
		NanoVG.nvgSave(nanoVG3Context);
		translateAndScale(center, new Vector2d(), camera);
		NanoVG.nvgTranslate(nanoVG3Context, (float) center.x, (float) center.y);
		NanoVG.nvgTranslate(nanoVG3Context, (float) -size.x / 2, (float) -size.y / 2);
		NanoVG.nvgRotate(nanoVG3Context, (float) image.getRotation());
		NanoVG.nvgTranslate(nanoVG3Context, (float) size.x / 2, (float) size.y / 2);
//		rotate(image.getRotation(), new Vector2f((float) center.x, (float) center.y));
		if (radius == 0) {
			NanoVG.nvgRect(nanoVG3Context, (float) -size.x / 2, (float) -size.y / 2, //(float) position.x, (float) position.y,
					(float) size.x, (float) size.y);
		} else {
			NanoVG.nvgRoundedRect(nanoVG3Context, (float) -size.x / 2, (float) -size.y / 2, //(float) position.x, (float) position.y,
					(float) size.x, (float) size.y, (float) radius);
		}
//		NanoVG.nvgTranslate(nanoVG3Context, (float) -position.x, (float) -position.y);
//		rotate(-image.getRotation(), new Vector2f((float) center.x, (float) center.y));
		NanoVG.nvgRestore(nanoVG3Context);
		NanoVG.nvgClosePath(nanoVG3Context);
		NanoVG.nvgFillPaint(nanoVG3Context, imgPaint);
		NanoVG.nvgFill(nanoVG3Context);
	}
	
	private NVGColor stroke = null;
	public void traceBoundary(Shape shape, float[] fill, Camera camera, double strokeWidth, double radius) {
		if (stroke == null) {
			stroke = NVGColor.create();
		}
		stroke.r(fill[0]).g(fill[1]).b(fill[2]).a(fill[3]);
		NanoVG.nvgBeginPath(nanoVG3Context);
		if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			Vector2d position = new Vector2d(rectangle.getX(), rectangle.getY());
			Vector2d size = new Vector2d(rectangle.getWidth(), rectangle.getHeight());
			translateAndScale(position, size, camera);
			if (radius == 0) {
				NanoVG.nvgRect(nanoVG3Context, (float) position.x, (float) position.y,
						(float) size.x, (float) size.y);
			} else {
				NanoVG.nvgRoundedRect(nanoVG3Context, (float) position.x, (float) position.y,
						(float) size.x, (float) size.y, (float) radius);
			}
		}
		else {
			throw new IllegalStateException("Cannot trace non-Rectangular shapes yet!");
		}
		NanoVG.nvgClosePath(nanoVG3Context);
		NanoVG.nvgStrokeColor(nanoVG3Context, stroke);
		NanoVG.nvgStrokeWidth(nanoVG3Context, (float) strokeWidth);
		NanoVG.nvgStroke(nanoVG3Context);
	}
	
	private NVGColor background = null;
	public void drawShape(Shape shape, float[] fill, Camera camera, double radius) {
		if (background == null) {
			background = NVGColor.create();
		}
		background.r(fill[0]).g(fill[1]).b(fill[2]).a(fill[3]);
		NanoVG.nvgBeginPath(nanoVG3Context);
		if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			Vector2d position = new Vector2d(rectangle.getX(), rectangle.getY());
			Vector2d size = new Vector2d(rectangle.getWidth(), rectangle.getHeight());
			translateAndScale(position, size, camera);
			if (radius == 0) {
				NanoVG.nvgRect(nanoVG3Context, (float) position.x, (float) position.y,
						(float) size.x, (float) size.y);
			} else {
				NanoVG.nvgRoundedRect(nanoVG3Context, (float) position.x, (float) position.y,
						(float) size.x, (float) size.y, (float) radius);
			}
		}
		else if (shape instanceof Ellipse) {
			Ellipse ellipsoid = (Ellipse) shape;
			Vector2d position = new Vector2d(ellipsoid.getCenter().x(), ellipsoid.getCenter().y());
			Vector2d size = new Vector2d(ellipsoid.getWidth(), ellipsoid.getHeight());
			translateAndScale(position, size, camera);
			NanoVG.nvgEllipse(nanoVG3Context, (float) position.x, (float) position.y,
					(float) size.x, (float) size.y);
			
		}
		else if (shape instanceof RightTrapezoid) {
			RightTrapezoid trapezoid = (RightTrapezoid) shape;
			Vector2d point1 = new Vector2d(trapezoid.getX(), trapezoid.getY());
			Vector2d point2 = new Vector2d(trapezoid.getX() + trapezoid.getTopWidth(), trapezoid.getY());
			Vector2d point3 = new Vector2d(trapezoid.getX() + trapezoid.getBottomWidth(), trapezoid.getY() + trapezoid.getHeight());
			Vector2d point4 = new Vector2d(trapezoid.getX(), trapezoid.getY() + trapezoid.getHeight());
			translateAndScale(point1, new Vector2d(), camera);
			translateAndScale(point2, new Vector2d(), camera);
			translateAndScale(point3, new Vector2d(), camera);
			translateAndScale(point4, new Vector2d(), camera);
			NanoVG.nvgMoveTo(nanoVG3Context, (float) point1.x, (float) point1.y);
			NanoVG.nvgLineTo(nanoVG3Context, (float) point2.x, (float) point2.y);
			NanoVG.nvgLineTo(nanoVG3Context, (float) point3.x, (float) point3.y);
			NanoVG.nvgLineTo(nanoVG3Context, (float) point4.x, (float) point4.y);
		}
		NanoVG.nvgClosePath(nanoVG3Context);
		NanoVG.nvgFillColor(nanoVG3Context, background);
		NanoVG.nvgFill(nanoVG3Context);
	}
	
	private NVGColor border2 = null;
	private NVGPaint fill2;
	public void drawPolygon(RegularPolygon polygon, Camera camera, int textureHandle, float[] stroke) {
		if (border2 == null) {
			border2 = NVGColor.create();
			fill2 = NVGPaint.create();
		}
		//fill2.image(textureHandle);
		Vector2d position = polygon.getTopLeft();
		Vector2d size = new Vector2d(polygon.getWidth(), polygon.getHeight());
		translateAndScale(position, size, camera);
		NanoVG.nvgImagePattern(nanoVG3Context, (float) position.x, (float) position.y,
				(float) size.x, (float) size.y, 0, textureHandle, 1, fill2);
		border2.r(stroke[0]).g(stroke[1]).b(stroke[2]).a(stroke[3]);
		NanoVG.nvgBeginPath(nanoVG3Context);
		position.set(polygon.getPoint(0).x(), polygon.getPoint(0).y());
		size.set(0, 0);
		translateAndScale(position, size, camera);
		NanoVG.nvgMoveTo(nanoVG3Context, (float) position.x, (float) position.y);
		for (int i = 1; i < polygon.getNumberOfSides(); i++) {
			position.set(polygon.getPoint(i));
			translateAndScale(position, size, camera);
			NanoVG.nvgLineTo(nanoVG3Context, (float) position.x, (float) position.y);
		}
		NanoVG.nvgClosePath(nanoVG3Context);
		NanoVG.nvgFillPaint(nanoVG3Context, fill2);
		NanoVG.nvgStrokeColor(nanoVG3Context, border2);
		NanoVG.nvgFill(nanoVG3Context);
		NanoVG.nvgStroke(nanoVG3Context);
	}
	
	private NVGColor border = null;
	public void drawPolygon(RegularPolygon polygon, Camera camera, float[] fill, float[] stroke) {
		if (border == null) {
			border = NVGColor.create();
		}
		if (!polygonPaints.containsKey(polygon)) {
			NVGColor paint = NVGColor.create();
			polygonPaints.put(polygon, paint);
		}
		NVGColor polygonPaint = polygonPaints.get(polygon);
		polygonPaint.r(fill[0]).g(fill[1]).b(fill[2]).a(fill[3]);
		border.r(stroke[0]).g(stroke[1]).b(stroke[2]).a(stroke[3]);
		NanoVG.nvgBeginPath(nanoVG3Context);
		Vector2d position = new Vector2d(polygon.getPoint(0).x(), polygon.getPoint(0).y());
		Vector2d size = new Vector2d();
		translateAndScale(position, size, camera);
		NanoVG.nvgMoveTo(nanoVG3Context, (float) position.x, (float) position.y);
		for (int i = 1; i < polygon.getNumberOfSides(); i++) {
			position.set(polygon.getPoint(i));
			translateAndScale(position, size, camera);
			NanoVG.nvgLineTo(nanoVG3Context, (float) position.x, (float) position.y);
		}
		NanoVG.nvgClosePath(nanoVG3Context);
		NanoVG.nvgFillColor(nanoVG3Context, polygonPaint);
		NanoVG.nvgStrokeColor(nanoVG3Context, border);
		NanoVG.nvgFill(nanoVG3Context);
		NanoVG.nvgStroke(nanoVG3Context);
	}
	
	private NVGColor textColorNVG = null;
	public void drawText(String text, Camera camera, Vector2d position, float[] textColor, String fontFace, int fontSize, TextAlign align) {
		if (textColorNVG == null) {
			textColorNVG = NVGColor.create();
		}
		textColorNVG.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(textColor[3]);
		NanoVG.nvgFontSize(nanoVG3Context, (int) ((double) fontSize * camera.getScale()));
		NanoVG.nvgFontFace(nanoVG3Context, fontFace);
		NanoVG.nvgTextAlign(nanoVG3Context, align.horizontal | align.vertical);
//		NanoVG.nvgBeginPath(nanoVG3Context);
		NanoVG.nvgFillColor(nanoVG3Context, textColorNVG);
		Vector2d positionTransformed = new Vector2d(position);
		Vector2d size = new Vector2d();
		translateAndScale(positionTransformed, size, camera);
		NanoVG.nvgText(nanoVG3Context, (float) positionTransformed.x, (float) positionTransformed.y, text);
//		NanoVG.nvgClosePath(nanoVG3Context);
	}
	
	public Vector2d getTextBounds(String text, String fontFace, int fontSize, Camera camera) {
		NanoVG.nvgFontSize(nanoVG3Context, (int) ((double) fontSize * camera.getScale()));
		NanoVG.nvgFontFace(nanoVG3Context, fontFace);
		float[] bounds = new float[4];
		NanoVG.nvgTextBounds(nanoVG3Context, 0, 0, text, bounds);
		return new Vector2d(bounds[2] - bounds[0], bounds[3] - bounds[1]);

	}

	@Override
	public long getPointer() {
		return nanoVG3Context;
	}

	@Override
	public void dispose() {
		NanoVGGL3.nvgDelete(nanoVG3Context);
	}

}
