package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final SimplyChat plugin;

    public PlayerQuitListener(SimplyChat plugin) {
        this.plugin = plugin;
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        UserManager userManager = plugin.getUserManager();
        userManager.updateUser(userManager.getUser(event.getPlayer()));
    }

}
