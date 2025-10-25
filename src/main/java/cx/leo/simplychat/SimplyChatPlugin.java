package cx.leo.simplychat;

import cx.leo.simplychat.commands.ChatCommands;
import cx.leo.simplychat.commands.ChatCommandManager;
import cx.leo.simplychat.commands.StyleCommands;
import cx.leo.simplychat.config.ConfigManager;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.data.impl.sqlite.SQLiteDataManager;
import cx.leo.simplychat.format.FormatManager;
import cx.leo.simplychat.listener.ChatListener;
import cx.leo.simplychat.listener.PlayerConnectionListener;
import cx.leo.simplychat.style.StyleManager;
import cx.leo.simplychat.user.UserManager;
import cx.leo.simplychat.utils.VaultUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SimplyChatPlugin extends JavaPlugin {

    private static SimplyChatPlugin instance;

    private boolean vaultEnabled;
    private ChatCommandManager commandManager;
    private ConfigManager configManager;
    private ChatManager chatManager;
    private FormatManager formatManager;
    private StyleManager styleManager;
    private DataManager dataManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        this.vaultEnabled = VaultUtil.checkEnabled(this);
        this.commandManager = new ChatCommandManager(this);
        this.configManager = new ConfigManager(this);
        this.chatManager = new ChatManager();
        this.formatManager = new FormatManager(this);
        this.styleManager = new StyleManager(this);
        this.dataManager = new SQLiteDataManager(this); // TODO Implement multiple ways to store data.
        this.userManager = new UserManager(this);

        this.registerEvent(new ChatListener(this));
        this.registerEvent(new PlayerConnectionListener(this));

        this.commandManager.parse(new ChatCommands(this));
        this.commandManager.parse(new StyleCommands(this));

        instance = this;
    }

    @Override
    public void onDisable() {
        getDataManager().disconnect();
        //getUserManager().updateUsers();
    }

    public void reload() {
        this.getConfigManager().reloadAll();
        this.formatManager.reload();
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
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

    public ChatManager getChatManager() {
        return chatManager;
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

    public static SimplyChatPlugin getInstance() {
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
