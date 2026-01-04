package cx.leo.simplychat.service;

import cx.leo.simplychat.api.database.UserDatabase;
import cx.leo.simplychat.api.database.UserService;
import cx.leo.simplychat.api.user.User;
import cx.leo.simplychat.user.UserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserServiceImpl extends UserService {

    public UserServiceImpl(@NotNull UserDatabase userDatabase) {
        super(userDatabase);
    }

    @Override
    public @NotNull User createUser(@NotNull UUID user) {
        return new UserImpl(user, null);
    }
}
