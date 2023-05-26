package cx.leo.simplychat.config;

import cx.leo.simplychat.SimplyChat;

public class ConfigManager {

    private final Config mainConfig;
    private final Config formatConfig;
    private final Config stylesConfig;

    public ConfigManager(SimplyChat plugin) {
        this.mainConfig = new Config(plugin, "config");
        this.formatConfig = new Config(plugin, "formats");
        this.stylesConfig = new Config(plugin, "styles");
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public Config getFormatConfig() {
        return formatConfig;
    }

    public Config getStylesConfig() {
        return stylesConfig;
    }

    public void reloadAll() {
        formatConfig.reload();
        mainConfig.reload();
        stylesConfig.reload();
    }
}
