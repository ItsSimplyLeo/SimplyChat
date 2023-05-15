package cx.leo.simplychat.utils;

import cx.leo.simplychat.SimplyChat;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtil {

    private static Chat chat;

    public static void checkEnabled(SimplyChat plugin) {
        var pm = plugin.getServer().getPluginManager();
        boolean enabled = pm.isPluginEnabled("Vault");

        if (!enabled) {
            plugin.getLogger().severe("Vault not found, disabling plugin.");
            plugin.getLogger().severe("Please install vault to use this plugin!");
            pm.disablePlugin(plugin);
            return;
        }

        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);

        if (rsp == null) {
            plugin.getLogger().severe("No eligible Chat/Permissions provider was found.");
            plugin.getLogger().severe("Please install a permissions plugin to use this plugin!");
            pm.disablePlugin(plugin);
            return;
        }

        chat = rsp.getProvider();
    }

    public static Chat getChat() {
        return chat;
    }

    public static String getPrimaryGroup(Player player) {
        return chat.getPrimaryGroup(player);
    }

    public static String getPlayerPrefix(Player player) {
        return chat.getPlayerPrefix(player);
    }

    public static String getPlayerSuffix(Player player) {
        return chat.getPlayerSuffix(player);
    }
}
