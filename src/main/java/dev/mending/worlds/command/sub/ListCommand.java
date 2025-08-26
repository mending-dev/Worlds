package dev.mending.worlds.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.worlds.Worlds;
import dev.mending.worlds.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;

import java.util.stream.Collectors;

public class ListCommand implements ICommand {

    private final Worlds plugin;

    public ListCommand(Worlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get(String literal) {
        return Commands.literal(literal)
                .requires(sender -> sender.getSender().hasPermission("worlds.command.list"))
                .executes(ctx -> {
                    ctx.getSource().getSender().sendMessage(
                        plugin.getLanguage().get("list")
                            .replaceText(Lang.replace("%worlds%", plugin.getServer().getWorlds().stream()
                                .map(World::getName)
                                .collect(Collectors.joining(", ")))
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
