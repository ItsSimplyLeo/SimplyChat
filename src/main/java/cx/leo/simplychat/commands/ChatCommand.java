package cx.leo.simplychat.commands;

import cx.leo.simplychat.SimplyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatCommand implements CommandExecutor {

    private final SimplyChat plugin;

    public ChatCommand(SimplyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                sender.sendMessage(Component.text("SimplyChat has been reloaded!", NamedTextColor.AQUA));
            }
        }
        return true;
    }
}
