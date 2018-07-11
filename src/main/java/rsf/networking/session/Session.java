package rsf.networking.session;

import rsf.game.model.character.player.PlayerAPI;
import rsf.networking.session.codec.ProtocolCodec;
import rsf.networking.session.packet.ByteBuf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Thomas
 */
public final class Session {

    // network fields
    private final SelectionKey key;
    private final SocketChannel channel;

    // the incoming packet buffer
    private final ByteBuffer buffer;

    // the protocol codec for this session
    private final ProtocolCodec codec = new ProtocolCodec(this);

    // the player that's attached to this session
    private PlayerAPI attachment;

    public Session(SelectionKey key) {
        this.key = key;
        this.channel = (SocketChannel) key.channel();
        this.buffer = ByteBuffer.allocate(812);
    }

    public void attach(PlayerAPI attachment) {
        this.attachment = attachment;
    }

    public void read() {
        try {
            buffer.clear();
            if (channel.read(buffer) == -1) {
                return;
            }
            buffer.flip();
            if (!codec.decode(buffer))
                disconnect(null);
        } catch (IOException e) {
            disconnect(e);
        }
    }

    public void write(ByteBuffer buffer) {
        try {
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            disconnect(e);
        }
    }

    public void write(ByteBuf buf) {
        codec.encode(buf);
    }

    private void disconnect(Exception e) {
        if (e != null && !(e.getMessage().contains("An existing connection") || e.getMessage().contains("An established connection was aborted"))) {
            e.printStackTrace();
        }
        try {
            key.attach(null);
            key.cancel();
            channel.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public ProtocolCodec getCodec() {
        return codec;
    }

    public PlayerAPI getAttachment() {
        return attachment;
    }
}
