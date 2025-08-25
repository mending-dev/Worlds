package dev.mending.worlds.config;

import com.google.gson.JsonObject;
import dev.mending.core.paper.api.config.json.Configuration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public class MainConfig extends Configuration {

    private boolean checkForUpdates;
    private boolean enableFlags;

    public MainConfig(@NotNull JavaPlugin plugin) {
        super(plugin, "config");
    }

    @Override
    public void onLoad(JsonObject json) {
        this.checkForUpdates = get("checkForUpdates", Boolean.class);
        this.enableFlags = get("enableFlags", Boolean.class);
    }
}
