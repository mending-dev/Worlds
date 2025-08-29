package dev.mending.worlds.world;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WorldHelper {

    public static List<String> loadGenerators(@Nonnull JavaPlugin plugin) {

        final Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
        final List<String> generators = new ArrayList<>();

        for (Plugin p : plugins) {
            if (p.isEnabled() && p.getDefaultWorldGenerator("world", "") != null) {
                generators.add(p.getDescription().getName());
            }
        }

        return generators;
    }

}
