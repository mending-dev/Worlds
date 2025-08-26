package dev.mending.worlds.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.sub.*;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class Command implements ICommand {

    private final Worlds plugin;

    public Command(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get(String literal) {

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(literal)
            .requires(sender -> sender.getSender().hasPermission("worlds.command"))
            .then(new ReloadCommand(plugin).get("reload"))
            .then(new ListCommand(plugin).get("list"))
            .then(new TeleportCommand(plugin).get("teleport"))
            .then(new CreateCommand(plugin).get("create"))
            .then(new CreateCommand(plugin).get("import"))
            .then(new DeleteCommand(plugin).get("delete"));

        if (plugin.getMainConfig().isEnableFlags()) {
            builder.then(new FlagCommand(plugin).get("flag"));
        }

        return builder.build();
    }
}
