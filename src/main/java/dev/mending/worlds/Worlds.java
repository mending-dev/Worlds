package dev.mending.worlds;

import dev.mending.core.paper.api.language.json.Language;
import dev.mending.worlds.chunk.ChunkLoader;
import dev.mending.worlds.command.Command;
import dev.mending.worlds.config.MainConfig;
import dev.mending.worlds.listener.FlagListener;
import dev.mending.worlds.world.WorldManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class Worlds extends JavaPlugin {

    private final Language language = new Language(this);
    private final MainConfig mainConfig = new MainConfig(this);
    private final WorldManager worldManager = new WorldManager(this);

    @Override
    public void onEnable() {

        this.language.init();
        this.mainConfig.init();
        this.worldManager.init();

        registerEvents(getServer().getPluginManager());
        registerCommands();

//        new ChunkLoader(getServer().getWorld("world"), 10, this).generateAsync((current, total) -> {
//            getLogger().info(String.format("Progress: %.2f%%", (current / (double) total) * 100));
//        });

        this.worldManager.save();
    }

    @Override
    public void onDisable() {
        this.worldManager.save();
    }

    private void registerEvents(final PluginManager pluginManager) {
        if (mainConfig.isEnableFlags()) {
            pluginManager.registerEvents(new FlagListener(this), this);
        }
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new Command(this).get("worlds"), List.of("world"));
        });
    }
}
