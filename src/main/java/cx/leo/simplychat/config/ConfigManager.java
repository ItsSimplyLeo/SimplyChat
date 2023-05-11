package cx.leo.simplychat.config;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.utils.Config;

public class ConfigManager {

    private final Config formatConfig;

    public ConfigManager(SimplyChat plugin) {

        this.formatConfig = new Config(plugin, "formats");
    }

    public Config getFormatConfig() {
        return formatConfig;
    }

    public void reloadAll() {
        formatConfig.reload();
    }
}
