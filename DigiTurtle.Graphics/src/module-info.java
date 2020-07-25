module digiturtlegraphics {
	exports com.digiturtle.graphics;
	exports com.digiturtle.graphics.ui;
	exports com.digiturtle.graphics.ui.widgets;
	exports com.digiturtle.input;
	exports com.digiturtle.screens;
	requires java.desktop;
	requires org.json;
	requires org.lwjgl;
	requires org.joml;
	requires digiturtlecore;
	requires org.lwjgl.opengl;
	requires org.lwjgl.stb;
	requires org.lwjgl.glfw;
	requires org.lwjgl.nanovg;
}