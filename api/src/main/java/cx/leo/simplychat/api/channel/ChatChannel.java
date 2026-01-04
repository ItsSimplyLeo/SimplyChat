package cx.leo.simplychat.api.channel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ChatChannel {

    /**
     * Gets the unique identifier of this chat channel.
     *
     * @return The channel ID.
     */
    @NotNull String getId();

    /**
     * Gets the chat status of this channel.
     *
     * @return The chat status, or null if the channel has no special status.
     */
    @Nullable ChatStatus getStatus();

}
