package com.digiturtle.graphics.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.Camera;
import com.digiturtle.graphics.Palette;
import com.digiturtle.graphics.RenderingContext;
import com.digiturtle.graphics.ui.widgets.*;

public class UserInterface implements InputEventListener {

	private Widget rootNode;
	
	private Model model;
	
	private long timeMillis = 0;
	
	private Palette palette;
	
	public UserInterface(Model sharedModel, Palette palette) {
		model = new Model(sharedModel);
		this.palette = palette;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Palette getPalette() {
		return palette;
	}

	public Widget getRootNode() {
		return rootNode;
	}

	public void setRootNode(Widget rootNode) {
		this.rootNode = rootNode;
	}

	public void render(Camera camera, RenderingContext context) {
		if (timeMillis > 0) {
			//Logger.debug("UserInterface::update()", getRootNode());
			float delta = (float) (System.currentTimeMillis() - timeMillis) / 1000f;
			rootNode.update(delta);
		}
		timeMillis = System.currentTimeMillis();
		rootNode.render(camera, context);
	}

	public boolean processInput(InputEvent event) {
		return rootNode.processInput(event);
	}
	
	@FunctionalInterface
	public interface UiSelector {
		public Collection<Widget> select(String... selector);
	}
	
	public static Collection<Widget> selectMulti(UserInterface[] interfaces, String... selector) {
		Collection<Widget> combined = new ArrayList<>();
		for (int i = 0; i < interfaces.length; i++) {
			Collection<Widget> perInterface = interfaces[i].select(selector);
			if (perInterface != null) {
				combined.addAll(perInterface);
			}
		}
		return combined;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Widget> T selectFirstOfType(Class<T> type, String... selector) {
		return (T) select(selector).stream().filter(widget -> widget.getClass().isAssignableFrom(type)).findFirst().get();
	}

	public Collection<Widget> select(String... selector) {
		if (!selector[0].equalsIgnoreCase(rootNode.getId())) {
			return null;
		}
		return select(selector, 0, rootNode);
	}

	private Collection<Widget> select(String[] selector, int index, Widget node) {
		Class<?> type = node.getClass();
		if (index == selector.length - 1) {
			if (selector[index].equalsIgnoreCase(node.getId()) || selector[index].equalsIgnoreCase("?")) {
				return Arrays.asList(node);
			}
			return null;
		}
		if (type == TabbedPane.class) {
			Collection<Widget> result = new ArrayList<Widget>();
			TabbedPane pane = (TabbedPane) node;
			for (Container container : pane.getContents()) {
				Collection<Widget> found = select(selector, index + 1, container);
				if (found != null) {
					result.addAll(found);
				}
			}
			return result;
		}
		if (type == ScrollableWidget.class) {
			ScrollableWidget scrollable = (ScrollableWidget) node;
			return select(selector, index + 1, scrollable.getContents());
		}
		if (type == Container.class) {
			Collection<Widget> result = new ArrayList<Widget>();
			Container container = (Container) node;
			for (int i = 0; i < container.getChildren().size(); i++) {
				Collection<Widget> found = select(selector, index + 1, container.getChildren().get(i));
				if (found != null) {
					result.addAll(found);
				}
			}
			return result;
		}
		else if (type == Modal.class) {
			if (selector[index].equalsIgnoreCase(node.getId())) {
				return select(selector, index + 1, ((Modal) node).getContainer());
			}
			return null;
		}
		else if (type == DraggableSurface.class) {
			return select(selector, index + 1, ((DraggableSurface) node).getSurface());
		}
		else {
			return null;
		}
	}

}
