package cx.leo.simplychat.api;

import cx.leo.simplychat.api.database.UserService;
import org.jetbrains.annotations.NotNull;

public interface SimplyChatAPI {

    /**
     * Gets the UserService instance for managing users.
     *
     * @return The UserService instance.
     */
    @NotNull UserService getUserService();

}
