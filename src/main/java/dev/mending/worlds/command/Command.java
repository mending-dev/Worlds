package dev.mending.worlds.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.sub.CreateCommand;
import dev.mending.worlds.command.sub.DeleteCommand;
import dev.mending.worlds.command.sub.ListCommand;
import dev.mending.worlds.command.sub.TeleportCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class Command implements ICommand {

    private final Worlds plugin;

    public Command(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("worlds")
            .requires(sender -> sender.getSender().hasPermission("worlds.command"))
            .then(new ListCommand(plugin).get())
            .then(new TeleportCommand(plugin).get())
            .then(new CreateCommand(plugin).get())
            .then(new DeleteCommand(plugin).get())
            .build();
    }
}
