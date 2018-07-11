package rsf.game.model.character.player;

import rsf.game.model.character.CharacterAPI;
import rsf.networking.session.Session;

/**
 * @author Thomas
 */
public class PlayerAPI extends CharacterAPI {

    private final Session session;

    private final PlayerIdentification identification;

    private final TaskManager taskManager = new TaskManager(this);

    public PlayerAPI(Session session, PlayerIdentification identification) {
        super(-1);
        this.session = session;
        this.identification = identification;
    }

    @Override
    public void preRender() {

    }

    @Override
    public void postRender() {

    }

    public Session getSession() {
        return session;
    }

    public PlayerIdentification getIdentification() {
        return identification;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}
