package cx.leo.simplychat.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.style.Style;
import cx.leo.simplychat.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    @CommandMethod("dev/view_styles")
    @CommandPermission("simplychat.comand.dev")
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
                Component.text("<b>" + sender.getName())
                        .append(Component.newline())
                        .append(Component.text("Nickname (" + nickname.getId() + "): " ).append(nickname.apply("THIS IS A TEST MESSAGE")))
                        .append(Component.newline())
                        .append(Component.text("Chat (" + chat.getId() + "): " ).append(chat.apply("THIS IS A TEST MESSAGE")))
        );

        sender.sendMessage(component);
    }

    @CommandMethod("dev/test_styles <style>")
    @CommandPermission("simplychat.comand.dev")
    public void devTest(Player sender, @NotNull @Argument("style") String styleName) {
        User user = plugin.getUserManager().getUser(sender);
        Style style = plugin.getStyleManager().getStyle(styleName);

        user.setNicknameStyle(style);
        user.setChatStyle(style);
        sender.sendMessage("style updated");

        plugin.getDataManager().updateUser(user);
    }

    @CommandMethod("dev/reset_styles")
    @CommandPermission("simplychat.comand.dev")
    public void reset(Player sender) {
        User user = plugin.getUserManager().getUser(sender);

        user.setNicknameStyle(Style.DEFAULT);
        user.setChatStyle(Style.DEFAULT);
        sender.sendMessage("style updated");

        plugin.getDataManager().updateUser(user);
    }
}
