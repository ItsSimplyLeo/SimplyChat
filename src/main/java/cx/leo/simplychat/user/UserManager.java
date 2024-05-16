package cx.leo.simplychat.user;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final SimplyChatPlugin plugin;
    private final HashMap<UUID, User> users;

    public UserManager(SimplyChatPlugin plugin) {
        this.plugin = plugin;
        this.users = new HashMap<>();
    }

    /**
     *
     * @param player Player to be converted
     * @return the {@link User} of a player
     */
    public User createUser(@NotNull Player player) {
        return new ChatUser(player.getUniqueId());
    }

    /**
     *
     * @param user User to be registered, should only be used by a {@link cx.leo.simplychat.data.DataManager}
     */
    public void register(@NotNull User user) {
        this.users.put(user.getUUID(), user);
    }

    /**
     *
     * @return Tracked users
     */
    public HashMap<UUID, User> getUsers() {
        return users;
    }

    /**
     *
     * @param player Player to get the {@link User} of
     * @return The {@link User} found from the {@link Player}
     */
    public @NotNull User getUser(Player player) {
        return users.get(player.getUniqueId());
    }

    /**
     *
     * @param player Player to get the {@link User} or create
     * @return The {@link User} from the {@link Player}
     */
    public @NotNull User getOrCreateUser(Player player) {
        User user;
        UUID uuid = player.getUniqueId();

        if (users.containsKey(uuid)) user = users.get(uuid);
        else user = new ChatUser(uuid);

        updateUser(user);
        users.put(uuid, user);

        return user;
    }

    /**
     * Unload and update a user
     *
     * @param user user to update and unload
     */
    public void unload(@NotNull User user) {
        updateUser(user);
        users.remove(user.getUUID());
    }

    /**
     *
     * @param user Specified user to update in the database
     */
    public void updateUser(User user) {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getDataManager().updateUser(user);
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Update all tracked users
     */
    public void updateUsers() {
        DataManager dataManager = plugin.getDataManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                users.values().forEach(dataManager::updateUser);
            }
        }.runTaskAsynchronously(plugin);
    }
}
