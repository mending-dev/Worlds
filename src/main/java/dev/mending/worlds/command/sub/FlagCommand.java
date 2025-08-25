package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import dev.mending.worlds.world.WorldFlag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Locale;

public class FlagCommand implements ICommand {

    private final Worlds plugin;

    public FlagCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("flag")
            .requires(sender -> sender.getSender().hasPermission("worlds.command.flag"))
            .then(Commands.argument("world", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    plugin.getServer().getWorlds().stream()
                            .map(World::getName)
                            .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                            .forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .then(Commands.argument("id", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        Arrays.stream(WorldFlag.values())
                            .map(WorldFlag::name)
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

                        final String id = ctx.getArgument("id", String.class);
                        final WorldFlag flag = WorldFlag.valueOf(id);

                        boolean value = true;

                        if (plugin.getWorldManager().getWorlds().get(worldName).getFlags().containsKey(flag)) {
                            value = plugin.getWorldManager().getWorlds().get(worldName).getFlags().get(flag);
                        }

                        ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("flag")
                            .replaceText(Lang.replace("%flag%", flag.name()))
                            .replaceText(Lang.replace("%value%", "" + value))
                        );

                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(ctx -> {

                            final String worldName = ctx.getArgument("world", String.class);
                            final World world = plugin.getServer().getWorld(worldName);

                            if (world == null) {
                                ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("notFound")
                                        .replaceText(Lang.replace("%world%", worldName))
                                );
                                return Command.SINGLE_SUCCESS;
                            }

                            final String id = ctx.getArgument("id", String.class);
                            final boolean value = ctx.getArgument("value", Boolean.class);
                            final WorldFlag flag = WorldFlag.valueOf(id);

                            plugin.getWorldManager().getWorlds().get(worldName).getFlags().put(flag, value);
                            plugin.getWorldManager().save();

                            ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("flagUpdated")
                                .replaceText(Lang.replace("%flag%", flag.name()))
                                .replaceText(Lang.replace("%value%", "" + value))
                            );

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
            .build();
    }
}
