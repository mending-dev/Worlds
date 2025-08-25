package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ReloadCommand implements ICommand {

    private final Worlds plugin;

    public ReloadCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("reload")
            .requires(sender -> sender.getSender().hasPermission("worlds.command.reload"))
            .executes(ctx -> {
                plugin.getLanguage().reload();
                plugin.getWorldManager().reload();
                ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("reloaded"));
                return Command.SINGLE_SUCCESS;
            })
            .build();
    }
}
