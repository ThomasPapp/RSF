package rsf.engine;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Thomas
 */
public class TaskExecutor {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Executor CACHED = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static void executeCached(Runnable runnable) {
        CACHED.execute(runnable);
    }

}
