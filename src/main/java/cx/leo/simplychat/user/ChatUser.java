package cx.leo.simplychat.user;

import cx.leo.simplychat.style.Style;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChatUser implements User {

    private final UUID uuid;

    private Style nicknameStyle = Style.DEFAULT, chatStyle = Style.DEFAULT;

    public ChatUser(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull Style getNicknameStyle() {
        return nicknameStyle;
    }

    @Override
    public void setNicknameStyle(Style nicknameStyle) {
        this.nicknameStyle = nicknameStyle;
    }

    @Override
    public @NotNull Style getChatStyle() {
        return chatStyle;
    }

    @Override
    public void setChatStyle(Style chatStyle) {
        this.chatStyle = chatStyle;
    }
}