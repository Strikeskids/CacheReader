package com.sk.wrappers.protocol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;

public class ProtocolParser {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		String filename = sc.nextLine();
		sc.close();
		BufferedReader r = new BufferedReader(new FileReader(filename));
		parseProtocol(r);
	}

	public static void parseProtocol(BufferedReader r) throws IOException {
		String line;
		Map<String, List<Integer>> map = new HashMap<>();

		while ((line = r.readLine()) != null) {
			line = line.trim();
			if (line.length() == 0)
				continue;
			LinkedList<String> parts = new LinkedList<>(Arrays.asList(line.split(" ")));
			List<Integer> locs = parseLocs(next(parts));

			String out = null;

			String next = next(parts);
			if (next == null)
				continue;

			if (next.equals("array")) {
				String counter = getExtractor(parts);
				String extractors = getExtractors(parts);
				out = String.format("new Array%s(%s, %s, ", getType(extractors), counter, extractors);
			} else if (next.equals("ea")) {
				out = "new ExtraAttributeReader(";
				locs.clear();
			} else if (next.equals("set")) {
				String name = next(parts);
				String value = next(parts);
				out = String.format("new BasicSetter(\"%s\", %s, ", name, value);
			} else {
				parts.addFirst(next);
				String extractors = getExtractors(parts);
				out = String.format("new Basic%s(%s, ", getType(extractors), extractors);
			}
			if (map.containsKey(out)) {
				map.get(out).addAll(locs);
			} else {
				map.put(out, locs);
			}
		}
		for (Entry<String, List<Integer>> entry : map.entrySet()) {
			System.out.printf("%s%s).addSelfToGroup(protocol);\n", entry.getKey(),
					stringifyCollection(entry.getValue()));
		}
	}

	public static List<Integer> parseLocs(String locString) {
		List<Integer> ret = new ArrayList<>();
		if (locString.contains("-")) {
			String[] locsplit = locString.split("-");
			int start = Integer.parseInt(locsplit[0]), end = Integer.parseInt(locsplit[1]);
			for (int i = start; i <= end; ++i) {
				ret.add(i);
			}
		} else {
			ret.add(Integer.parseInt(locString));
		}
		return ret;
	}

	public static String getType(String input) {
		return input.contains("Field") ? "Reader" : "Skipper";
	}

	public static String getExtractors(Queue<String> parts) {
		List<String> extractors = new ArrayList<>();
		String curExtractor;
		String arrayType = "StreamExtractor";
		while ((curExtractor = getExtractor(parts)) != null) {
			if (curExtractor.contains("Field"))
				arrayType = "FieldExtractor";
			extractors.add(curExtractor);
		}
		return String.format("new %s[]{%s}", arrayType, stringifyCollection(extractors));
	}

	public static String getExtractor(Queue<String> parts) {
		String type = next(parts);
		if (type == null)
			return null;
		type = type.toUpperCase();
		String baseExtractor = null;
		try {
			ParseType.valueOf(type);
			baseExtractor = "ParseType." + type;
		} catch (IllegalArgumentException e) {
			baseExtractor = String.format("new SizedStreamExtractor(%d)", Integer.parseInt(type));
		}
		String name = next(parts);
		if (name == null)
			return baseExtractor;
		return String.format("new FieldExtractor(%s, \"%s\")", baseExtractor, name);
	}

	private static String next(Queue<String> parts) {
		if (parts.isEmpty())
			return null;
		String ret = parts.poll();
		if (ret.equals("#")) {
			parts.clear();
			return null;
		}
		if (ret.equals(","))
			return null;
		return ret;
	}

	public static String stringifyCollection(Object o) {
		String ret = o.toString();
		return ret.substring(1, ret.length() - 1);
	}

}
