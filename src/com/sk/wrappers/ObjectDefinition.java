package com.sk.wrappers;

import com.sk.Debug;
import com.sk.datastream.Stream;
import com.sk.wrappers.protocol.ArrayProtocol;
import com.sk.wrappers.protocol.BasicProtocol;
import com.sk.wrappers.protocol.ExtraAttributeReader;
import com.sk.wrappers.protocol.FieldExtractor;
import com.sk.wrappers.protocol.ProtocolGroup;
import com.sk.wrappers.protocol.StaticLocReader;
import com.sk.wrappers.protocol.extractor.ParseType;
import com.sk.wrappers.protocol.extractor.StaticExtractor;

public class ObjectDefinition extends ProtocolWrapper<ObjectDefinitionLoader> {

	public String name;
	public int type = -1;
	public int width = 1;
	public int height = 1;
	public boolean walkable = true;
	public boolean walkable2 = false;
	public int blockType = 2;
	public String[] actions = new String[5];
	
	public int scriptId;
	public int configId;
	public int[] childrenIds;

	public ObjectDefinition(ObjectDefinitionLoader loader, int id) {
		super(loader, id, protocol);
	}

	private static final ProtocolGroup protocol = new ProtocolGroup();

	static {

		new StaticLocReader(1) {
			@Override
			public void read(Object store, int type, Stream s) {
				int ci = s.getUByte();
				if (Debug.on)
					System.out.printf("Skip 1 %d [", ci);
				for (int i = 0; i < ci; ++i) {
					if (Debug.on)
						s.printBytes(1);
					else
						s.skip(1);
					int cj = s.getUByte();
					if (Debug.on)
						System.out.printf("%d [", cj);
					for (int j = 0; j < cj; ++j) {
						if (Debug.on)
							System.out.printf("%x ", s.getBigSmart());
						else
							s.getBigSmart();
					}
					if (Debug.on)
						System.out.print("] ");
				}
				if (Debug.on)
					System.out.println("]");
			}
		}.addSelfToGroup(protocol);

		new StaticLocReader(77, 92) {
			@Override
			public void read(Object obj, int type, Stream s) {
				int script = s.getUShort();
				int config = s.getUShort();
				int ending = type == 92 ? s.getBigSmart() : -1;
				int count = s.getUByte() + 1;
				int[] arr = new int[count + 1];
				for (int i = 0; i < count; ++i)
					arr[i] = s.getBigSmart();
				arr[count] = ending;
				FieldExtractor.setValue(obj, type, type, "scriptId", script == 0xFFFF ? -1 : script);
				FieldExtractor.setValue(obj, type, type, "configId", config == 0xFFFF ? -1 : config);
				FieldExtractor.setValue(obj, type, type, "childrenIds", arr);
			}
		}.addSelfToGroup(protocol);

		new StaticLocReader(79) {
			@Override
			public void read(Object o, int type, Stream s) {
				s.skip(5);
				int ci = s.getUByte();
				s.skip(2 * ci);
			}
		}.addSelfToGroup(protocol);

		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(null))}, 21, 22, 23, 62, 64, 73, 82, 88, 89, 91, 94, 97, 98, 103, 105, 168, 169, 177, 189, 198, 199, 200).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "name")}, 2).addSelfToGroup(protocol);
		new ExtraAttributeReader().addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(false), "walkable"), new FieldExtractor(new StaticExtractor(0), "blockType")}, 17).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BIG_SMART)}, 24).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "actions")}, 150, 151, 152, 153, 154).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.BIG_SMART), new FieldExtractor(ParseType.UBYTE)}, 106).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(false), "walkable")}, 18).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.USHORT)}, 40, 41).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.UBYTE)}, 78).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 5, 160).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(true), "walkable2")}, 74).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE), new FieldExtractor(ParseType.USHORT)}, 99, 100).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BYTE)}, 28, 29, 39, 196, 197).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.USHORT)}, 173).addSelfToGroup(protocol);
		new ArrayProtocol(ParseType.UBYTE, new FieldExtractor[]{new FieldExtractor(ParseType.BYTE)}, 42).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "width")}, 14).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "type")}, 19).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.SMART)}, 170, 171).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.STRING, "actions")}, 30, 31, 32, 33, 34).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE)}, 69, 75, 81, 101, 104, 178, 186, 250, 251, 253, 254).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT)}, 44, 45, 65, 66, 67, 70, 71, 72, 93, 95, 102, 107, 164, 165, 166, 167, 190, 191, 192, 193, 194, 195).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.UBYTE, "height")}, 15).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.INT)}, 162).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.BYTE), new FieldExtractor(ParseType.BYTE), new FieldExtractor(ParseType.BYTE), new FieldExtractor(ParseType.BYTE)}, 163).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.USHORT), new FieldExtractor(ParseType.USHORT)}, 252, 255).addSelfToGroup(protocol);
		new BasicProtocol(new FieldExtractor[]{new FieldExtractor(new StaticExtractor(1), "blockType")}, 27).addSelfToGroup(protocol);

	}

}
