package cx.leo.simplychat.commands;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.style.Style;
import cx.leo.simplychat.user.User;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.jetbrains.annotations.NotNull;

public class StyleCommands {

    private final SimplyChatPlugin plugin;

    public StyleCommands(SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    private final static String NAMECOLOR_CLEAR = "nickname|nick|namecolor|namecolour";
    private final static String MESSAGE_COLOUR = "messagecolor|messagecolour";

    @Command(NAMECOLOR_CLEAR + " <style>")
    public void namecolor(@NotNull Player sender, @NotNull @Argument("style") Style style) {
        if (!sender.hasPermission("simplychat.style." + style.getId().toLowerCase())) {
            sender.sendMessage("You do not have permission to use this style.");
            return;
        }

        User user = plugin.getUserManager().getUser(sender);

        user.setNicknameStyle(style);

        this.plugin.getDataManager().updateUser(user).whenComplete(
                (result, error) -> {
                    if (error != null) {
                        sender.sendMessage("Failed to update your nickname style in the database.");
                        plugin.getLogger().severe("Error updating nickname style for " + sender.getName() + ": " + error.getMessage());
                    } else {
                        sender.sendMessage("Your nickname style has been set to " + style.getId() + ".");
                    }
                }
        );
    }

    @Command(NAMECOLOR_CLEAR + " clear")
    public void namecolorClear(@NotNull Player sender) {
        User user = plugin.getUserManager().getUser(sender);

        user.setNicknameStyle(null);

        this.plugin.getDataManager().updateUser(user).whenComplete(
                (result, error) -> {
                    if (error != null) {
                        sender.sendMessage("Failed to clear your nickname style in the database.");
                        plugin.getLogger().severe("Error clearing nickname style for " + sender.getName() + ": " + error.getMessage());
                    } else {
                        sender.sendMessage("Your nickname style has been cleared.");
                    }
                }
        );
    }

    @Command(MESSAGE_COLOUR + " <style>")
    public void messageColor(@NotNull Player sender, @NotNull @Argument("style") Style style) {
        if (!sender.hasPermission("simplychat.style." + style.getId().toLowerCase())) {
            sender.sendMessage("You do not have permission to use this style.");
            return;
        }

        User user = plugin.getUserManager().getUser(sender);

        user.setChatStyle(style);

        this.plugin.getDataManager().updateUser(user).whenComplete(
                (result, error) -> {
                    if (error != null) {
                        sender.sendMessage("Failed to update your chat style in the database.");
                        plugin.getLogger().severe("Error updating chat style for " + sender.getName() + ": " + error.getMessage());
                    } else {
                        sender.sendMessage("Your chat style has been set to " + style.getId() + ".");
                    }
                }
        );
    }

    @Command(MESSAGE_COLOUR + " clear")
    public void messageColorClear(@NotNull Player sender) {
        User user = plugin.getUserManager().getUser(sender);

        user.setChatStyle(null);

        this.plugin.getDataManager().updateUser(user).whenComplete(
                (result, error) -> {
                    if (error != null) {
                        sender.sendMessage("Failed to clear your chat style in the database.");
                        plugin.getLogger().severe("Error clearing chat style for " + sender.getName() + ": " + error.getMessage());
                    } else {
                        sender.sendMessage("Your chat style has been cleared.");
                    }
                }
        );
    }

}
