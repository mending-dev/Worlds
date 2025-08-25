package dev.mending.worlds.world.settings;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

@Getter @Setter
public class WorldSettings {

    private WorldType type = WorldType.NORMAL;
    private World.Environment environment = World.Environment.NORMAL;
    private ChunkGenerator generator;

}
