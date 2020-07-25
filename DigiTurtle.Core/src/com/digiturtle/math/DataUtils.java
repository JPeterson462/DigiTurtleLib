package com.digiturtle.math;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.lwjgl.BufferUtils;

import com.digiturtle.core.Logger;

public class DataUtils {
	
	public static boolean contains(Integer[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}
	
	public static int count(boolean[] array, boolean value) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				count++;
			}
		}
		return count;
	}
	
	public static String getClipboard() {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable t = c.getContents(null);
	    String contents = "";
	    try {
			if (t != null) {
				contents = (String) t.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (UnsupportedFlavorException | IOException e) {
			Logger.error("DataUtils.getClipboard()", e);
		}
	    return contents;
	}
	
	public static void setClipboard(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(text);
		clipboard.setContents(stringSelection, null);
	}
	
	public static String toHexString(byte[] bytes) {
		String hex = "0123456789ABCDEF";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			builder.append(hex.charAt((bytes[i] & 0xF0) >> 4));
			builder.append(hex.charAt((bytes[i] & 0x0F) >> 0));
		}
		return builder.toString();
	}
	
	public static boolean jsonContains(JSONArray array, int value) {
		for (int i = 0; i < array.length(); i++) {
			if (array.getInt(i) == value) {
				return true;
			}
		}
		return false;
	}
	
	public static int[] toArray(JSONArray array) {
		int[] data = new int[array.length()];
		for (int i = 0; i < data.length; i++) {
			data[i] = array.getInt(i);
		}
		return data;
	}
	
	public static Shape constructFromString(String string) {
		String[] parts = string.split(" ");
		if (parts.length > 0) {
			if (parts[0].equalsIgnoreCase("circle") && parts.length == 2) {
				Circle circle = new Circle();
				circle.setRadius(Double.parseDouble(parts[1]));
				return circle;
			}
			else if (parts[0].equalsIgnoreCase("ellipse") && parts.length == 3) {
				Ellipse ellipse = new Ellipse();
				ellipse.setRadius(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
				return ellipse;
			}
			else if (parts[0].equalsIgnoreCase("rectangle") && parts.length == 3) {
				Rectangle rectangle = new Rectangle(0, 0, Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
				rectangle.center();
				return rectangle;
			}
			else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static int fromHex(char c) {
		String hex = "0123456789abcdef";
		return hex.indexOf(Character.toLowerCase(c));
	}
	
	public static int fromHex(String string) {
		int result = 0;
		for (int i = 0; i < string.length(); i++) {
			result = result * 16 + fromHex(string.charAt(i));
		}
		return result;
	}
	
	public static float[] readFromJson(Object object) {
		if (object instanceof JSONArray) {
			JSONArray array = (JSONArray) object;
			float[] data = new float[array.length()];
			for (int i = 0; i < data.length; i++) {
				data[i] = (float) array.getDouble(i);
			}
			return data;
		}
		else if (object instanceof String) {
			String hex = (String) object;
			if (hex.startsWith("#")) {
				if (hex.length() == 1 + 6) {
					float[] result = new float[] {
						(float) fromHex(hex.substring(1, 3)) / 255f,
						(float) fromHex(hex.substring(3, 5)) / 255f,
						(float) fromHex(hex.substring(5, 7)) / 255f,
						1f
					};
					return result;
				}
				else if (hex.length() == 1 + 3) {
					int r1 = fromHex(hex.substring(1, 2));
					int g1 = fromHex(hex.substring(2, 3));
					int b1 = fromHex(hex.substring(3, 4));
					return new float[] {
						(float) (r1 * 16 + r1) / 255f,
						(float) (g1 * 16 + g1) / 255f,
						(float) (b1 * 16 + b1) / 255f,
						1f
					};
				}
				else if (hex.length() == 1 + 8) {
					return new float[] {
						(float) fromHex(hex.substring(1, 3)) / 255f,
						(float) fromHex(hex.substring(3, 5)) / 255f,
						(float) fromHex(hex.substring(5, 7)) / 255f,
						(float) fromHex(hex.substring(7, 9)) / 255f
					};
				}
			}
			return null;
		}
		return null;
	}
	
	public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int read;
		while ((read = input.read(buffer)) > 0) {
			output.write(buffer, 0, read);
		}
	}
	
	public static String readToString(InputStream input, int bufferSize) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, bufferSize);
		byte[] bytes = output.toByteArray();
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	public static ByteBuffer readFromStreamFully(InputStream input, int bufferSize) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, bufferSize);
		byte[] bytes = output.toByteArray();
		ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
		buffer.put(bytes).flip();
		return buffer;
	}

	public static InputStream getResource(String path) {
		if (path.startsWith("http")) {
			try {
				return new URL(path).openStream();
			} catch (IOException e) {
				Logger.error("DataUtils.getResource(String): " + path, e);
			}
		}
		File file = new File("res/" + path);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.error("DataUtils.getResource(String)", e);
			return null;
		}
	}
	
	public static void writeToFile(String contents, OutputStream outputStream) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(outputStream)));
		writer.write(contents);
		writer.flush();
	}
	
}
