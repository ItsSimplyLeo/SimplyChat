package cx.leo.simplychat.channel.impl;

import org.jetbrains.annotations.NotNull;

public class ChatChannelImpl {

    private final String id;

    public ChatChannelImpl(@NotNull String id) {
        this.id = id;
    }

    @NotNull
    public String getId() {
        return id;
    }
}
