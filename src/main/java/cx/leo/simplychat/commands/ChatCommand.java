package cx.leo.simplychat.commands;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cx.leo.simplychat.SimplyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatCommand {

    private final SimplyChat plugin;

    public ChatCommand(SimplyChat plugin) {
        this.plugin = plugin;
    }

    @CommandMethod("chat reload")
    @CommandPermission("simplychat.command.reload")
    @CommandDescription("Reload SimplyChat's configuration")
    public void onChatReload(@NotNull CommandSender sender) {
        plugin.reload();
        sender.sendMessage(Component.text("SimplyChat has been reloaded!", NamedTextColor.AQUA));
    }
}
