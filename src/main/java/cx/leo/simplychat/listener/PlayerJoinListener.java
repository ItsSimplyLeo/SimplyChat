package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.CompletableFuture;

public class PlayerJoinListener implements Listener {

    private final SimplyChat plugin;

    public PlayerJoinListener(SimplyChat plugin) {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserManager userManager = plugin.getUserManager();
        DataManager dataManager = plugin.getDataManager();
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> dataManager.loadUser(player.getUniqueId()));

        future.thenAccept(user -> {
            if (user == null) userManager.createUser(player);
            else plugin.getUserManager().register(user);
        });
    }

}
