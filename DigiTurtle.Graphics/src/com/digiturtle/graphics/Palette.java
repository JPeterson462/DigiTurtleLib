package com.digiturtle.graphics;

import org.json.JSONObject;

import com.digiturtle.math.DataUtils;
import com.digiturtle.math.MathUtils;

public class Palette {
	
	private JSONObject object;
	
	public Palette(JSONObject object) {
		this.object = object;
	}

	private float[] lightenOrDarken(float[] color, int percentage) {
		int delta = (int) ((float) percentage * 2.56f);
		int r = (int) (color[0] * 256);
		int g = (int) (color[1] * 256);
		int b = (int) (color[2] * 256);
		r = MathUtils.clamp(0, r + delta, 256);
		g = MathUtils.clamp(0, g + delta, 256);
		b = MathUtils.clamp(0, b + delta, 256);
		return new float[] {
			(float) r / 256f,
			(float) g / 256f,
			(float) b / 256f,
			color[3]
		};
	}
	
	public float[] getColor(String style, int index, int tinting) {
		Object css = object.getJSONArray(style).get(index);
		float[] computedColor = DataUtils.readFromJson(css);
		if (tinting != 0) {
			computedColor = lightenOrDarken(computedColor, tinting);
		}
		return computedColor;
	}

}
