package rsf.engine.task;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Thomas
 */
public class TaskManager {

    private static final Deque<Task> RUNNING_TASKS = new ArrayDeque<>();

    private static final Deque<Task> AWAITING_TASKS = new ArrayDeque<>();

    private static final Deque<Task> DEAD_TASKS = new ArrayDeque<>();

    private static synchronized void sync(Runnable logic) {
        logic.run();
    }

    public static void submit(Task task) {
        sync(() -> AWAITING_TASKS.add(task));
    }

    public static void manage() {
        // send all the awaiting tasks to the running task deque
        sync(() -> {
            RUNNING_TASKS.addAll(AWAITING_TASKS);
            AWAITING_TASKS.clear();
        });

        // process and run the tasks
        RUNNING_TASKS.stream().filter(Task::await).forEach(t -> {
            if (!t.valid()) DEAD_TASKS.add(t);
            else if (!t.run()) DEAD_TASKS.add(t);
        });

        // remove all of the dead/non-running tasks
        DEAD_TASKS.forEach(RUNNING_TASKS::remove);
    }

}
