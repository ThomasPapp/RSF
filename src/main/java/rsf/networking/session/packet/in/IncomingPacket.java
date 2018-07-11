package rsf.networking.session.packet.in;

import rsf.game.model.character.player.PlayerAPI;
import rsf.networking.session.packet.ByteBuf;
import rsf.networking.session.packet.PacketDecoder;

/**
 * @author Thomas
 */
public final class IncomingPacket {

    private final PlayerAPI player;

    private final int id;

    private final int length;

    private final ByteBuf buf;

    private final PacketDecoder decoder;

    public IncomingPacket(PlayerAPI player, int id, int length, ByteBuf buf, PacketDecoder decoder) {
        this.player = player;
        this.id = id;
        this.length = length;
        this.buf = buf;
        this.decoder = decoder;
    }

    void decode() {
        decoder.decode(player, id, length, buf);
    }
}
