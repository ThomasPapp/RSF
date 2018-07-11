package rsf.networking.session.codec;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import rsf.networking.session.Session;
import rsf.networking.session.packet.ByteBuf;

import java.nio.ByteBuffer;

/**
 * @author Thomas
 */
public final class ProtocolCodec {

    private static final BiMap<String, ProtocolDecoder> DECODERS = HashBiMap.create();

    private final Session session;

    private ProtocolDecoder decoder;

    public ProtocolCodec(Session session) {
        this.session = session;
        // we start with the handshake decoder
        this.decoder = DECODERS.get("handshake");
    }

    public static void register(String name, ProtocolDecoder decoder) {
        if (!DECODERS.containsKey(name))
            DECODERS.put(name, decoder);
    }

    public boolean decode(ByteBuffer buffer) {
        return decoder != null && decoder.decode(session, buffer);
    }

    public void set(String name) {
        decoder = DECODERS.get(name);
    }

    @SuppressWarnings("incomplete-switch")
    public void encode(ByteBuf buf) {
        if (buf.getOpcode() == -1) {
            session.write(buf.getBuffer());
            return;
        }
        int opcode = buf.getOpcode();
        ByteBuffer buffer = ByteBuffer.allocate(buf.getBuffer().position() + 4);
        buffer.put((byte) (opcode/* + session.getOutgoingRandom().getNextValue()*/));
        switch (buf.getType()) {
        case VAR:
            buffer.put((byte) buf.getBuffer().position());
            break;
        case VAR_SHORT:
            buffer.putShort((short) buf.getBuffer().position());
            break;
        }
        buf.getBuffer().flip();
        buffer.put(buf.getBuffer());
        session.write(buffer);
    }
}
