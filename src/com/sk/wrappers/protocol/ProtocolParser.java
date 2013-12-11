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

import com.sk.wrappers.protocol.extractor.ParseType;

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
			LinkedList<String> parts = new LinkedList<>(Arrays.asList(line.split(" ")));
			String locString = next(parts);
			if (locString == null) {
				System.out.println(line);
				continue;
			}
			List<Integer> locs = parseLocs(locString);

			String out = null;

			String next = next(parts);
			if (next == null)
				continue;

			if (next.equals("array")) {
				String counter = getStreamExtractor(parts);
				String extractors = getExtractors(parts);
				out = String.format("new ArrayProtocol(%s, %s, ", counter, extractors);
			} else if (next.equals("ea")) {
				out = "new ExtraAttributeReader(";
				locs.clear();
			} else {
				parts.addFirst(next);
				String extractors = getExtractors(parts);
				out = String.format("new BasicProtocol(%s, ", extractors);
			}
			if (map.containsKey(out)) {
				map.get(out).addAll(locs);
			} else {
				map.put(out, locs);
			}
		}
		for (Entry<String, List<Integer>> entry : map.entrySet()) {
			System.out.printf("%s%s).addSelfToGroup(protocol);\n", entry.getKey().replaceAll("XX[0-9]+XX", ""),
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

	public static String getExtractors(Queue<String> parts) {
		List<String> extractors = new ArrayList<>();
		String curExtractor;
		while ((curExtractor = getExtractor(parts)) != null) {
			extractors.add(curExtractor);
		}
		return String.format("new FieldExtractor[]{%s}", stringifyCollection(extractors));
	}

	public static String getExtractor(Queue<String> parts) {
		String baseExtractor = getStreamExtractor(parts);
		if (baseExtractor == null)
			return null;
		String name = next(parts);
		if (name == null)
			return String.format("new FieldExtractor(%s)", baseExtractor);
		else
			return String.format("new FieldExtractor(%s, \"%s\"XX%dXX)", baseExtractor, name,
					System.currentTimeMillis());
	}

	public static String getStreamExtractor(Queue<String> parts) {
		String type = next(parts);
		if (type == null)
			return null;
		type = type.toUpperCase();
		if (type.equals("SET")) {
			String value = next(parts);
			return String.format("new StaticExtractor(%s)", value);
		}
		try {
			ParseType.valueOf(type);
			return "ParseType." + type;
		} catch (IllegalArgumentException e) {
		}
		try {
			return String.format("new SizedStreamExtractor(%d)", Integer.parseInt(type));
		} catch (NumberFormatException e) {
		}
		return null;
	}

	private static String next(Queue<String> parts) {
		if (parts.isEmpty())
			return null;
		String ret = parts.poll();
		if (ret.equals("#")) {
			parts.clear();
			return null;
		}
		if (ret.equals("-") || ret.equals(","))
			return null;
		return ret;
	}

	public static String stringifyCollection(Object o) {
		String ret = o.toString();
		return ret.substring(1, ret.length() - 1);
	}

}
