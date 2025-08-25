package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;

import java.util.Locale;

public class DeleteCommand implements ICommand {

    private final Worlds plugin;

    public DeleteCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("delete")
            .requires(sender -> sender.getSender().hasPermission("worlds.command.delete"))
            .then(Commands.argument("world", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    plugin.getServer().getWorlds().stream()
                        .map(World::getName)
                        .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                        .forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(ctx -> {
                    final String worldName = ctx.getArgument("world", String.class);
                    final World world = plugin.getServer().getWorld(worldName);

                    if (world == null) {
                        ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("notFound")
                            .replaceText(Lang.replace("%world%", worldName))
                        );
                        return Command.SINGLE_SUCCESS;
                    }

                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("deleting")
                            .replaceText(Lang.replace("%world%", worldName))
                    );

                    plugin.getServer().unloadWorld(world, true);
                    plugin.getWorldManager().getWorlds().remove(worldName);
                    plugin.getWorldManager().save();

                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("deleted")
                            .replaceText(Lang.replace("%world%", worldName))
                    );

                    return Command.SINGLE_SUCCESS;
                })
            )
            .build();
    }
}
