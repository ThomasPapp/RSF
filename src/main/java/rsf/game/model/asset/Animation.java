package rsf.game.model.asset;

import rsf.engine.task.Task;
import rsf.game.model.character.CharacterAPI;

/**
 * @author Thomas
 */
public final class Animation {

    private final int id;

    private final int delay;

    private final int duration;

    public Animation(int id, int delay) {
        this.id = id;
        this.delay = delay;
        this.duration = 1; // TODO the actual duration from the cache
    }

    public Animation(int id) {
        this(id, 0);
    }

    /**
     * Adds a task which is executed when the animation stops playing
     * @param character the character the task is for
     * @param logic the logic of the task
     * @return the {@link Animation}
     */
    public Animation addTask(CharacterAPI character, Runnable logic) {
        Task task = new Task(duration, character) {
            @Override
            public boolean run() {
                logic.run();
                return false;
            }
            @Override
            public void stop() {
                logic.run();
                super.stop();
            }
        };
        if (character.isPlayer()) {
            character.asPlayer().getTaskManager().run(task);
        }
        return this;
    }

    public int getId() {
        return id;
    }

    public int getDelay() {
        return delay;
    }
}
