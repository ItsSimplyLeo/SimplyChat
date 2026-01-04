package cx.leo.simplychat.channel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatChannelManager {

    private final List<ChatChannel> channels;

    public ChatChannelManager() {
        this.channels = new ArrayList<>();
    }

    @NotNull public ChatChannel getDefaultChannel() {
        return channels.get(0); // TODO: Add configuration
    }

    @Nullable
    public ChatChannel getChannelById(String id) {
        for (ChatChannel channel : channels) if (channel.getId().equalsIgnoreCase(id)) return channel;
        return null;
    }

}
