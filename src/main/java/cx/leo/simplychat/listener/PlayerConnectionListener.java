package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerConnectionListener implements Listener {

    private final SimplyChatPlugin plugin;

    public PlayerConnectionListener(final SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UserManager userManager = plugin.getUserManager();
        final DataManager dataManager = plugin.getDataManager();

        dataManager.loadUser(player.getUniqueId()).thenAccept(optionalUser -> optionalUser.ifPresentOrElse(userManager::register,
                () -> {
                    User newUser = userManager.createUser(player);
                    dataManager.updateUser(newUser);
                }
        ));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final UserManager userManager = plugin.getUserManager();
        final User user = userManager.getUser(event.getPlayer());
        userManager.updateUser(user);
    }
}