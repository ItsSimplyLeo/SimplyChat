package cx.leo.simplychat.commands;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.style.Style;
import cx.leo.simplychat.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.jetbrains.annotations.NotNull;

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

    @Command(COMMAND_PREFIX + " style current")
    @Permission("simplychat.command.dev")
    public void devViewStyles(Player sender) {
        User user = plugin.getUserManager().getUser(sender);

        if (user == null) {
            sender.sendMessage("u dont exist m8");
            return;
        }

        Component component = Component.empty();

        Style nickname = user.getNicknameStyle();
        Style chat = user.getChatStyle();

        component = component.append(
                Component.text(sender.getName())
                        .append(Component.newline())
                        .append(Component.text("Nickname (" + nickname.getId() + "): " ).append(nickname.apply("THIS IS A TEST MESSAGE")))
                        .append(Component.newline())
                        .append(Component.text("Chat (" + chat.getId() + "): " ).append(chat.apply("THIS IS A TEST MESSAGE")))
        );

        sender.sendMessage(component);
    }

    @Command(COMMAND_PREFIX + " style update nickname <style>")
    @Permission("simplychat.command.dev")
    public void devTest(Player sender, @NotNull @Argument("style") Style style) {
        User user = plugin.getUserManager().getUser(sender);

        user.setNicknameStyle(style);
        sender.sendMessage("style updated");

        plugin.getDataManager().updateUser(user);
    }

    @Command(COMMAND_PREFIX + " style update chat <style>")
    @Permission("simplychat.command.dev")
    public void styleUpdateChat(Player sender, @NotNull @Argument("style") Style style) {
        User user = plugin.getUserManager().getUser(sender);

        user.setChatStyle(style);
        sender.sendMessage("style updated");

        plugin.getDataManager().updateUser(user);
    }

    @Command(COMMAND_PREFIX + " style reset")
    @Permission("simplychat.command.dev")
    public void reset(Player sender) {
        User user = plugin.getUserManager().getUser(sender);

        user.setNicknameStyle(Style.DEFAULT);
        user.setChatStyle(Style.DEFAULT);
        sender.sendMessage("style updated");

        plugin.getDataManager().updateUser(user);
    }

    @Command(COMMAND_PREFIX + " style test <style> <content>")
    @Permission("simplychat.command.dev")
    public void styleTest(Player sender, @NotNull @Argument("style") Style style, @NotNull @Greedy @Argument("content") String content) {
        sender.sendMessage(Component.text("Viewing Style: " + style.getId()).append(Component.newline()).append(style.apply(content)));
    }
}
