package cx.leo.simplychat.api.database;

import cx.leo.simplychat.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class UserService {

    private final Set<User> users = new HashSet<>();
    private final UserDatabase userDatabase;

    public UserService(@NotNull UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @NotNull
    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public CompletableFuture<User> loadUser(@NotNull UUID user) {
        return userDatabase.getUser(user).thenApply(optionalUser -> optionalUser.orElseGet(() -> {
            User newUser = createUser(user);
            users.add(newUser);
            return newUser;
        }));
    }

    public User getCachedUser(@NotNull UUID user) {
        return users.stream().filter(u -> u.getUUID().equals(user)).findFirst().orElse(null);
    }

    @NotNull
    public abstract User createUser(@NotNull UUID user);

}
