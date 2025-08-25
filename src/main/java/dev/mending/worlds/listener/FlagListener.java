package dev.mending.worlds.listener;

import dev.mending.worlds.Worlds;
import dev.mending.worlds.world.WorldFlag;
import dev.mending.worlds.world.settings.WorldSettings;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FlagListener implements Listener {

    private final Worlds plugin;

    public FlagListener(Worlds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        final WorldSettings settings = plugin.getWorldManager().getWorlds().get(e.getEntity().getWorld().getName());
        if (settings.getFlags().containsKey(WorldFlag.PVP) && !settings.getFlags().get(WorldFlag.PVP)) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final WorldSettings settings = plugin.getWorldManager().getWorlds().get(e.getBlock().getWorld().getName());

        if (settings.getFlags().containsKey(WorldFlag.BLOCK_BREAK) && !settings.getFlags().get(WorldFlag.BLOCK_BREAK)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final WorldSettings settings = plugin.getWorldManager().getWorlds().get(e.getBlock().getWorld().getName());

        if (settings.getFlags().containsKey(WorldFlag.BLOCK_PLACE) && !settings.getFlags().get(WorldFlag.BLOCK_PLACE)) {
            e.setCancelled(true);
        }
    }

}
