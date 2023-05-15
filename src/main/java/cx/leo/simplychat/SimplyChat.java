package cx.leo.simplychat;

import cx.leo.simplychat.commands.ChatCommand;
import cx.leo.simplychat.config.ConfigManager;
import cx.leo.simplychat.format.FormatManager;
import cx.leo.simplychat.listener.ChatListener;
import cx.leo.simplychat.utils.VaultUtil;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplyChat extends JavaPlugin {

    private static SimplyChat instance;

    private ConfigManager configManager;
    private FormatManager formatManager;

    @Override
    public void onEnable() {
        VaultUtil.checkEnabled(this);

        this.configManager = new ConfigManager(this);
        this.formatManager = new FormatManager(this);

        this.registerEvent(new ChatListener(this));
        this.getCommand("chat").setExecutor(new ChatCommand(this));

        instance = this;
    }

    public void reload() {
        this.configManager.reloadAll();
    }

    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public static SimplyChat getInstance() {
        return instance;
    }
}
