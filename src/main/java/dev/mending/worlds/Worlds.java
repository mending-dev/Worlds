package dev.mending.worlds;

import dev.mending.core.paper.api.language.json.Language;
import dev.mending.worlds.command.Command;
import dev.mending.worlds.world.WorldManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class Worlds extends JavaPlugin {

    private final Language language = new Language(this);
    private final WorldManager worldManager = new WorldManager(this);

    @Override
    public void onEnable() {

        this.language.init();
        this.worldManager.init();

        registerCommands();
    }

    @Override
    public void onDisable() {
        this.worldManager.save();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new Command(this).get(), List.of("world"));
        });
    }
}
