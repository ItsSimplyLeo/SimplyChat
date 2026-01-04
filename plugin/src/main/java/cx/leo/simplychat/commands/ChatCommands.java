package cx.leo.simplychat.commands;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.api.channel.ChatChannel;
import cx.leo.simplychat.api.style.Style;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatCommands {

    private final static String COMMAND_PREFIX = "simplychat|chat";

    private final SimplyChatPlugin plugin;

    public ChatCommands(SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(COMMAND_PREFIX + " reload")
    @Permission("simplychat.command.reload")
    @CommandDescription("Reload SimplyChat's configuration")
    public void onChatReload(@NotNull CommandSender sender) {
        plugin.reload();
        sender.sendMessage(Component.text("SimplyChat has been reloaded!", NamedTextColor.AQUA));
    }

    @Command(COMMAND_PREFIX + " mute|lock")
    @Permission("simplychat.command.mute")
    @CommandDescription("Lock the chat, preventing players from sending messages")
    public void onChatMute(@NotNull CommandSender sender, @Nullable @Argument("channel") String channelId) { // TODO: Channel Argument
        if (channelId == null) {
            ChatChannel defaultChannel = plugin.getChatChannelManager().getDefaultChannel();
            channelId = defaultChannel.getId();
        }

        ChatChannel channel = plugin.getChatChannelManager().getChannelById(channelId);
        if (channel == null) return;

        boolean newState = !channel.isChatMuted();
        channel.setChatMuted(newState);

        if (newState) {
            sender.sendMessage(Component.text("Chat has been locked!", NamedTextColor.RED));
        } else {
            sender.sendMessage(Component.text("Chat has been unlocked!", NamedTextColor.GREEN));
        }
    }

    @Command(COMMAND_PREFIX + " slowmode")
    @Permission("simplychat.command.slowmode")
    @CommandDescription("Toggle slow mode for the chat")
    public void onChatSlowMode(@NotNull CommandSender sender, @Nullable @Argument("channel") String channelId) {
        if (channelId == null) {
            ChatChannel defaultChannel = plugin.getChatChannelManager().getDefaultChannel();
            channelId = defaultChannel.getId();
        }

        ChatChannel channel = plugin.getChatChannelManager().getChannelById(channelId);
        if (channel == null) return;

        boolean newState = !channel.isSlowModeEnabled();
        channel.setSlowModeEnabled(newState);

        if (newState) {
            sender.sendMessage(Component.text("Slow mode has been enabled!", NamedTextColor.YELLOW));
        } else {
            sender.sendMessage(Component.text("Slow mode has been disabled!", NamedTextColor.GREEN));
        }
    }

    @Command(COMMAND_PREFIX + " style test <style> <content>")
    @Permission("simplychat.command.dev")
    public void styleTest(Player sender, @NotNull @Argument("style") Style style, @NotNull @Greedy @Argument("content") String content) {
        sender.sendMessage(Component.text("Viewing Style: " + style.getId()).append(Component.newline()).append(style.apply(content)));
    }
}
