package rsf.networking.session.packet;

import rsf.game.model.character.player.PlayerAPI;

public interface PacketDecoder {

    void decode(PlayerAPI player, int opcode, int length, ByteBuf buf);

}
