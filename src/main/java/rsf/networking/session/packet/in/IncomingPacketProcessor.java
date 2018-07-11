package rsf.networking.session.packet.in;

import rsf.Settings;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Thomas
 */
public class IncomingPacketProcessor {

    // the maximum amount of packets that can be processed and/or queued at once
    private static final int MAX_PACKETS = Settings.MAX_PLAYERS * 15;

    // the packets that are getting processed this game cycle
    private static final Deque<IncomingPacket> PROCESSING_PACKETS = new ArrayDeque<>(MAX_PACKETS);

    // the packets that are awaiting processing and will be processed next game cycle
    private static final Deque<IncomingPacket> AWAITING_PACKETS = new ArrayDeque<>(MAX_PACKETS);

    private static synchronized void sync(Runnable logic) {
        logic.run();
    }

    public static void await(IncomingPacket packet) {
        sync(() -> AWAITING_PACKETS.add(packet));
    }

    public static void process() {
        // send the awaiting packets over to the processing deque
        sync(() -> {
            PROCESSING_PACKETS.addAll(AWAITING_PACKETS);
            AWAITING_PACKETS.clear();
        });

        // process the packets
        IncomingPacket packet;
        while ((packet = PROCESSING_PACKETS.poll()) != null) {
            packet.decode();
        }
    }
}
