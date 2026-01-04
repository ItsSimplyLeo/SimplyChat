package cx.leo.simplychat.utils;

import cx.leo.simplychat.SimplyChatPlugin;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultUtil {

    private static Chat chat;

    public static boolean checkEnabled(SimplyChatPlugin plugin) {
        var pm = plugin.getServer().getPluginManager();
        boolean enabled = pm.isPluginEnabled("Vault");

        if (!enabled) {
            plugin.getLogger().severe("Vault plugin not found!");
            plugin.getLogger().severe("Only default formats will be used.");
            return false;
        }

        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);

        if (rsp == null) {
            plugin.getLogger().warning("No eligible Chat/Permissions provider was found.");
            plugin.getLogger().warning("Only default formats will be used.");
            return false;
        }

        chat = rsp.getProvider();
        return true;
    }

    public static Chat getChat() {
        return chat;
    }

    public static String getPrimaryGroup(@NotNull Player player) {
        if (chat == null) return "";
        return chat.getPrimaryGroup(player);
    }

    public static String getPlayerPrefix(@NotNull Player player) {
        if (chat == null) return "";
        return chat.getPlayerPrefix(player);
    }

    public static String getPlayerSuffix(@NotNull Player player) {
        if (chat == null) return "";
        return chat.getPlayerSuffix(player);
    }
}
