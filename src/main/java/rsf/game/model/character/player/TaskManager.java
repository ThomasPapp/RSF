package rsf.game.model.character.player;

import rsf.engine.task.Task;

/**
 * @author Thomas
 */
public final class TaskManager {

    private final PlayerAPI player;

    // the task which is currently running
    private Task task;

    public TaskManager(PlayerAPI player) {
        this.player = player;
    }

    public void run(Task task) {
        if (this.task != null) this.task.stop();
        rsf.engine.task.TaskManager.submit(this.task = task);
    }

    public void stop() {
        if (task != null) task.stop();
    }

}
