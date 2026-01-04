package cx.leo.simplychat.api.channel;

public interface ChatStatus {

    /**
     * Checks if the chat is currently muted.
     *
     * @return true if the chat is muted, false otherwise.
     */
    boolean isChatMuted();

    /**
     * Checks if slow mode is enabled.
     *
     * @return true if slow mode is enabled, false otherwise.
     */
    boolean isSlowModeEnabled();

    /**
     * Gets the slow mode time in seconds.
     *
     * @return The slow mode time in seconds.
     */
    long getSlowModeTime(); // in seconds

}
