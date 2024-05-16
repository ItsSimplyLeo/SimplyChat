package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PlayerConnectionListener implements Listener {

    private final SimplyChatPlugin plugin;

    public PlayerConnectionListener(SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserManager userManager = plugin.getUserManager();
        DataManager dataManager = plugin.getDataManager();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            CompletableFuture<Optional<User>> userFuture = dataManager.loadUser(player.getUniqueId());
            userFuture.whenComplete((optionalUser, throwable) -> {
                if (optionalUser.isPresent()) {
                    userManager.register(optionalUser.get());
                } else {
                    dataManager.updateUser(userManager.createUser(player));
                }
            });
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UserManager userManager = plugin.getUserManager();
        userManager.updateUser(userManager.getUser(event.getPlayer()));
    }

}