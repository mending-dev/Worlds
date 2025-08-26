package dev.mending.worlds.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.mending.core.paper.api.config.json.Configuration;
import dev.mending.worlds.chunk.ChunkLoader;
import dev.mending.worlds.world.settings.WorldSettings;
import dev.mending.worlds.world.settings.WorldSettingsAdapter;
import lombok.Getter;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

        // Load worlds from file
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String name = entry.getKey();
            WorldSettings settings = gson.fromJson(entry.getValue(), WorldSettings.class);

            if (plugin.getServer().getWorld(name) == null) {
                World world = new WorldBuilder(plugin, name, settings).create();
                if (settings.getDifficulty() != null) {
                    world.setDifficulty(settings.getDifficulty());
                }
            }

            this.worlds.put(name, settings);
        }

        // Import already existing worlds
        plugin.getServer().getWorlds().forEach(world -> {
            if (!worlds.containsKey(world.getName())) {
                WorldSettings settings = new WorldSettings();
                settings.setEnvironment(world.getEnvironment());
                settings.setType(world.getWorldType());
                this.worlds.put(world.getName(), settings);
            }
        });

        // Load game-rules
        worlds.forEach((name, settings) -> {
            if (json.has(name)) {
                JsonObject gameRulesObj = json.getAsJsonObject(name).getAsJsonObject("gameRules");
                if (gameRulesObj != null) {
                    for (GameRule gameRule : GameRule.values()) {
                        if (gameRulesObj.has(gameRule.getName())) {
                            plugin.getServer().getWorld(name).setGameRule(gameRule, gameRulesObj.get(gameRule.getName()));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onPreSave(JsonObject json) {

        json.entrySet().clear();

        // Save settings
        worlds.forEach((name, settings) -> {
            for (Map.Entry<String, WorldSettings> entry : worlds.entrySet()) {
                json.add(entry.getKey(), gson.toJsonTree(entry.getValue(), WorldSettings.class));
            }
        });

        // Save game-rules
        plugin.getServer().getWorlds().forEach(world -> {

            JsonObject worldObj = json.getAsJsonObject(world.getName());
            JsonObject gameRulesObj = new JsonObject();
            Set<String> availableRules = new HashSet<>(Arrays.asList(world.getGameRules()));

            for (GameRule<?> rule : GameRule.values()) {
                if (!availableRules.contains(rule.getName())) {
                    continue; // diese Welt kennt die Regel nicht
                }

                Object value = world.getGameRuleValue(rule);
                if (value != null) {
                    switch (value) {
                        case Boolean b -> gameRulesObj.addProperty(rule.getName(), b);
                        case Integer i -> gameRulesObj.addProperty(rule.getName(), i);
                        case Double d -> gameRulesObj.addProperty(rule.getName(), d);
                        default -> gameRulesObj.addProperty(rule.getName(), value.toString());
                    }
                }
            }

            worldObj.add("gameRules", gameRulesObj);
        });
    }

    public void createWorld(String name, WorldSettings settings) {
        new WorldBuilder(plugin, name, settings).create();
        this.worlds.put(name, settings);
        save();
    }

}
