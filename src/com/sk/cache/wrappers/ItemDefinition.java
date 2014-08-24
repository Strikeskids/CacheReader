package com.sk.cache.wrappers;

import com.sk.cache.wrappers.loaders.ItemDefinitionLoader;
import com.sk.cache.wrappers.protocol.BasicProtocol;
import com.sk.cache.wrappers.protocol.ExtraAttributeReader;
import com.sk.cache.wrappers.protocol.ProtocolGroup;
import com.sk.cache.wrappers.protocol.extractor.ArrayExtractor;
import com.sk.cache.wrappers.protocol.extractor.FieldExtractor;
import com.sk.cache.wrappers.protocol.extractor.ParseType;
import com.sk.cache.wrappers.protocol.extractor.StaticExtractor;
import com.sk.cache.wrappers.protocol.extractor.StreamExtractor;

public class ItemDefinition extends ProtocolWrapper {

	public boolean stackable;
	public boolean members;
	public boolean edible;
	public boolean noted;
	public boolean lent;
	public int slot = -1;
	public int noteId = -1;
	public int value = 0;
	public int lentId = -1;
	public String[] actions = { null, null, null, null, "Drop" };
	public String[] groundActions = { null, null, "Take", null, null };
	public int noteTemplateId = -1;
	public int lentTemplateId = -1;
	public int team;

	public String name;

	public ItemDefinition(ItemDefinitionLoader loader, int id) {
		super(loader, id, protocol);
	}

	private static final ProtocolGroup protocol = new ProtocolGroup();

	static {
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.SHORT, ParseType.SHORT}, null)}, 41).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true), "members")}, 16).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "name")}, 2).addSelfToGroup(protocol);
		new ExtraAttributeReader().addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BIG_SMART)}, 1, 23, 24, 25, 26, 78, 79, 90, 91, 92, 93).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.INT, "value")}, 12).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(false))}, 156).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.BYTE}, null)}, 42).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "lentTemplateId")}, 122).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "team")}, 115).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "actions")}, 35, 36, 37, 38, 39).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(2))}, 165).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "slot")}, 13).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BYTE)}, 113, 114).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.USHORT)}, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "groundActions")}, 30, 31, 32, 33, 34).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "noteId")}, 97).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "lentId")}, 121).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.USHORT}, null)}, 132).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT, "noteTemplateId")}, 98).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING)}, 164).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE)}, 14, 27, 96, 134).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 4, 5, 6, 7, 8, 18, 44, 45, 94, 95, 110, 111, 112, 139, 140, 142, 143, 144, 145, 146, 150, 151, 152, 153, 154, 161, 162, 163).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BYTE), new FieldExtractor(ParseType.BYTE), new FieldExtractor(ParseType.BYTE)}, 125, 126).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.INT)}, 43).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true), "stackable")}, 11).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true))}, 65, 157).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new ArrayExtractor(ParseType.UBYTE,0,new StreamExtractor[]{ParseType.USHORT, ParseType.USHORT}, null)}, 40).addSelfToGroup(protocol);

	}

}
