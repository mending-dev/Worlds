package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class TeleportCommand implements ICommand {

    private final Worlds plugin;

    public TeleportCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("teleport")
            .requires(sender -> sender.getSender().hasPermission("worlds.command.teleport"))
            .then(Commands.argument("world", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    plugin.getServer().getWorlds().stream()
                        .map(World::getName)
                        .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                        .forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(ctx -> {
                    if (ctx.getSource().getSender() instanceof Player player) {
                        final String worldName = ctx.getArgument("world", String.class);
                        return teleport(worldName, ctx.getSource().getSender(), player);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("player", ArgumentTypes.player())
                    .executes(ctx -> {
                        final Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                        final String worldName = ctx.getArgument("world", String.class);
                        return teleport(worldName, ctx.getSource().getSender(), player);
                    })
                )
            )
            .build();
    }

    private int teleport(String worldName, CommandSender sender, Player target) {

        final World world = plugin.getServer().getWorld(worldName);

        if (world == null) {
            sender.sendMessage(plugin.getLanguage().get("notFound")
                    .replaceText(Lang.replace("%world%", worldName))
            );
            return Command.SINGLE_SUCCESS;
        }

        target.teleport(world.getSpawnLocation());
        return Command.SINGLE_SUCCESS;
    }
}
