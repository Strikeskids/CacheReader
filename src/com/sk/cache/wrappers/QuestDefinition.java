package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.QuestDefinitionLoader;
import com.sk.cache.wrappers.protocol.ArrayProtocol;
import com.sk.cache.wrappers.protocol.BasicProtocol;
import com.sk.cache.wrappers.protocol.ExtraAttributeReader;
import com.sk.cache.wrappers.protocol.FieldExtractor;
import com.sk.cache.wrappers.protocol.ProtocolGroup;
import com.sk.cache.wrappers.protocol.extractor.ParseType;
import com.sk.cache.wrappers.protocol.extractor.StaticExtractor;

public class QuestDefinition extends ProtocolWrapper {

	public String name;
	public int scriptId, scriptStartValue, scriptEndValue, configId, configStartValue, configEndValue;
	public int difficulty;

	public QuestDefinition(QuestDefinitionLoader loader, int id) {
		super(loader, id, protocol);
		scriptId = -1; // = new ArrayList<>(1);
		scriptStartValue = -1; // = new ArrayList<>(1);
		scriptEndValue = -1; // = new ArrayList<>(1);
		configId = -1; // = new ArrayList<>(1);
		configStartValue = -1; // = new ArrayList<>(1);
		configEndValue = -1; // = new ArrayList<>(1);
	}

	private static final ProtocolGroup protocol = new ProtocolGroup();
	static {
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "difficulty")}, 9).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.JAG_STRING, "name")}, 1).addSelfToGroup(protocol);
		new ExtraAttributeReader().addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE), new FieldExtractor(ParseType.UBYTE)}, 14).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.INT)}, 10).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BIG_SMART)}, 17).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.INT), new FieldExtractor(ParseType.INT), new FieldExtractor(ParseType.INT), new FieldExtractor(ParseType.STRING)}, 18, 19).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE)}, 6, 7).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 5, 15).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.INT)}, 12).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "configId"), new FieldExtractor(ParseType.INT, "configStartValue"), new FieldExtractor(ParseType.INT, "configEndValue")}, 3).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "scriptId"), new FieldExtractor(ParseType.INT, "scriptStartValue"), new FieldExtractor(ParseType.INT, "scriptEndValue")}, 4).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true))}, 8).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.JAG_STRING)}, 2).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 13).addSelfToGroup(protocol);
	}

}
