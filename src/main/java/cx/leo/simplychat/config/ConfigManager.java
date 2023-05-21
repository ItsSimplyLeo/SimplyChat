package cx.leo.simplychat.config;

import cx.leo.simplychat.SimplyChat;

public class ConfigManager {

    private final Config mainConfig;
    private final Config formatConfig;

    public ConfigManager(SimplyChat plugin) {
        this.mainConfig = new Config(plugin, "config");
        this.formatConfig = new Config(plugin, "formats");
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public Config getFormatConfig() {
        return formatConfig;
    }

    public void reloadAll() {
        formatConfig.reload();
        mainConfig.reload();
    }
}
