package cx.leo.simplychat.api.channel;

public interface ChatStatus {

    /**
     * Checks if the chat is currently muted.
     *
     * @return true if the chat is muted, false otherwise.
     */
    boolean isChatMuted();

    /**
     * Mutes or unmutes the chat.
     *
     * @param muted true to mute the chat, false to unmute it.
     */
    void setChatMuted(boolean muted);

    /**
     * Checks if slow mode is enabled.
     *
     * @return true if slow mode is enabled, false otherwise.
     */
    boolean isSlowModeEnabled();

    /**
     * Enables or disables slow mode.
     *
     * @param enabled true to enable slow mode, false to disable it.
     */
    void setSlowModeEnabled(boolean enabled);

    /**
     * Sets the slow mode time in milliseconds.
     *
     * @param slowModeTime The slow mode time in milliseconds.
     */
    void setSlowModeTime(long slowModeTime);

    /**
     * Gets the slow mode time in milliseconds.
     *
     * @return The slow mode time in milliseconds.
     */
    long getSlowModeTime();

}
