package cx.leo.simplychat.format;

import cx.leo.simplychat.SimplyChat;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class FormatManager {

    private final SimplyChat plugin;
    private final Format defaultFormat = new Format("default-backup", "<gray><name><dark_gray>: <white><message>");
    private final HashMap<String, Format> formats = new HashMap<>();

    public FormatManager(SimplyChat plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public HashMap<String, Format> getFormats() {
        return formats;
    }

    public @NotNull Format getDefaultFormat() {
        return getFormat("default");
    }

    public @NotNull Format getFormat(String formatName) {
        Format format = formats.get(formatName.toLowerCase());
        if (format == null) return formats.getOrDefault("default", defaultFormat);
        return format;
    }

    public void reload() {
        formats.clear();

        YamlConfiguration config = plugin.getConfigManager().getFormatConfig().yaml();
        if (config == null) return;
        ConfigurationSection section = config.getConfigurationSection("formats");
        if (section == null) return;
        section.getKeys(false).forEach(
                group -> {
                    Format format = new Format(group.toLowerCase(), section.getString(group + ".chat"));

                    ConfigurationSection actions = section.getConfigurationSection(group + ".actions");
                    if (actions == null) {
                        formats.put(group.toLowerCase(), format);
                        return;
                    }

                    actions.getKeys(false).forEach(
                            actionId -> {
                                List<String> hover = actions.getStringList(actionId + ".HOVER_TEXT");
                                ClickEvent action = null;

                                for (ClickEvent.Action clickAction : ClickEvent.Action.values()) {
                                    if (actions.isSet(actionId + "." + clickAction.name())) {
                                        action = ClickEvent.clickEvent(clickAction, actions.getString(actionId + "." + clickAction.name(), "error"));
                                        break;
                                    }
                                }

                                format.addActions(actionId, new FormatActions(actionId, action, hover));
                            }
                    );

                    formats.put(group.toLowerCase(), format);
                }
        );
    }

}
