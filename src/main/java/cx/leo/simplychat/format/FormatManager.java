package cx.leo.simplychat.format;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;

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

    public Format getDefaultFormat() {
        return defaultFormat;
    }

    public @NotNull Format getFormat(String formatName) {
        Format format = formats.get(formatName.toLowerCase());

        if (format == null) return getDefaultFormat();

        return format;
    }

    public void reload() {
        formats.clear();

        // group.default.format/hover-text
        if (configuration == null) return;
        ConfigurationSection section = configuration.getConfigurationSection("groups");
        if (section == null) return;
        section.getKeys(false).forEach(
                group -> {
                    Format format = new Format(group.toLowerCase(), section.getString(group + ".format"));

                    ConfigurationSection hoverSec = section.getConfigurationSection(group + ".hover-text");
                    if (section == null) {
                        formats.put(group.toLowerCase(), format);
                        return;
                    }

                    hoverSec.getKeys(false).forEach(
                            hoverId -> {
                                FormatHover hover = new FormatHover(hoverId, Collections.singletonList(hoverSec.getString(hoverId)));
                                format.addHover(hover);
                            }
                    );

                    formats.put(group.toLowerCase(), format);
                }
        );
    }

}
