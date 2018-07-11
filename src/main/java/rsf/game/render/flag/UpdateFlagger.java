package rsf.game.render.flag;

import rsf.networking.session.packet.ByteBuf;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Thomas
 */
public final class UpdateFlagger {

    private final UpdateFlag<?>[] flags = new UpdateFlag[10];

    private int flag;

    private ByteBuf cachedBuf;

    public void flag(UpdateFlag<?> flag) {
        flags[flag.ordinal()] = flag;
        this.flag |= flag.bitmask();
    }

    public ByteBuf encode(boolean player) {
        if (cachedBuf != null) {
            return cachedBuf;
        }
        ByteBuf buf = new ByteBuf(ByteBuffer.allocate(2048));
        if (player && flag >= 0xff) {
            flag |= 0x20;
            buf.put(flag & 0xff);
            buf.put(flag >> 8);
        } else {
            buf.put(flag);
        }
        Arrays.stream(flags).filter(Objects::nonNull).forEachOrdered(flag -> flag.encode(buf));
        return cachedBuf = buf;
    }

    public void reset() {
        flag = 0;
        Arrays.fill(flags, null);
        cachedBuf = null;
    }
}
