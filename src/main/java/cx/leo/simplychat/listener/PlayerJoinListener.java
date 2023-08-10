package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SimplyChat plugin;

    public PlayerJoinListener(SimplyChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserManager userManager = plugin.getUserManager();
        DataManager dataManager = plugin.getDataManager();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            User user = dataManager.loadUser(player.getUniqueId());

            if (user == null) {
                user = userManager.createUser(player);
                dataManager.updateUser(user);
            }

            userManager.register(user);
        });
    }

}
