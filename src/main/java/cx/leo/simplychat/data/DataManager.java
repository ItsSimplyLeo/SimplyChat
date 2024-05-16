package cx.leo.simplychat.data;

import cx.leo.simplychat.user.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataManager {

    void init();

    void connect();

    void disconnect();

    void reload();

    CompletableFuture<Optional<User>> loadUser(UUID uuid);

    void updateUser(User user);
}
