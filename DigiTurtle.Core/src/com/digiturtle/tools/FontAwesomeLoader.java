package com.digiturtle.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontAwesomeLoader {
	
	public static void main(String[] args) throws IOException { 
		BufferedReader reader = new BufferedReader(new InputStreamReader(FontAwesomeLoader.class.getClassLoader().getResource("com/digiturtle/tools/fa-cheatsheet.txt").openStream()));
		// icon-renren (&#xf18b;)
		Pattern pattern = Pattern.compile("([\\S]+)\\s+\\(&#x([a-fA-F0-9]+);\\)");
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.substring(line.indexOf(' ') + 1);
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				System.out.println(matcher.group(1).substring(5).replace('-', '_').toUpperCase() + "(0x" + matcher.group(2).toUpperCase() + "),");
			}
		}
	}
	
}
