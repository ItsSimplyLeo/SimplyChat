package cx.leo.simplychat;

import cx.leo.simplychat.commands.ChatCommand;
import cx.leo.simplychat.commands.ChatCommandManager;
import cx.leo.simplychat.config.ConfigManager;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.data.impl.sqlite.SQLiteDataManager;
import cx.leo.simplychat.format.FormatManager;
import cx.leo.simplychat.listener.ChatListener;
import cx.leo.simplychat.listener.PlayerJoinListener;
import cx.leo.simplychat.listener.PlayerQuitListener;
import cx.leo.simplychat.style.StyleManager;
import cx.leo.simplychat.user.UserManager;
import cx.leo.simplychat.utils.VaultUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SimplyChat extends JavaPlugin {

    private static SimplyChat instance;

    private ChatCommandManager commandManager;
    private ConfigManager configManager;
    private FormatManager formatManager;
    private StyleManager styleManager;
    private DataManager dataManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        VaultUtil.checkEnabled(this);

        this.commandManager = new ChatCommandManager(this);
        this.configManager = new ConfigManager(this);
        this.formatManager = new FormatManager(this);
        this.styleManager = new StyleManager(this);
        this.dataManager = new SQLiteDataManager(this); // TODO Implement multiple ways to store data.
        this.userManager = new UserManager(this);

        this.registerEvent(new ChatListener(this));
        this.registerEvent(new PlayerJoinListener(this));
        this.registerEvent(new PlayerQuitListener(this));

        this.commandManager.parse(new ChatCommand(this));

        instance = this;
    }

    @Override
    public void onDisable() {
        getDataManager().disconnect();
        //getUserManager().updateUsers();
    }

    public void reload() {
        this.getConfigManager().reloadAll();
    }

    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public ChatCommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public StyleManager getStyleManager() {
        return styleManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public static SimplyChat getInstance() {
        return instance;
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return configManager.getMainConfig().yaml();
    }

    @Override
    public void reloadConfig() {
        configManager.getMainConfig().reload();
    }

    @Override
    public void saveConfig() {
        configManager.getMainConfig().save();
    }
}
