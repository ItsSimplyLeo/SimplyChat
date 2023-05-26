package cx.leo.simplychat.user;

import cx.leo.simplychat.style.Style;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {

    @NotNull UUID getUUID();

    @NotNull Style getNicknameStyle();

    void setNicknameStyle(Style style);

    @NotNull Style getChatStyle();

    void setChatStyle(Style style);
}
