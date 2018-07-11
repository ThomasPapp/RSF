package rsf.game.model.character;

import rsf.game.model.Model;
import rsf.game.model.character.player.PlayerAPI;
import rsf.game.render.flag.UpdateFlagger;

/**
 * @author Thomas
 */
public abstract class CharacterAPI extends Model {

    protected final UpdateFlagger update_flagger = new UpdateFlagger();

    public CharacterAPI(int id) {
        super(id);
    }

    /** Handles the pre-render conditions */
    public abstract void preRender();

    /** Handles the post-render conditions */
    public abstract void postRender();

    public PlayerAPI asPlayer() {
        return (PlayerAPI) this;
    }

    public boolean isPlayer() {
        return this instanceof PlayerAPI;
    }

    public UpdateFlagger getUpdateFlagger() {
        return update_flagger;
    }
}
