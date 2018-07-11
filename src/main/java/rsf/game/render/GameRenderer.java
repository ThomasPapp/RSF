package rsf.game.render;

import rsf.game.Game;
import rsf.game.model.character.player.PlayerAPI;

/**
 * @author Thomas
 */
public class GameRenderer {

    // the game render for players
    private static GameRender playerRender;

    // the game render for npcs
    private static GameRender npcRender;

    public static synchronized void render() {
        // handle the pre-render conditions
        Game.getPlayers().forEach(PlayerAPI::preRender);

        // handle the rendering
        Game.getPlayers().forEach(player -> {
            playerRender.render(player);
            npcRender.render(player);
        });

        // handle the post-render conditions
        Game.getPlayers().forEach(PlayerAPI::postRender);
    }

}
