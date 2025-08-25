package dev.mending.worlds.world.settings;

import com.google.gson.*;
import dev.mending.worlds.world.WorldFlag;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.lang.reflect.Type;
import java.util.Map;

public class WorldSettingsAdapter implements JsonSerializer<WorldSettings>, JsonDeserializer<WorldSettings> {

    public JsonElement serialize(WorldSettings src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        obj.addProperty("type", src.getType().name());
        obj.addProperty("environment", src.getEnvironment().name());

        JsonObject flagObj = new JsonObject();

        for (WorldFlag flag : src.getFlags().keySet()) {
            flagObj.addProperty(flag.getName(), src.getFlags().get(flag));
        }

        if (!flagObj.isEmpty()) {
            obj.add("flags", flagObj);
        }

        return obj;
    }

    public WorldSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        WorldSettings settings = new WorldSettings();

        if (obj.has("type")) {
            settings.setType(WorldType.valueOf(obj.get("type").getAsString()));
        }

        if (obj.has("environment")) {
            settings.setEnvironment(World.Environment.valueOf(obj.get("environment").getAsString()));
        }

        if (obj.has("flags")) {
            JsonObject flagObj = obj.getAsJsonObject("flags");
            for (Map.Entry<String, JsonElement> entry : flagObj.entrySet()) {
                Boolean value = context.deserialize(entry.getValue(), Boolean.class);
                settings.getFlags().put(WorldFlag.getByName(entry.getKey()), value);
            }
        }

        return settings;
    }
}
