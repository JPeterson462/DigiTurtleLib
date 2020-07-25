package com.digiturtle.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Logger {
	
	public static void setOutAndErr(String outFile, String errFile) throws FileNotFoundException {
		System.setOut(new PrintStream(new File(outFile)));
		System.setErr(new PrintStream(new File(errFile)));
	}
	
	private static String getTime() {
		LocalDateTime now = LocalDateTime.now();
		return String.format("%02d", now.getHour()) + ":" + String.format("%02d", now.getMinute()) + ":" + String.format("%02d", now.getSecond());
	}
	
	public static void error(String source, Exception exception) {
		System.out.println("Error at " + source);
		StringWriter writer = new StringWriter();
		exception.printStackTrace(new PrintWriter(writer));
		System.err.println(writer.toString());
		throw new RuntimeException(exception);
	}
	
	public static void debug(String message, Object... data) {
		System.out.println(getTime() + " [DEBUG] " + message);
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				System.out.println("\tnull");
				continue;
			}
			if (data[i].getClass() == float[].class) {
				System.out.println("\t" + Arrays.toString((float[]) data[i]));
				continue;
			}
			if (data[i].getClass() == boolean[].class) {
				System.out.println("\t" + Arrays.toString((boolean[]) data[i]));
				continue;
			}
			System.out.println("\t" + data[i].toString());
		}
	}

	public static void info(String string) {
		System.out.println(getTime() + " [INFO] " + string);		
	}

}
