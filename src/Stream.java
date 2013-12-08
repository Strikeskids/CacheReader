

import java.nio.BufferUnderflowException;
import java.util.Arrays;
import java.util.zip.Inflater;

import com.sk.compress.BZip2Decompressor;

public abstract class Stream {

	public static final int BYTE_SIZE = 8, SHORT_SIZE = 16, INT_SIZE = 32, LONG_SIZE = 64;

	public static final int NUM_LONG_BYTES = LONG_SIZE / BYTE_SIZE;
	public static final int NUM_INT_BYTES = INT_SIZE / BYTE_SIZE;
	public static final int NUM_SHORT_BYTES = SHORT_SIZE / BYTE_SIZE;

	public static final int[] MASKS = new int[INT_SIZE];
	static {
		for (int i = 1, currentMask = 0; i < MASKS.length; ++i) {
			currentMask = currentMask << 1 | 1;
			MASKS[i] = currentMask;
		}
	}
	public static final int BYTE_SIGN_BIT = 1 << (BYTE_SIZE - 1);
	public static final int BYTE_MASK = MASKS[BYTE_SIZE];
	public static final int SKIP_SIGN_BYTE_MASK = MASKS[BYTE_SIZE - 1];

	public static final int SHORT_SIGN_BIT = 1 << (SHORT_SIZE - 1);
	public static final int SHORT_MASK = MASKS[SHORT_SIZE];
	public static final int SKIP_SIGN_SHORT_MASK = MASKS[SHORT_SIZE - 1];

	public static final int INT_SIGN_BIT = 1 << (INT_SIZE - 1);
	public static final int SKIP_SIGN_INT_MASK = MASKS[INT_SIZE - 1];
	public static final long INT_MASK = 0xffffffffL;

	private static final int MAX_NOSIGN_SHORT = 0x7fff;

	public abstract int getLocation();

	public abstract int getLength();

	public abstract byte getByte();

	public abstract void seek(int loc);

	public final int getLeft() {
		return getLength() - getLocation();
	}

	protected final void assertLeft(int num) {
		if (!checkLeft(num))
			throw new BufferUnderflowException();
	}

	public final boolean checkLeft(int num) {
		int byteLeft = getLeft();
		return byteLeft >= num;
	}

	public final long getLong() {
		return getUInt() << 32 | getUInt();
	}

	public final long getUInt() {
		return getInt() & INT_MASK;
	}

	public final int getInt() {
		return joinBytes(getBytes(new byte[NUM_INT_BYTES]));
	}

	public final int getUShort() {
		return getShort() & SHORT_MASK;
	}

	public final short getShort() {
		return (short) joinBytes(getBytes(new byte[NUM_SHORT_BYTES]));
	}

	public final int getUByte() {
		return getByte() & BYTE_MASK;
	}

	public final int getUInt24() {
		return joinBytes(getBytes(new byte[24 / BYTE_SIZE]));
	}

	public final int getReferenceTableSmart() {
		return getSmart(NUM_SHORT_BYTES, NUM_INT_BYTES);
	}

	public final int getBigSmart() {
		byte[] smartBytes = getSmartBytes(NUM_SHORT_BYTES, NUM_INT_BYTES);
		int joined = joinBytes(smartBytes);
		if (smartBytes.length == NUM_SHORT_BYTES && joined == MAX_NOSIGN_SHORT)
			return -1;
		return joined;
	}

	public final int getSmart() {
		return getSmart(1, NUM_SHORT_BYTES);
	}

	private final int getSmart(int smallSize, int bigSize) {
		return joinBytes(getSmartBytes(smallSize, bigSize));
	}

	private final byte[] getSmartBytes(int smallSize, int bigSize) {
		byte first = getByte();
		int size = (first & BYTE_SIGN_BIT) == BYTE_SIGN_BIT ? bigSize : smallSize;
		byte[] inputBytes = new byte[size];
		inputBytes[0] = (byte) (first & ~BYTE_SIGN_BIT);
		return getBytes(inputBytes, 1, size - 1);
	}

	public final int getSmarts() {
		int i = 0;
		for (int j = MAX_NOSIGN_SHORT; j == MAX_NOSIGN_SHORT; i += j) {
			j = getSmart();
		}
		return i;
	}

	public byte[] getAllBytes() {
		byte[] bytes = new byte[getLength()];
		for (int i = 0; i < bytes.length; ++i)
			bytes[i] = getByte();
		return bytes;
	}

	public final byte[] getBytes(final byte[] buffer) {
		return getBytes(buffer, 0, buffer.length);
	}

	public final byte[] getBytes(final byte[] buffer, int off, int len) {
		assertLeft(len);
		len += off;
		for (int k = off; k < len; k++) {
			buffer[k] = getByte();
		}
		return buffer;
	}

	public final void printBytes(int num) {
		assertLeft(num);
		for (int i = 0; i < num; ++i) {
			System.out.printf("%02x ", getUByte());
		}
	}

	private static final char charSubs[] = { '\u20AC', '\0', '\u201A', '\u0192', '\u201E', '\u2026', '\u2020',
			'\u2021', '\u02C6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017D', '\0', '\0', '\u2018',
			'\u2019', '\u201C', '\u201D', '\u2022', '\u2013', '\u2014', '\u02DC', '\u2122', '\u0161', '\u203A',
			'\u0153', '\0', '\u017E', '\u0178' };

	public final String getString() {
		StringBuilder ret = new StringBuilder();
		int c;
		while ((c = getUByte()) != 0) {
			if (c >= 128 && c < 160) {
				char n = charSubs[c - 128];
				if (n == 0) {
					n = '?';
				}
				c = n;
			}
			ret.append((char) c);
		}
		return ret.toString();
	}

	public final String getJagString() {
		if (getUByte() != 0)
			throw new RuntimeException();
		return getString();
	}

	public final void reset() {
		seek(0);
	}

	public final byte[] decompress() {
		reset();
		int compression = getUByte(), size = getInt();
		if (size < 0)
			throw new IndexOutOfBoundsException();
		if (compression == 0) {
			return getBytes(new byte[size]);
		} else {
			return decompress(compression);
		}	
	}
	
	private final byte[] decompress(int compression) {
		int decompressedSize = getInt();
		if (decompressedSize < 0)
			throw new IndexOutOfBoundsException();
		byte decompressed[] = new byte[decompressedSize];
		if (compression == 1) {
			decompressBzip2(decompressed);
		} else {
			decompressGzip(decompressed);
		}
		return decompressed;
	}
	
	private final void decompressBzip2(byte[] decompressed) {
		byte[] payload = getAllBytes();
		BZip2Decompressor.decompress(decompressed, decompressed.length, payload, 0, 9);
	}
	
	private final void decompressGzip(byte[] decompressed) {
		byte[] payload = getAllBytes();
		int location = getLocation();
		if (payload[location] != 31 || payload[++location] != -117) {
			throw new RuntimeException("Invalid GZIP header!");
		}
		Inflater inflater = new Inflater(true);
		try {
			inflater.setInput(payload, location + 10, getLength() - location - 18);
			inflater.inflate(decompressed);
		} catch (Exception ex) {
			inflater.reset();
			throw new RuntimeException("Invalid GZIP compressed data!");
		}
		inflater.reset();
	}

	public void skip(int num) {
		assertLeft(num);
		seek(num + getLocation());
	}

	public static final int joinBytes(byte... bytes) {
		if (bytes.length > 4)
			throw new IllegalArgumentException();
		int joined = 0;
		for (int i = 0; i < bytes.length; ++i) {
			joined <<= BYTE_SIZE;
			joined |= bytes[i] & BYTE_MASK;
		}
		return joined;
	}

	public static final int joinBytes(int... bytes) {
		if (bytes.length > 4)
			throw new IllegalArgumentException();
		int joined = 0;
		for (int i = 0; i < bytes.length; ++i) {
			joined <<= BYTE_SIZE;
			joined |= bytes[i] & BYTE_MASK;
		}
		return joined;
	}

	public static byte[] unwrapBuffer(Object o, boolean copy) {
		if (o instanceof byte[]) {
			byte[] ret = (byte[]) o;
			if (copy) {
				return Arrays.copyOf(ret, ret.length);
			} else {
				return ret;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static byte[] unwrapBuffer(Object o) {
		return unwrapBuffer(o, false);
	}

	public static Object wrapBuffer(byte[] b, boolean copy) {
		return copy ? Arrays.copyOf(b, b.length) : b;
	}

	public static Object wrapBuffer(byte[] b) {
		return wrapBuffer(b, false);
	}

}
