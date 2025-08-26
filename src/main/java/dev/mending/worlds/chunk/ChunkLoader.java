package dev.mending.worlds.chunk;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Async ChunkLoader for Paper: Pre-generates chunks around a world's spawn efficiently without blocking the server.
 */
public class ChunkLoader {

    private final JavaPlugin plugin;

    private final List<Chunk> chunks;
    private final World world;
    private final int radius;

    private BukkitRunnable task;

    /**
     * Initializes a new ChunkLoader.
     *
     * @param world  The world to generate chunks in.
     * @param radius The radius in chunks around the spawn to generate.
     */
    public ChunkLoader(@Nonnull World world, @Nonnegative int radius, @Nonnull JavaPlugin plugin) {
        this.world = world;
        this.radius = radius;
        this.chunks = new ArrayList<>();
        this.plugin = plugin;
    }

    /**
     * Generates chunks asynchronously with progress reporting.
     * The task stops cleanly when all chunks are generated.
     *
     * @param callback Callback to report progress (current, total).
     */
    public void generateAsync(ProgressCallback callback) {
        int centerX = world.getSpawnLocation().getBlockX() >> 4;
        int centerZ = world.getSpawnLocation().getBlockZ() >> 4;
        int diameter = radius * 2 + 1;
        int totalChunks = diameter * diameter;

        AtomicInteger count = new AtomicInteger(0);

        this.task = new BukkitRunnable() {
            int x = centerX - radius;
            int z = centerZ - radius;

            @Override
            public void run() {
                if (z > centerZ + radius) {
                    // Finished loading all chunks, stop task cleanly
                    this.cancel();
                    return;
                }

                Chunk chunk = world.getChunkAt(x, z);
                if (!chunk.isLoaded()) {
                    chunk.load();
                }
                chunks.add(chunk);

                int currentCount = count.incrementAndGet();
                if (callback != null) {
                    callback.progress(currentCount, totalChunks);
                }

                x++;
                if (x > centerX + radius) {
                    x = centerX - radius;
                    z++;
                }
            }
        };

        this.task.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    /**
     * Cancels the chunk loading task if running.
     */
    public void cancelTask() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    /**
     * Returns the list of chunks that have been generated so far.
     *
     * @return List of loaded chunks.
     */
    public List<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Interface for reporting progress.
     */
    public interface ProgressCallback {
        void progress(int current, int total);
    }
}