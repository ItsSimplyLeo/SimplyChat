package cx.leo.simplychat.user;

import cx.leo.simplychat.api.channel.ChatChannel;
import cx.leo.simplychat.api.style.Style;
import cx.leo.simplychat.api.user.User;
import cx.leo.simplychat.style.StyleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public class ChatUser implements User {

    private final UUID uuid;

    private Instant lastMessageTime;
    private ChatChannel chatChannel;
    private Style nicknameStyle = StyleImpl.DEFAULT, chatStyle = StyleImpl.DEFAULT;

    public ChatUser(@NotNull UUID uuid, @NotNull ChatChannel chatChannel) {
        this.uuid = uuid;
        this.chatChannel = chatChannel;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @Nullable Instant getLastMessageTime() {
        return lastMessageTime;
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

    @Override
    public @NotNull ChatChannel getChatChannel() {
        return chatChannel;
    }

    @Override
    public void setChatChannel(@NotNull ChatChannel chatChannel) {
        this.chatChannel = chatChannel;
    }
}