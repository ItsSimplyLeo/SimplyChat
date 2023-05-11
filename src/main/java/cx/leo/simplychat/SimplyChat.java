package cx.leo.simplychat;

import cx.leo.simplychat.format.FormatManager;
import cx.leo.simplychat.listener.ChatListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplyChat extends JavaPlugin {

    private static SimplyChat instance;

    private FormatManager formatManager;

    @Override
    public void onEnable() {
        this.formatManager = new FormatManager();
        this.registerEvent(new ChatListener(this));

        instance = this;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public static SimplyChat getInstance() {
        return instance;
    }
}
