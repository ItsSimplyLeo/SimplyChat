package cx.leo.simplychat.api.database;

import cx.leo.simplychat.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserDatabase {

    @NotNull CompletableFuture<Optional<User>> getUser(@NotNull UUID uuid);

    @NotNull CompletableFuture<Void> saveUser(@NotNull User user);

}
