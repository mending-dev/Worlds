package dev.mending.worlds.chunk;

import dev.mending.worlds.Worlds;
import org.bukkit.World;

public class ChunkHandler {

    public static void preGenerateChunks(Worlds plugin) {
        plugin.getWorldManager().getWorlds().forEach((name, settings) -> {
            if (settings.getPreGenerateChunkRadius() > 0) {
                World world = plugin.getServer().getWorld(name);
                if (world != null) {
                    plugin.getLogger().info(String.format("Starting pre-generation of chunks in world '%s'", name));
                    new ChunkLoader(world, 10, plugin).generateAsync((current, total) -> {
                        plugin.getLogger().info(String.format("Progress: %.2f%%", (current / (double) total) * 100));
                        if (current == total) {
                            plugin.getLogger().info(String.format("Finished pre-generation of chunks in world '%s'", name));
                        }
                    });
                }
            }
        });
    }

}
