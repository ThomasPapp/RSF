package rsf.networking.session.codec;

import rsf.networking.session.Session;
import rsf.plugin.Plugin;

import java.nio.ByteBuffer;

/**
 * @author Thomas
 */
public interface ProtocolDecoder extends Plugin {

    boolean decode(Session session, ByteBuffer buffer);
}
