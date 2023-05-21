package cx.leo.simplychat.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderUtil {

    private static boolean papiEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static String papi(Player player, String content) {
        if (papiEnabled()) return PlaceholderAPI.setPlaceholders(player, content);
        else return content;
    }

    public static String parse(Player player, String content) {
        return papi(player, content);
    }

}
