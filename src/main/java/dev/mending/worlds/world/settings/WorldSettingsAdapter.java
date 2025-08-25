package dev.mending.worlds.world.settings;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.lang.reflect.Type;

public class WorldSettingsAdapter implements JsonSerializer<WorldSettings>, JsonDeserializer<WorldSettings> {

    public JsonElement serialize(WorldSettings src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getType().name());
        obj.addProperty("environment", src.getEnvironment().name());

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

        return settings;
    }
}
