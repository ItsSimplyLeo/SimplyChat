package cx.leo.simplychat.data;

import cx.leo.simplychat.user.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataManager {

    void init();

    void connect();

    void disconnect();

    void reload();

    User loadUser(UUID uuid);

    void updateUser(User user);
}
