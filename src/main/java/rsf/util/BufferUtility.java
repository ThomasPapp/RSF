package rsf.util;

import java.nio.ByteBuffer;

public class BufferUtility {

    public static String getString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        byte b;
        while ((b = buffer.get()) != 0) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static String getRS2String(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        byte b;
        while ((b = buffer.get()) != 10) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    public static void putString(String string, ByteBuffer buffer) {
        buffer.put(string.getBytes());
        buffer.put((byte) 0);
    }

    public static int getSmart(ByteBuffer buffer) {
        int peek = buffer.get() & 0xFF;
        if (peek <= Byte.MAX_VALUE) {
            return peek;
        }
        return ((peek << 8) | (buffer.get() & 0xFF)) - 32768;
    }

    public static int getBigSmart(ByteBuffer buffer) {
        int value = 0;
        int current = getSmart(buffer);
        while (current == 32767) {
            current = getSmart(buffer);
            value += 32767;
        }
        value += current;
        return value;
    }

}
