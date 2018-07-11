package rsf.networking.session.packet;

import rsf.util.BufferUtility;

import java.nio.ByteBuffer;

/**
 * @author Thomas
 */
public final class ByteBuf {

	private final static int BIT_MASKS[] = {
			0, 0x1, 0x3, 0x7,
			0xf, 0x1f, 0x3f, 0x7f,
			0xff, 0x1ff, 0x3ff, 0x7ff,
			0xfff, 0x1fff, 0x3fff, 0x7fff,
			0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
			0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
			0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
			0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
			-1
	};
	
	private ByteBuffer buffer;
	private PacketType type = PacketType.NORMAL;
	private int opcode = -1;
	private int bits;
	
	public ByteBuf(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public ByteBuf() {
		this(-1);
	}
	
	public ByteBuf(int opcode) {
		this(opcode, 5000, PacketType.NORMAL);
	}
	
	public ByteBuf(int opcode, PacketType type) {
		this(opcode, 5000, type);
	}
	
	public ByteBuf(int opcode, int capacity) {
		this(opcode, capacity, PacketType.NORMAL);
	}
	
	public ByteBuf(int opcode, int capacity, PacketType type) {
		this.setOpcode(opcode);
		this.buffer = ByteBuffer.allocate(capacity);
		this.setType(type);
	}
	
	public void put(int value) {
		buffer.put((byte) value);
	}

	public void put(byte[] payload) {
		buffer.put(payload);
	}
	
	public void putA(int value) {
		buffer.put((byte) (value + 128));
	}
	
	public void putC(int value) {
		buffer.put((byte) -value);
	}
	
	public void putS(int value) {
		buffer.put((byte) (128 - value));
	}
	
	public void putShort(int value) {
		buffer.putShort((short) value);
	}
	
	public void putShortA(int value) {
		buffer.put((byte) (value >> 8));
		buffer.put((byte) (value + 128));
	}
	
	public void putLEShort(int value) {
		buffer.put((byte) value);
		buffer.put((byte) (value >> 8));
	}
	
	public void putLEShortA(int value) {
		buffer.put((byte) (value + 128));
		buffer.put((byte) (value >> 8));
	}

	public void putTri(int value) {
		buffer.put((byte) (value >> 16));
		buffer.put((byte) (value >> 8));
		buffer.put((byte) value);
	}
	
	public void putInt(int value) {
		buffer.putInt(value);
	}
	
	public void putInt1(int value) {
		buffer.put((byte) (value >> 8));
		buffer.put((byte) value);
		buffer.put((byte) (value >> 24));
		buffer.put((byte) (value >> 16));
	}
	
	public void putInt2(int value) {
		buffer.put((byte) (value >> 16));
		buffer.put((byte) (value >> 24));
		buffer.put((byte) value);
		buffer.put((byte) (value >> 8));
	}
	
	public void putLEInt(int value) {
		buffer.put((byte) value);
		buffer.put((byte) (value >> 8));
		buffer.put((byte) (value >> 16));
		buffer.put((byte) (value >> 24));
	}
	
	public void putLong(long value) {
		buffer.putLong(value);
	}

	public void putJagString(String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 10);
	}

	public void putString(String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
	}

	public void putSmart(int value) {
		if (value > Byte.MAX_VALUE) {
			buffer.putShort((short) (value + 32768));
		} else {
			buffer.put((byte) value);
		}
	}

	
	public void startBitAccess() {
		bits = buffer.position() * 8;
	}

	public void finishBitAccess() {
		buffer.position((bits + 7) / 8);
	}
	
	public void putBits(int bit_amount, int value) {
		int bytePos = bits >> 3;
		int bitOffset = 8 - (bits & 7);
		bits += bit_amount;
		int pos = (bits + 7) / 8;
		buffer.position(pos);
		for (; bit_amount > bitOffset; bitOffset = 8) {
			int tmp = buffer.get(bytePos);
			tmp &= ~BIT_MASKS[bitOffset];
			tmp |= (value >> (bit_amount - bitOffset)) & BIT_MASKS[bitOffset];
			buffer.put(bytePos++, (byte) tmp);
			bit_amount -= bitOffset;
		}
		if (bit_amount == bitOffset) {
			int tmp = buffer.get(bytePos);
			tmp &= ~BIT_MASKS[bitOffset];
			tmp |= value & BIT_MASKS[bitOffset];
			buffer.put(bytePos, (byte) tmp);
		} else {
			int tmp = buffer.get(bytePos);
			tmp &= ~(BIT_MASKS[bit_amount] << (bitOffset - bit_amount));
			tmp |= (value & BIT_MASKS[bit_amount]) << (bitOffset - bit_amount);
			buffer.put(bytePos, (byte) tmp);
		}
	}
	
	public void put(ByteBuf buf) {
		buf.buffer.flip();
		buffer.put(buf.buffer);
	}
	
	public void put(ByteBuffer buf) {
		buf.flip();
		buffer.put(buf);
	}

	public void putA(ByteBuf buf) {
		buf.getBuffer().flip();
		while (buf.getBuffer().hasRemaining()) {
			putA(buf.getBuffer().get());
		}
	}

	public int get() {
		return buffer.get();
	}

	public int getA() {
		return (buffer.get() & 0xFF) - 128;
	}

	public int getC() {
		return -buffer.get();
	}

	public int getS() {
		return 128 - buffer.get();
	}

	public int getShort() {
		return buffer.getShort();
	}

	public int getLEShort() {
		return (buffer.get() & 0xFF) | ((buffer.get() & 0xFF) << 8);
	}

	public int getShortA() {
		return ((buffer.get() & 0xFF) << 8) | (buffer.get() - 128 & 0xFF);
	}

	public int getLEShortA() {
		return (buffer.get() - 128 & 0xFF) | ((buffer.get() & 0xFF) << 8);
	}

	public int getInt() {
		return buffer.getInt();
	}

	public int getLEInt() {
		return (buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8)
				+ ((buffer.get() & 0xFF) << 16) + ((buffer.get() & 0xFF) << 24);
	}

	public int getInt1() {
		return ((buffer.get() & 0xFF) << 8) + (buffer.get() & 0xFF)+ ((buffer.get() & 0xFF) << 24) + ((buffer.get() & 0xFF) << 16);
	}

	public int getInt2() {
		return ((buffer.get() & 0xFF) << 16) + ((buffer.get() & 0xFF) << 24)
				+ (buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8);
	}

	public long getLong() {
		return buffer.getLong();
	}

	public String getString() {
		return BufferUtility.getRS2String(buffer);
	}

	public String getJagString() {
		return BufferUtility.getRS2String(buffer);
	}

	public int getSmart() {
		return BufferUtility.getSmart(buffer);
	}

	public void getReverse(byte[] payload, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			payload[i] = buffer.get();
		}
	}
	
	public boolean isEmpty() {
		return !buffer.hasRemaining();
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	
}
