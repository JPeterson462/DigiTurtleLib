package com.digiturtle.graphics;

import org.lwjgl.nanovg.NanoVG;

public enum TextAlign {

	LEFT_TOP(NanoVG.NVG_ALIGN_LEFT, NanoVG.NVG_ALIGN_TOP),
		CENTER_TOP(NanoVG.NVG_ALIGN_CENTER, NanoVG.NVG_ALIGN_TOP),
			RIGHT_TOP(NanoVG.NVG_ALIGN_RIGHT, NanoVG.NVG_ALIGN_TOP),
	LEFT_MIDDLE(NanoVG.NVG_ALIGN_LEFT, NanoVG.NVG_ALIGN_MIDDLE),
		CENTER_MIDDLE(NanoVG.NVG_ALIGN_CENTER, NanoVG.NVG_ALIGN_MIDDLE),
			RIGHT_MIDDLE(NanoVG.NVG_ALIGN_RIGHT, NanoVG.NVG_ALIGN_MIDDLE),
	LEFT_BOTTOM(NanoVG.NVG_ALIGN_LEFT, NanoVG.NVG_ALIGN_BOTTOM),
		CENTER_BOTTOM(NanoVG.NVG_ALIGN_CENTER, NanoVG.NVG_ALIGN_BOTTOM),
			RIGHT_BOTTOM(NanoVG.NVG_ALIGN_RIGHT, NanoVG.NVG_ALIGN_BOTTOM);
	
	public final int horizontal, vertical;
	
	TextAlign(int horizontal, int vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
}
