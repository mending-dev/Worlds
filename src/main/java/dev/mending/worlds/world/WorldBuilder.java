package dev.mending.worlds.world;

import dev.mending.worlds.Worlds;
import dev.mending.worlds.world.settings.WorldSettings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

@Getter @Setter
public class WorldBuilder {

    private final JavaPlugin plugin;
    private final String name;
    private final WorldSettings settings;

    public WorldBuilder(@Nonnull JavaPlugin plugin, @Nonnull String name, @Nonnull WorldSettings settings) {
        this.plugin = plugin;
        this.name = name;
        this.settings = settings;
    }

    public World create() {
        WorldCreator creator = new WorldCreator(this.name);
        creator.environment(settings.getEnvironment());
        creator.type(settings.getType());
        if (settings.getGenerator() != null) { creator.generator(settings.getGenerator()); }
        return Bukkit.getServer().createWorld(creator);
    }


}