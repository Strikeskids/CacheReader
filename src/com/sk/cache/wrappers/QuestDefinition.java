package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.QuestDefinitionLoader;
import com.sk.cache.wrappers.protocol.BasicProtocol;
import com.sk.cache.wrappers.protocol.ExtraAttributeReader;
import com.sk.cache.wrappers.protocol.ProtocolGroup;
import com.sk.cache.wrappers.protocol.extractor.ArrayExtractor;
import com.sk.cache.wrappers.protocol.extractor.FieldExtractor;
import com.sk.cache.wrappers.protocol.extractor.ParseType;
import com.sk.cache.wrappers.protocol.extractor.StaticExtractor;
import com.sk.cache.wrappers.protocol.extractor.StreamExtractor;

public class QuestDefinition extends ProtocolWrapper {

	public String name;
	public int[] scriptId, scriptStartValue, scriptEndValue, configId, configStartValue, configEndValue;
	public int questPoints;

	public QuestDefinition(QuestDefinitionLoader loader, int id) {
		super(loader, id, protocol);
	}

	private static final ProtocolGroup protocol = new ProtocolGroup();
	static {
		new ExtraAttributeReader().addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.JAG_STRING, "name")}, 1).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.USHORT}, null)}, 13).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BIG_SMART)}, 17).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE)}, 6, 7).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.INT}, null)}, 10).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 5, 15).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.INT)}, 12).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.USHORT, ParseType.INT, ParseType.INT}, new String[]{"scriptId", "scriptStartValue", "scriptEndValue"})}, 4).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.UBYTE, ParseType.UBYTE}, null)}, 14).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true))}, 8).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "questPoints")}, 9).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.JAG_STRING)}, 2).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.INT, ParseType.INT, ParseType.INT, ParseType.STRING}, null)}, 18, 19).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.USHORT, ParseType.INT, ParseType.INT}, new String[]{"configId", "configStartValue", "configEndValue"})}, 3).addSelfToGroup(protocol);

	}

}
