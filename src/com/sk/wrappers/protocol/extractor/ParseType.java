package com.sk.wrappers.protocol.extractor;

import com.sk.datastream.Stream;

public enum ParseType implements StreamExtractor {
	BYTE {
		@Override
		public Object get(Stream s) {
			return s.getByte();
		}
	},
	UBYTE {
		@Override
		public Object get(Stream s) {
			return s.getUByte();
		}
	},
	SHORT {
		@Override
		public Object get(Stream s) {
			return s.getShort();
		}
	},
	USHORT {
		@Override
		public Object get(Stream s) {
			return s.getUShort();
		}
	},
	UINT24 {
		@Override
		public Object get(Stream s) {
			return s.getUInt24();
		}
	},
	INT {
		@Override
		public Object get(Stream s) {
			return s.getInt();
		}
	},
	UINT {
		@Override
		public Object get(Stream s) {
			return s.getUInt();
		}
	},
	LONG {
		@Override
		public Object get(Stream s) {
			return s.getLong();
		}
	},
	STRING {
		@Override
		public Object get(Stream s) {
			return s.getString();
		}
	},
	BIG_SMART {
		@Override
		public Object get(Stream s) {
			return s.getBigSmart();
		}
	},
	SMART {
		@Override
		public Object get(Stream s) {
			return s.getSmart();
		}
	},
	JAG_STRING {
		@Override
		public Object get(Stream s) {
			return s.getJagString();
		}
	};
}
