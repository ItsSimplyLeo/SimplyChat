package cx.leo.simplychat.user;

import cx.leo.simplychat.SimplyChat;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final SimplyChat plugin;
    private final HashMap<UUID, User> users;

    public UserManager(SimplyChat plugin) {
        this.plugin = plugin;
        this.users = new HashMap<>();
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public User getOrCreateUser(Player player) {
        User user;
        UUID uuid = player.getUniqueId();

        if (users.containsKey(uuid)) user = users.get(uuid);
        else user = new ChatUser(uuid);

        return user;
    }
}
