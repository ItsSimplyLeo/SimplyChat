package cx.leo.simplychat.api.channel;

import org.jetbrains.annotations.NotNull;

public interface ChatChannel extends ChatStatus {

    /**
     * Gets the unique identifier of this chat channel.
     *
     * @return The channel ID.
     */
    @NotNull String getId();

}
