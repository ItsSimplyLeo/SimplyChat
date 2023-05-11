package cx.leo.simplychat.format;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class FormatManager {

    private final Format defaultFormat = new Format("default-backup", "<gray><name><dark_gray>: <white><message>");
    private final FileConfiguration configuration = null;
    private final HashMap<String, Format> formats = new HashMap<>();

    public FormatManager() {
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

        if (format == null) return defaultFormat;

        return format;
    }

    public void reload() {
        formats.clear();

        if (configuration == null) return;
        ConfigurationSection section = configuration.getConfigurationSection("groups");
        if (section == null) return;
        section.getKeys(false).forEach(
                group -> {
                    Format format = new Format(group.toLowerCase(), section.getString(group + ".format"));

                    ConfigurationSection actions = section.getConfigurationSection(group + ".actions");
                    if (section == null) {
                        formats.put(group.toLowerCase(), format);
                        return;
                    }

                    actions.getKeys(false).forEach(
                            actionId -> {
                                List<String> hover = actions.getStringList(actionId + ".HOVER_TEXT");
                                ClickEvent action = null;

                                for (ClickEvent.Action clickAction : ClickEvent.Action.values()) {
                                    if (actions.isSet(clickAction.name())) {
                                        action = ClickEvent.clickEvent(clickAction, actions.getString(actionId + "." + clickAction, "error"));
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
