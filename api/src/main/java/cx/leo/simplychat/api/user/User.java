package cx.leo.simplychat.api.user;

import cx.leo.simplychat.api.channel.ChatChannel;
import cx.leo.simplychat.api.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public interface User {

    /**
     * Gets the UUID of the user.
     *
     * @return The UUID of the user.
     */
    @NotNull UUID getUUID();

    /**
     * Gets the last message time for the user.
     *
     * @return The time of the last message sent by the user, or null if not available.
     */
    @Nullable Instant getLastMessageTime();

    /**
     * Sets the last message time for the user.
     *
     * @param time The time to set as the last message time.
     */
    void setLastMessageTime(@Nullable Instant time);

    /**
     * Gets the user's nickname style.
     *
     * @return The style of the user's nickname.
     */
    @NotNull Style getNicknameStyle();

    /**
     * Sets the user's nickname style.
     *
     * @param style The style to set for the user's nickname.
     */
    void setNicknameStyle(@Nullable Style style);

    /**
     * Gets the user's chat style.
     *
     * @return The style of the user's chat.
     */
    @NotNull Style getChatStyle();

    /**
     * Sets the user's chat style.
     *
     * @param style The style to set for the user's chat.
     */
    void setChatStyle(@Nullable Style style);

    /**
     * Gets the user's current chat channel.
     *
     * @return The current chat channel of the user.
     */
    @NotNull ChatChannel getChatChannel();

    /**
     * Sets the user's current chat channel.
     *
     * @param channel The chat channel to set for the user.
     */
    void setChatChannel(@NotNull ChatChannel channel);
}
