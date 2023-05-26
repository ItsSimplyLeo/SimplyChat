package cx.leo.simplychat.user;

import java.util.UUID;

public class ChatUser implements User {

    private final UUID uuid;

    public ChatUser(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}