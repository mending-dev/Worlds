package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
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

                    delete(ctx.getSource().getSender(), worldName, true);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("deleteFolder", BoolArgumentType.bool())
                    .executes(ctx -> {

                        final String worldName = ctx.getArgument("world", String.class);
                        final boolean deleteFolder = ctx.getArgument("deleteFolder", Boolean.class);

                        delete(ctx.getSource().getSender(), worldName, false);

                        if (deleteFolder) {
                            try {
                                FileUtils.deleteDirectory(new File(plugin.getServer().getWorldContainer(), worldName));
                            } catch (IOException exception) {
                                plugin.getLogger().severe("Error while deleting world directory: " + exception.getMessage());
                            }
                        }

                        return Command.SINGLE_SUCCESS;
                    })
                )
            )
            .build();
    }

    private void delete(CommandSender sender, String worldName, boolean saveWorld) {

        final World world = plugin.getServer().getWorld(worldName);

        if (world == null) {
            sender.sendMessage(plugin.getLanguage().get("notFound")
                .replaceText(Lang.replace("%world%", worldName))
            );
            return;
        }

        sender.sendMessage(plugin.getLanguage().get("deleting")
            .replaceText(Lang.replace("%world%", worldName))
        );

        plugin.getServer().unloadWorld(world, saveWorld);
        plugin.getWorldManager().getWorlds().remove(worldName);
        plugin.getWorldManager().save();

        sender.sendMessage(plugin.getLanguage().get("deleted")
            .replaceText(Lang.replace("%world%", worldName))
        );
    }
}
