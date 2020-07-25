package com.digiturtle.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.digiturtle.core.Logger;
import com.digiturtle.core.ManagedResource;
import com.digiturtle.math.DataUtils;

public class Font extends ManagedResource {
	
	private int pointer;
	
	private String name;
	
	private static HashMap<String, Font> registry = new HashMap<>();
	
	private ByteBuffer buffer;
	
	public Font(InputStream stream, String name) {
		this.name = name;
		pointer = -1;
		registry.put(name, this);
		try {
			buffer = DataUtils.readFromStreamFully(stream, 1024);
		} catch (IOException e) {
			Logger.error("Font(InputStream, String)", e);
		}
	}
	
	public static Font findFont(String name) {
		return registry.get(name);
	}
	
	public void tryCreate(RenderingContext context) {
		if (pointer == -1) {
			pointer = context.createFont(buffer, name);
		}
	}

	@Override
	public long getPointer() {
		return pointer;
	}

	@Override
	public void dispose() {
		
	}

}
