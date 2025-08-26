package dev.mending.worlds.world.settings;

import dev.mending.worlds.world.WorldFlag;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class WorldSettings {

    private WorldType type = WorldType.NORMAL;
    private World.Environment environment = World.Environment.NORMAL;
    private Difficulty difficulty;
    private int preGenerateChunkRadius;
    private ChunkGenerator generator;
    private Map<WorldFlag, Boolean> flags = new HashMap<>();

}
