package rsf.networking;

import rsf.engine.TaskExecutor;
import rsf.networking.session.Session;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * @author Thomas
 */
public class NetworkReactor {

    // so we cant run multiple reactors at once
    private static boolean running;

    // network fields
    private static ServerSocketChannel channel;
    private static Selector selector;

    private static final Runnable TASK = () -> {
        System.out.println("[Network Reactor] Awaiting connections...");
        while (running) {
            try {
                selector.selectNow();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            it.forEachRemaining(key -> {
                try {
                    if (!key.isValid()) {
                        key.cancel();
                        return;
                    }
                    if (key.isAcceptable()) {
                        SocketChannel socket_channel;
                        socket_channel = channel.accept();
                        if (socket_channel == null)
                            return;
                        socket_channel.configureBlocking(false);
                        SelectionKey session_key = socket_channel.register(selector, SelectionKey.OP_READ);
                        if (session_key == null) return;
                        Session session = new Session(session_key);
                        session_key.attach(session);
                    }
                    if (key.isReadable()) {
                        Object attachment = key.attachment();
                        if (attachment == null || !(attachment instanceof Session)) return;
                        ((Session) attachment).read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    public static void start() {
        if (running) {
            System.err.println("Reactor already running!");
            return;
        }

        // configure the reactor
        try {
            channel = ServerSocketChannel.open();
            selector = SelectorProvider.provider().openSelector();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            InetSocketAddress address = new InetSocketAddress(43594);
            channel.bind(address);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            running = true;
            TaskExecutor.execute(TASK);
        }
    }

}
