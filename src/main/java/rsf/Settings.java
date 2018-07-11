package rsf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Thomas
 */
public class Settings {

    // constant settings
    /** The maximum amount of players allowed in-game */
    public static final int MAX_PLAYERS = 2_000;

    // field settings - can be changed by the user
    private static int revision;
    private static boolean issac;
    private static String plugin_path;

    public static void load() {
        FileInputStream file;
        Properties properties = new Properties();
        try {
            file = new FileInputStream("./settings.properties");
            properties.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        revision = Integer.parseInt(properties.getProperty("revision"));
        issac = Boolean.parseBoolean(properties.getProperty("issac"));
        plugin_path = properties.getProperty("plugin_path");
    }

    public static int getRevision() {
        return revision;
    }

    public static boolean isIssac() {
        return issac;
    }

    public static String getPluginPath() {
        return plugin_path;
    }
}
