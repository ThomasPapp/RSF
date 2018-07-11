package rsf.engine;

import rsf.engine.task.TaskManager;
import rsf.game.render.GameRenderer;
import rsf.networking.session.packet.in.IncomingPacketProcessor;
import rsf.util.Stopwatch;

/**
 * @author Thomas
 */
public class GameEngine implements Runnable {

    private static final long CYCLE_RATE = 600;

    private final Stopwatch stopwatch = new Stopwatch();

    private boolean running;

    public void start() {
        running = true;
        TaskExecutor.execute(this);
    }

    @Override
    public void run() {
        while (running) {
            // reset the stopwatch and start it back over
            stopwatch.reset();

            // handle the packet processing before any of the game
            IncomingPacketProcessor.process();

            // handle the tasks
            TaskManager.manage();

            // render the game
            GameRenderer.render();

            // sleep the thread
            long elapsed = stopwatch.getElapsed();
            long sleep_time = CYCLE_RATE - elapsed;
//            System.out.println("[Game Engine] Took " + elapsed +" milliseconds to process.");
            if (sleep_time < CYCLE_RATE) {
                try {
                    Thread.sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
