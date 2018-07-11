package rs317.plugins.network

import rsf.networking.session.Session
import rsf.networking.session.codec.ProtocolCodec
import rsf.networking.session.codec.ProtocolDecoder
import rsf.plugin.LoadablePlugin
import rsf.plugin.PluginType
import java.nio.ByteBuffer

/**
 * @author Thomas
 */
@LoadablePlugin(type= PluginType.NETWORK)
class HandshakeDecoder: ProtocolDecoder {

    override fun configure() {
        ProtocolCodec.register("handshake", this)
    }

    override fun decode(session: Session?, buffer: ByteBuffer?): Boolean {
        val opcode = buffer!!.get()
        print(opcode)
        return true
    }
}