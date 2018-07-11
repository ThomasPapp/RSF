package rsf;

import rsf.engine.GameEngine;
import rsf.engine.TaskExecutor;
import rsf.networking.NetworkReactor;
import rsf.plugin.PluginLoader;

/**
 * @author Thomas
 */
public class RSF {

    // remove this method - here for testing purposes
    public static void main(String[] args) {

        // load the settings
        Settings.load();

        // load the plugins
        TaskExecutor.executeCached(PluginLoader::load);

        GameEngine engine = new GameEngine();
        engine.start();
        NetworkReactor.start();
    }

}
