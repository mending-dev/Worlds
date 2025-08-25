package dev.mending.worlds.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.mending.core.paper.api.config.json.Configuration;
import dev.mending.worlds.world.settings.WorldSettings;
import dev.mending.worlds.world.settings.WorldSettingsAdapter;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WorldManager extends Configuration {

    private final Map<String, WorldSettings> worlds;

    public WorldManager(@NotNull JavaPlugin plugin) {
        super(plugin, "worlds");
        this.worlds = new HashMap<>();
    }

    @Override
    public Gson createGson() {
        return (new GsonBuilder())
            .registerTypeAdapter(WorldSettings.class, new WorldSettingsAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    }


    @Override
    public void onLoad(JsonObject json) {

        this.worlds.clear();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String name = entry.getKey();
            WorldSettings settings = gson.fromJson(entry.getValue(), WorldSettings.class);
            new WorldBuilder(plugin, name, settings).create();
            this.worlds.put(name, settings);
        }

        // DEBUG: Print worlds
        // TODO: Remove this code
        this.worlds.forEach((name, settings) -> {
            plugin.getLogger().info(name + ": " + settings.getType().name() + ", " + settings.getEnvironment().name());
        });
    }

    @Override
    public void onPreSave(JsonObject json) {
        json.entrySet().clear();
        worlds.forEach((name, settings) -> {
            for (Map.Entry<String, WorldSettings> entry : worlds.entrySet()) {
                json.add(entry.getKey(), gson.toJsonTree(entry.getValue(), WorldSettings.class));
            }
        });
    }

    public void createWorld(String name, WorldSettings settings) {
        new WorldBuilder(plugin, name, settings).create();
        this.worlds.put(name, settings);
    }

}
