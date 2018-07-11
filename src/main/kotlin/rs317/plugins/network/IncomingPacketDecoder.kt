package rs317.plugins.network

import rsf.networking.session.Session
import rsf.networking.session.codec.ProtocolCodec
import rsf.networking.session.codec.ProtocolDecoder
import rsf.networking.session.packet.ByteBuf
import rsf.networking.session.packet.`in`.IncomingPacket
import rsf.networking.session.packet.`in`.IncomingPacketProcessor
import rsf.plugin.LoadablePlugin
import rsf.plugin.PluginType
import java.nio.ByteBuffer


/**
 * The protocol decoder for incoming packets.
 *
 * Comes with RSF
 *
 * @author Thomas
 */
@LoadablePlugin(type= PluginType.NETWORK)
class IncomingPacketDecoder: ProtocolDecoder {

    // an array of all the packet sizes
    private val SIZES = intArrayOf(

    )

    override fun configure() {
        ProtocolCodec.register("incoming_packet", this)
    }

    override fun decode(session: Session?, buffer: ByteBuffer?): Boolean {
        while (buffer!!.hasRemaining()) {
            val opcode = buffer.get().toInt() and 0xff
            var size = SIZES[opcode]
            if (size == -3) {
                print("[Incoming Packet Decoder] Invalid packet opcode=$opcode")
                return true
            }
            if (size == -1) {
                size = buffer.get().toInt() and 0xff
            }
            if (buffer.remaining() < size) {
                print("{Incoming Packet Decoder] Invalid packet length opcode=$opcode")
                return true
            }
            val payload = ByteArray(size)
            buffer.get(payload)
            val packet = IncomingPacket(session!!.attachment, opcode, size, ByteBuf(ByteBuffer.wrap(payload)), null)
            IncomingPacketProcessor.await(packet)
        }
        return true
    }
}