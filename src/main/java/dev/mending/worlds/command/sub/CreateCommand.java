package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import dev.mending.worlds.world.settings.WorldSettings;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Locale;

public class CreateCommand implements ICommand {

    private final Worlds plugin;

    public CreateCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("create")
            .requires(sender -> sender.getSender().hasPermission("worlds.command.create"))
            .then(Commands.argument("name", StringArgumentType.word())
                .executes(ctx -> {
                    final String name = ctx.getArgument("name", String.class);
                    create(ctx.getSource().getSender(), name, new WorldSettings());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("environment", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        Arrays.stream(World.Environment.values())
                                .map(World.Environment::name)
                                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                .forEach(builder::suggest);
                        return builder.buildFuture();
                    })
                    .executes(ctx -> {

                        final String name = ctx.getArgument("name", String.class);
                        final String environment = ctx.getArgument("environment", String.class);
                        final WorldSettings settings = new WorldSettings();

                        settings.setEnvironment(World.Environment.valueOf(environment));

                        create(ctx.getSource().getSender(), name, settings);
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.argument("type", StringArgumentType.word())
                            .suggests((ctx, builder) -> {
                                Arrays.stream(WorldType.values())
                                        .map(WorldType::name)
                                        .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                        .forEach(builder::suggest);
                                return builder.buildFuture();
                            })
                            .executes(ctx -> {

                                final String name = ctx.getArgument("name", String.class);
                                final String environment = ctx.getArgument("environment", String.class);
                                final String type = ctx.getArgument("type", String.class);
                                final WorldSettings settings = new WorldSettings();

                                settings.setEnvironment(World.Environment.valueOf(environment));
                                settings.setType(WorldType.valueOf(type));

                                create(ctx.getSource().getSender(), name, settings);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
                )
            )
            .build();
    }

    private void create(CommandSender sender, String name, WorldSettings settings) {

        sender.sendMessage(plugin.getLanguage().get("creating")
                .replaceText(Lang.replace("%world%", name))
        );

        plugin.getWorldManager().createWorld(name, settings);

        sender.sendMessage(plugin.getLanguage().get("created")
                .replaceText(Lang.replace("%world%", name))
        );
    }
}
