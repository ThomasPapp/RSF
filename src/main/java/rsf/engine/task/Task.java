package rsf.engine.task;

import rsf.game.model.Model;
import rsf.game.model.character.CharacterAPI;

import java.util.Arrays;

/**
 * @author Thomas
 */
public abstract class Task {

    /** The amount of time (in ticks) to wait before executing the logic */
    protected int delay;

    protected boolean running = true;

    // the amount of ticks that've passed since the last execute
    private int ticks;

    // the characters attached to this task
    private CharacterAPI[] attachments;

    public Task(int delay, CharacterAPI... attachments) {
        this.delay = delay;
        this.attachments = attachments;
    }

    public Task(int delay) {
        this.delay = delay;
    }

    /** Creates a task running every game tick */
    public Task() {
        this(1);
    }

    public boolean start() {
        return running = true;
    }

    /**
     * Runs the task logic
     * @return {@code true} if the task should continue to run, otherwise {@code false}
     */
    public abstract boolean run();

    public void stop() {
        running = false;
    }

    boolean await() {
        boolean wait = ticks < delay;
        if (wait) ticks += 1;
        else ticks = 0;
        return wait;
    }

    boolean valid() {
        return running && (attachments == null || Arrays.stream(attachments).allMatch(Model::isActive));
    }

    public boolean isRunning() {
        return running;
    }
}
