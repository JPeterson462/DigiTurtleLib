package com.digiturtle.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBImage;

import com.digiturtle.core.Logger;
import com.digiturtle.core.ManagedResource;
import com.digiturtle.math.DataUtils;

public class Texture extends ManagedResource {
	
	private int id, width, height;
	
	private ByteBuffer data;
	
	private boolean glCreated = false;
	
	private int wrap = GL12.GL_CLAMP_TO_EDGE;
	
	private boolean repeated = false;
	
	private float scale = 1;
	
	public Texture(InputStream stream) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);
        try {
			data = STBImage.stbi_load_from_memory(DataUtils.readFromStreamFully(stream, 1024), width, height, components, 4);
			//Logger.debug("Texture()", width.get(0), height.get(0), components.get(0), data.capacity());
		} catch (IOException e) {
			Logger.error("Texture(InputStream)", e);
			return;
		}
        id = GL11.glGenTextures();
        this.width = width.get();
        this.height = height.get();
        createGL();
	}
	
	public Texture(InputStream stream, boolean noGL) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);
        try {
			data = STBImage.stbi_load_from_memory(DataUtils.readFromStreamFully(stream, 1024), width, height, components, 4);
			//Logger.debug("Texture(noGL)", width.get(0), height.get(0), components.get(0), data.capacity());
		} catch (IOException e) {
			Logger.error("Texture(InputStream)", e);
			return;
		}
        this.width = width.get();
        this.height = height.get();
	}
	
	public Texture setRepeat(boolean repeat, float scale) {
		wrap = repeat ? GL11.GL_REPEAT : GL12.GL_CLAMP_TO_EDGE;
		repeated = repeat;
		this.scale = scale;
		return this;
	}
	
	public float getScale() {
		return scale;
	}
	
	public boolean isRepeated() {
		return repeated;
	}
	
	public void createGL() {
        id = GL11.glGenTextures();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap);
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
	    glCreated = true;
	}
	
	public boolean isGLCreated() {
		return glCreated;
	}
	
	public ByteBuffer getData() {
		return data;
	}
	
	public void wasCreated() {
        STBImage.stbi_image_free(data);
	}

	@Override
	public long getPointer() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	@Override
	public void dispose() {
		GL11.glDeleteTextures(id);
	}

}
