package cx.leo.simplychat.user;

import cx.leo.simplychat.style.Style;

import java.util.UUID;

public class ChatUser implements User {

    private final UUID uuid;

    private Style nicknameStyle, chatStyle;

    public ChatUser(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Style getNicknameStyle() {
        return nicknameStyle;
    }

    @Override
    public void setNicknameStyle(Style nicknameStyle) {
        this.nicknameStyle = nicknameStyle;
    }

    @Override
    public Style getChatStyle() {
        return chatStyle;
    }

    @Override
    public void setChatStyle(Style chatStyle) {
        this.chatStyle = chatStyle;
    }
}