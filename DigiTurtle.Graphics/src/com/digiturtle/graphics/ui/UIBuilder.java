package com.digiturtle.graphics.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digiturtle.core.Logger;
import com.digiturtle.graphics.FontAwesome;
import com.digiturtle.graphics.Palette;
import com.digiturtle.graphics.TextAlign;
import com.digiturtle.graphics.ui.widgets.Button;
import com.digiturtle.graphics.ui.widgets.ClickableHex;
import com.digiturtle.graphics.ui.widgets.Container;
import com.digiturtle.graphics.ui.widgets.DataTable;
import com.digiturtle.graphics.ui.widgets.DraggableSurface;
import com.digiturtle.graphics.ui.widgets.Dropdown;
import com.digiturtle.graphics.ui.widgets.EmptySpaceWidget;
import com.digiturtle.graphics.ui.widgets.IconAndLabelButton;
import com.digiturtle.graphics.ui.widgets.Image;
import com.digiturtle.graphics.ui.widgets.ImageButton;
import com.digiturtle.graphics.ui.widgets.Label;
import com.digiturtle.graphics.ui.widgets.Modal;
import com.digiturtle.graphics.ui.widgets.ScrollableWidget;
import com.digiturtle.graphics.ui.widgets.TabbedPane;
import com.digiturtle.graphics.ui.widgets.TextField;
import com.digiturtle.graphics.ui.widgets.Tooltip;
import com.digiturtle.graphics.ui.widgets.ValueSlider;
import com.digiturtle.graphics.ui.widgets.ValueSlider.Direction;
import com.digiturtle.graphics.ui.widgets.ZoomSlider;
import com.digiturtle.math.DataUtils;
import com.digiturtle.math.Rectangle;
import com.digiturtle.math.Shape;

public class UIBuilder {
	
	private String json;
	
	@FunctionalInterface
	private interface WidgetBuilder {
		public Widget create(JSONObject object, Model model, Container container);
	}
	
	private HashMap<String, WidgetBuilder> builders = new HashMap<>();
	
	private Palette palette;
	
	private BroadcastListener broadcastListener;
	
	private Pattern paletteRegex = Pattern.compile("([^\\[]+)\\[([\\d]+)\\]");
	
	public UIBuilder(BroadcastListener broadcastListener, JSONObject palette) {
		this.palette = new Palette(palette);
		this.broadcastListener = broadcastListener;
		builders.put("DraggableSurface", this::createDraggableSurface);
		builders.put("Container", this::createContainer);
		builders.put("Image", this::createImage);
		builders.put("ClickableHex", this::createClickableHex);
		builders.put("Label", this::createLabel);
		builders.put("Modal", this::createModal);
		builders.put("Button", this::createButton);
		builders.put("ImageButton", this::createImageButton);
		builders.put("IconAndLabelButton", this::createIconAndLabelButton);
		builders.put("ValueSlider", this::createValueSlider);
		builders.put("ZoomSlider", this::createZoomSlider);
		builders.put("EmptySpace", this::createEmptySpace);
		builders.put("Tooltip", this::createTooltip);
		builders.put("TabbedPane", this::createTabbedPane);
		builders.put("DataTable", this::createDataTable);
		builders.put("TextField", this::createTextField);
		builders.put("Scrollable", this::createScrollable);
		builders.put("Dropdown", this::createDropdown);
	}
	
	private Rectangle parseBounds(JSONArray array) {
		return new Rectangle(array.getInt(0), array.getInt(1), array.getInt(2), array.getInt(3));
	}
	
	private float[] parseColor(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			if (string.startsWith("palette:")) {
				String data = string.substring("palette:".length());
				String factor = "";
				if (data.contains("]+") || data.contains("]-")) {
					factor = data.substring(data.lastIndexOf(']') + 1);
					data = data.substring(0, data.length() - factor.length());
				}
				System.out.println(string + " => " + factor + ", " + data);
				Matcher matcher = paletteRegex.matcher(data);
				if (matcher.matches()) {
					String color = matcher.group(1);
					int index = Integer.parseInt(matcher.group(2));
					int tinting = 0;
					if (factor.length() > 0) {
						tinting = Integer.parseInt(factor);
					}
					return palette.getColor(color, index, tinting);
				}
			}
		}
		return DataUtils.readFromJson(object);
	}
	
	private double[] parseDoubleArray(JSONArray array) {
		double[] data = new double[array.length()];
		for (int i = 0; i < data.length; i++) {
			data[i] = array.getDouble(i);
		}
		return data;
	}
	
	private DraggableSurface createDraggableSurface(JSONObject object, Model model, Container container) {
		//Logger.debug("DraggableSurface");
		DraggableSurface surface = new DraggableSurface(object.getString("id"));
		if (object.has("z-index")) {
			surface.setZIndex(object.getInt("z-index"));
		}
		surface.setBounds(parseBounds(object.getJSONArray("bounds")));
		if (object.has("binding")) {
			surface.setModel(model, object.getString("binding"));
		}
		JSONObject subobject = object.getJSONObject("surface");
		Widget instance = builders.get(subobject.getString("$type")).create(subobject, model, container);
		surface.setSurface(instance);
		return surface;
	}
	
	private Container createContainer(JSONObject object, Model model, Container parent) {
		//Logger.debug("Container");
		Container container = new Container(object.getString("id"));
		if (object.has("z-index")) {
			container.setZIndex(object.getInt("z-index"));
		}
		container.setBackgroundColor(parseColor(object.get("background-color")));
		if (object.has("binding")) {
			container.setModel(model, object.getString("binding"));
		} else {
			container.setModel(model, "UNKNOWN");
		}
		if (object.has("rounded")) {
			container.setRounded(object.getBoolean("rounded"));
		}
		container.setBounds(parseBounds(object.getJSONArray("bounds")));
		if (object.get("children") instanceof JSONArray) {
			JSONArray children = object.getJSONArray("children");
			for (int i = 0; i < children.length(); i++) {
				JSONObject subobject = children.getJSONObject(i);
				container.addChild(builders.get(subobject.getString("$type")).create(subobject, model, container));
			}
		}
		else if (object.get("children") instanceof String) {
			try {
				JSONArray children = new JSONArray(DataUtils.readToString(DataUtils.getResource(object.getString("children")), 1024));
				for (int i = 0; i < children.length(); i++) {
					JSONObject subobject = children.getJSONObject(i);
					container.addChild(builders.get(subobject.getString("$type")).create(subobject, model, container));
				}
			} catch (JSONException | IOException e) {
				Logger.error("UIBuilder.createContainer(JSONObject, Model)", e);
			}
			
		}
		return container;
	}
	
	private Image createImage(JSONObject object, Model model, Container container) {
		//Logger.debug("Image");
		Image image = new Image(object.getString("id"));
		if (object.has("z-index")) {
			image.setZIndex(object.getInt("z-index"));
		}
		image.setBounds(parseBounds(object.getJSONArray("bounds")));
		if (object.has("binding")) {
			image.setModel(model, object.getString("binding"));
		}
		if (object.has("source")) {
			image.setSource(DataUtils.getResource(object.getString("source")));
		}
		if (object.has("rounded")) {
			image.setRounded(object.getBoolean("rounded"));
		}
		return image;
	}
	
	private ClickableHex createClickableHex(JSONObject object, Model model, Container container) {
		//Logger.debug("Clickable Hex");
		ClickableHex hex = new ClickableHex(object.getString("id"));
		if (object.has("z-index")) {
			hex.setZIndex(object.getInt("z-index"));
		}
		JSONArray center = object.getJSONArray("center");
		hex.setFill(parseColor(object.get("fill-color")));
		if (object.has("binding")) {
			hex.setModel(model, object.getString("binding"));
		}
		if (object.has("skip-input")) {
			hex.setSkipInput(object.getBoolean("skip-input"));
		}
		hex.setBounds(center.getDouble(0), center.getDouble(1), object.getDouble("radius"));
		return hex;
	}
	
	private Label createLabel(JSONObject object, Model model, Container container) {
		//Logger.debug("Label");
		Label label = new Label(object.getString("id"));
		if (object.has("z-index")) {
			label.setZIndex(object.getInt("z-index"));
		}
		label.setAlignment(TextAlign.valueOf(object.getString("align").toUpperCase().replace('-', '_')));
		label.setFont(object.getString("font-face"), object.getInt("font-size"));
		if (object.has("binding")) {
			label.setModel(model, object.getString("binding"));
		}
		label.setPosition(object.getJSONArray("position").getDouble(0), object.getJSONArray("position").getDouble(1));
		if (object.has("text")) {
			label.setText(object.getString("text"));
		}
		label.setTextColor(parseColor(object.get("text-color")));
		return label;
	}
	
	private Modal createModal(JSONObject object, Model model, Container container) {
		//Logger.debug("Modal");
		Modal modal = new Modal(object.getString("id"));
		if (object.has("z-index")) {
			modal.setZIndex(object.getInt("z-index"));
		}
		modal.setModel(model);
		modal.setBackgroundColor(parseColor(object.get("background-color")));
		modal.setFont(object.getString("font-face"), object.getInt("font-size"));
		modal.setSize(object.getDouble("width"), object.getDouble("height"));
		modal.setTextColor(parseColor(object.get("label-color")));
		modal.setTitle(object.getString("title"));
		modal.setVisible(false);
		modal.setConsumeInput(object.getBoolean("consume-input"));
		JSONObject subobject = object.getJSONObject("container");
		modal.setContainer((Container) builders.get(subobject.getString("$type")).create(subobject, model, container));
		return modal;
	}
	
	private void loadButtonProperties(Button button, JSONObject object, Model model, Container container) {
		if (object.has("z-index")) {
			button.setZIndex(object.getInt("z-index"));
		}
		if (object.has("action")) {
			button.setAction(object.getString("action"));
			button.setListener((b) -> broadcastListener.onBroadcast(button.getAction()));
		}
		if (object.has("rounded")) {
			button.setRounded(object.getBoolean("rounded"));
		}
		if (object.has("binding")) {
			button.setModel(model, object.getString("binding"));
		}
		button.setBounds(parseBounds(object.getJSONArray("bounds")));
		float[] up = parseColor(object.get("background-color"));
		float[] hover = object.has("background-color-hover") ? parseColor(object.get("background-color-hover")) : null;
		float[] down = object.has("background-color-down") ? parseColor(object.get("background-color-down")) : null;
		if (down == null) {
			if (hover != null) {
				button.setBackgroundColor(up, hover);
			} else {
				button.setBackgroundColor(up);
			}
		} else {
			if (hover != null && up != null) {
				button.setBackgroundColor(up, hover, down);
			} else {
				button.setBackgroundColor(down);
			}
		}
	}
	
	private Button createButton(JSONObject object, Model model, Container container) {
		//Logger.debug("Button");
		Button button = new Button(object.getString("id"));
		loadButtonProperties(button, object, model, container);
		return button;
	}
	
	private ImageButton createImageButton(JSONObject object, Model model, Container container) {
		//Logger.debug("ImageButton");
		ImageButton button = new ImageButton(object.getString("id"));
		loadButtonProperties(button, object, model, container);
		Image image = new Image(object.getString("id") + ".Image");
		image.setBounds(parseBounds(object.getJSONArray("image-bounds")));
		if (object.has("image-binding")) {
			image.setModel(model, object.getString("image-binding"));
		}
		if (object.has("rounded")) {
			image.setRounded(object.getBoolean("rounded"));
		}
		if (object.has("source")) {
			image.setSource(DataUtils.getResource(object.getString("source")));
		}
		button.setImage(image);
		return button;
	}
	
	private IconAndLabelButton createIconAndLabelButton(JSONObject object, Model model, Container container) {
		//Logger.debug("IconAndLabelButton");
		IconAndLabelButton button = new IconAndLabelButton(object.getString("id"));
		loadButtonProperties(button, object, model, container);
		if (object.has("font-face")) {
			button.setFontFace(object.getString("font-face"));
			button.setLabel(object.getString("label"));
		}
		button.setFontSize(object.getInt("font-size"));
		if (object.has("icon")) {
			button.setIcon(FontAwesome.valueOf(object.getString("icon").toUpperCase().replace("-", "_")));
		}
		button.setTextColor(parseColor(object.get("text-color")));
		return button;
	}
	
	private ValueSlider createValueSlider(JSONObject object, Model model, Container container) {
		//Logger.debug("ValueSlider");
		ValueSlider slider = new ValueSlider(object.getString("id"));
		if (object.has("z-index")) {
			slider.setZIndex(object.getInt("z-index"));
		}
		if (object.has("binding")) {
			slider.setModel(model, object.getString("binding"));
		}
		slider.setBarColor(parseColor(object.get("bar-color")));
		float[] up = parseColor(object.get("slider-color"));
		float[] hover = object.has("slider-color-hover") ? parseColor(object.get("slider-color-hover")) : null;
		float[] down = object.has("slider-color-down") ? parseColor(object.get("slider-color-down")) : null;
		if (down == null) {
			if (hover == null) {
				slider.setSliderColor(up, hover);
			} else {
				slider.setSliderColor(up);
			}
		} else {
			if (hover != null && up != null) {
				slider.setSliderColor(up, hover, down);
			} else {
				slider.setSliderColor(up);
			}
		}
		slider.setDefaultValue(object.getDouble("default-value"));
		slider.setDirection(Direction.valueOf(object.getString("direction").toUpperCase()));
		slider.setRange(object.getJSONArray("range").getDouble(0), object.getJSONArray("range").getDouble(1));
		slider.setThickness(object.getDouble("thickness"));
		Shape sliderShape = DataUtils.constructFromString(object.getString("slider"));
		slider.setSlider(sliderShape);
		slider.setBounds(parseBounds(object.getJSONArray("bounds")));
		slider.setInnerPadding(sliderShape.getWidth());
		return slider;
	}
	
	private ZoomSlider createZoomSlider(JSONObject object, Model model, Container container) {
		//Logger.debug("ZoomSlider");
		ZoomSlider slider = new ZoomSlider(object.getString("id"));
		if (object.has("z-index")) {
			slider.setZIndex(object.getInt("z-index"));
		}
		if (object.has("binding")) {
			slider.setModel(model, object.getString("binding"));
		}
		slider.setDefaultValue(object.getDouble("default-value"));
		slider.setBounds(parseBounds(object.getJSONArray("bounds")));
		slider.setButtonTextColor(parseColor(object.get("button-text-color")));
		slider.setDirection(Direction.HORIZONTAL);
		slider.setJumps(object.getInt("jumps"));
		slider.setRange(object.getJSONArray("range").getDouble(0), object.getJSONArray("range").getDouble(1));
		slider.setBarColor(parseColor(object.get("slider-bar-color")));
		float[] up = parseColor(object.get("slider-slider-color"));
		float[] hover = object.has("slider-slider-color-hover") ? parseColor(object.get("slider-slider-color-hover")) : null;
		float[] down = object.has("slider-slider-color-down") ? parseColor(object.get("slider-slider-color-down")) : null;
		if (down == null) {
			if (hover == null) {
				slider.setSliderColor(up, hover);
			} else {
				slider.setSliderColor(up);
			}
		} else {
			if (hover != null && up != null) {
				slider.setSliderColor(up, hover, down);
			} else {
				slider.setSliderColor(up);
			}
		}
		up = parseColor(object.get("button-background-color"));
		hover = object.has("button-background-color-hover") ? parseColor(object.get("button-background-color-hover")) : null;
		down = object.has("button-background-color-down") ? parseColor(object.get("button-background-color-down")) : null;
		if (down == null) {
			if (hover == null) {
				slider.setButtonBackgroundColor(up, hover);
			} else {
				slider.setButtonBackgroundColor(up);
			}
		} else {
			if (hover != null && up != null) {
				slider.setButtonBackgroundColor(up, hover, down);
			} else {
				slider.setButtonBackgroundColor(up);
			}
		}
		return slider;
	}
	
	private EmptySpaceWidget createEmptySpace(JSONObject object, Model model, Container container) {
		EmptySpaceWidget space = new EmptySpaceWidget(object.getString("id"));
		space.setBounds(parseBounds(object.getJSONArray("bounds")));
		space.setStroke(parseColor(object.get("stroke")));
		if (object.has("binding")) {
			space.setModel(model, object.getString("binding"));
		}
		return space;
	}
	
	private Tooltip createTooltip(JSONObject object, Model model, Container container) {
		Tooltip tooltip = new Tooltip(object.getString("id"));
		if (object.has("binding")) {
			tooltip.setModel(model, object.getString("binding"));
		}
		tooltip.setContents(createContainer(object.getJSONObject("contents"), model, container));
		tooltip.setTarget(container.findChildById(object.getString("target")));
		if (object.has("z-index")) {
			tooltip.setZIndex(object.getInt("z-index"));
		}
		return tooltip;
	}
	
	private TabbedPane createTabbedPane(JSONObject object, Model model, Container container) {
		TabbedPane pane = new TabbedPane(object.getString("id"));
		pane.setBounds(parseBounds(object.getJSONArray("bounds")));
		pane.setTabSize(object.getJSONArray("tab-size").getDouble(0), object.getJSONArray("tab-size").getDouble(1));
		pane.setFont(object.getString("font-face"), object.getInt("font-size"));
		JSONArray tabs = object.getJSONArray("tabs");
		for (int i = 0; i < tabs.length(); i++) {
			JSONObject tabInfo = tabs.getJSONObject(i).getJSONObject("tab-properties");
			FontAwesome icon = null;
			if (tabInfo.has("icon") && tabInfo.getString("icon").length() > 0) {
				icon = FontAwesome.valueOf(tabInfo.getString("icon").toUpperCase());
			}
			pane.addTab(icon, tabInfo.getString("text"), tabInfo.getDouble("tab-width"), createContainer(tabs.getJSONObject(i), model, container), 
					parseColor(tabInfo.get("background-color")), parseColor(tabInfo.get("background-color-hover")), 
					parseColor(tabInfo.get("background-color-down")), parseColor(tabInfo.get("text-color")));
		}
		return pane;		
	}
	
	private DataTable createDataTable(JSONObject object, Model model, Container container) {
		DataTable table = new DataTable(object.getString("id"));
		table.setBounds(parseBounds(object.getJSONArray("bounds")));
		table.setDimensions(object.getDouble("height"), parseDoubleArray(object.getJSONArray("width")));
		if (object.has("binding")) {
			table.setModel(model, object.getString("binding"));
		}
		if (object.has("z-index")) {
			table.setZIndex(object.getInt("z-index"));
		}
		JSONArray array = object.getJSONArray("background-color");
		float[][] colors = new float[array.length()][4];
		for (int i = 0; i < array.length(); i++) {
			float[] color = parseColor(array.get(i));
			for (int e = 0; e < 4; e++) {
				colors[i][e] = color[e];
			}
		}
		table.setBackgroundColors(colors);
		return table;
	}
	
	private TextField createTextField(JSONObject object, Model model, Container container) {
		TextField textfield = new TextField(object.getString("id"));
		textfield.setBackgroundColor(parseColor(object.get("background-color")));
		textfield.setSelectionColor(parseColor(object.get("selection-color")));
		textfield.setBounds(parseBounds(object.getJSONArray("bounds")));
		textfield.setFont(object.getString("font-face"), object.getInt("font-size"));
		textfield.setTextColor(parseColor(object.get("text-color")));
		if (object.has("binding")) {
			textfield.setModel(model, object.getString("binding"));
		}
		if (object.has("mask")) {
			textfield.setMask(object.getString("mask").charAt(0));
		}
		return textfield;
	}
	
	private ScrollableWidget createScrollable(JSONObject object, Model model, Container container) {
		ScrollableWidget scrollable = new ScrollableWidget(object.getString("id"), object.has("scroll-size") ? object.getDouble("scroll-size") : 16);
		JSONObject contents = object.getJSONObject("contents");
		scrollable.setContents(builders.get(contents.getString("$type")).create(contents, model, container));
		scrollable.setBounds(parseBounds(object.getJSONArray("bounds")));
		scrollable.setScrollbarColor(
				parseColor(object.get("background-color")), 
				parseColor(object.get("scroll-color")), 
				parseColor(object.get("scroll-color-hover")), 
				parseColor(object.get("scroll-color-down")));
		return scrollable;
	}
	
	private Dropdown createDropdown(JSONObject object, Model model, Container container) {
		Dropdown dropdown = new Dropdown(object.getString("id"));
		dropdown.setBackgroundColor(parseColor(object.get("background-color")), parseColor(object.get("hover-color")));
		dropdown.setTextColor(parseColor(object.get("text-color")));
		dropdown.setFont(object.getString("font-face"), object.getInt("font-size"));
		dropdown.setBounds(parseBounds(object.getJSONArray("bounds")));
		dropdown.setButtonColor(
				parseColor(object.get("button-background-color")), 
				parseColor(object.get("button-background-color-hover")), 
				parseColor(object.get("button-background-color-down")));
		dropdown.setButtonTextColor(parseColor(object.get("button-text-color")));
		if (object.has("binding")) {
			dropdown.setModel(model, object.getString("binding"));
		}
		if (object.has("selected")) {
			dropdown.setAction(object.getString("selected"));
			dropdown.setListener((d, option) -> broadcastListener.onBroadcast(d.getAction() + "[" + option + "]"));
		}
		dropdown.setDirection(Dropdown.Direction.valueOf(object.getString("direction").toUpperCase()));
		return dropdown;
	}
	
	public UserInterface build(InputStream input, Model model) {
		try {
			json = DataUtils.readToString(input, 1024);
		} catch (IOException e) {
			Logger.error("UIBuilder.build(InputStream)", e);
			json = "{}";
		}
		UserInterface ui = new UserInterface(model, palette);
		JSONObject object = new JSONObject(json);
		ui.setRootNode(builders.get(object.getString("$type")).create(object, ui.getModel(), null));
		return ui;
	}
	
	public ModalRegistry buildModalRegistry(InputStream input) {
		try {
			json = DataUtils.readToString(input, 1024);
		} catch (IOException e) {
			Logger.error("UIBuilder.buildModalRegistry(InputStream)", e);
			json = "[]";
		}
		ModalRegistry registry = new ModalRegistry();
		JSONArray object = new JSONArray(json);
		for (int i = 0; i < object.length(); i++) {
			Model model = new Model();
			registry.registerModal(createModal(object.getJSONObject(i), model, null));
		}
		return registry;
	}

}
