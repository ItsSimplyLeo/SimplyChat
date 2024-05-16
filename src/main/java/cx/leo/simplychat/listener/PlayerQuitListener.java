package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final SimplyChatPlugin plugin;

    public PlayerQuitListener(SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UserManager userManager = plugin.getUserManager();
        userManager.updateUser(userManager.getUser(event.getPlayer()));
    }

}
