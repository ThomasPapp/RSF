package rsf.plugin;

import rsf.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Thomas
 */
public class PluginLoader {

    // the amount of plugins loaded
    private static int size;

    public static void load() {
        try {
            Files.walk(new File(Settings.getPluginPath()).toPath()).forEach(path -> {
                try {
                    File file = new File(path.toString());
                    if (file.isDirectory()) {
                        return;
                    }

                    if (!file.getName().endsWith(".class") || file.getName().contains("$") || file.getName().contains("package-info")) {
                        return;
                    }
                    String name = file.getPath().replace("out\\production\\classes\\plugins\\", "plugins.").replace("\\", ".").replace(".class", "");
                    Class<?> reflected_class;
                    reflected_class = Class.forName(name);
                    if (reflected_class == null) {
                        return;
                    }
                    if (!reflected_class.isAnnotationPresent(LoadablePlugin.class)) {
                        return;
                    }
                    Plugin plugin = (Plugin) reflected_class.newInstance();
                    plugin.configure();
                    size += 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[Plugin Loader] Loaded "+ size +" plugins!");
        }
    }

    /**
     * Gets the amount of plugins that are loaded
     * @return the amount of loaded plugins
     */
    public int getSize() {
        return size;
    }

}
