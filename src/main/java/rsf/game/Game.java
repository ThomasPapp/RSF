package rsf.game;

import rsf.Settings;
import rsf.game.model.character.player.PlayerAPI;

/**
 * @author Thomas
 */
public class Game {

    // all of the players that are currently in-game
    private static final Indexer<PlayerAPI> PLAYERS = new Indexer<>(Settings.MAX_PLAYERS);

    public static Indexer<PlayerAPI> getPlayers() {
        return PLAYERS;
    }

}
