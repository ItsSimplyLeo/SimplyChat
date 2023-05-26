package cx.leo.simplychat.user;

import cx.leo.simplychat.style.Style;

import java.util.UUID;

public interface User {

    UUID getUUID();

    Style getNicknameStyle();

    void setNicknameStyle(Style style);

    Style getChatStyle();

    void setChatStyle(Style style);
}
