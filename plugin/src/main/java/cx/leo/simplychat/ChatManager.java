package cx.leo.simplychat;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatManager {

    private final HashMap<UUID, Long> slowModeMap = new HashMap<>();

    private boolean chatMuted = false;
    private boolean slowModeEnabled = false;
    private long slowModeTime = TimeUnit.SECONDS.toMillis(3); // Default cooldown time of 3 seconds between chats

    public HashMap<UUID, Long> getSlowModeMap() {
        return slowModeMap;
    }

    public boolean chatMuted() {
        return chatMuted;
    }

    public void chatMuted(boolean muted) {
        this.chatMuted = muted;
    }

    public boolean slowModeEnabled() {
        return slowModeEnabled;
    }

    public void slowModeEnabled(boolean enabled) {
        this.slowModeEnabled = enabled;
    }

    public long slowModeTime() {
        return slowModeTime;
    }

    public void slowModeTime(long slowModeTime) {
        this.slowModeTime = slowModeTime;
    }
}
