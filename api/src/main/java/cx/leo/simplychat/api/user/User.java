package cx.leo.simplychat.api.user;

import cx.leo.simplychat.api.channel.ChatChannel;
import cx.leo.simplychat.api.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public interface User {

    @NotNull UUID getUUID();

    @Nullable Instant getLastMessageTime();

    @NotNull Style getNicknameStyle();

    void setNicknameStyle(Style style);

    @NotNull Style getChatStyle();

    void setChatStyle(Style style);

    @NotNull ChatChannel getChatChannel();

    void setChatChannel(@NotNull ChatChannel channel);
}
