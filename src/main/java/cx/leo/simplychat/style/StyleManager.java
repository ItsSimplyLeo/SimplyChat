package cx.leo.simplychat.style;

import cx.leo.simplychat.SimplyChat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class StyleManager {

    private final SimplyChat plugin;
    private final HashMap<String, Style> styles;

    public StyleManager(SimplyChat plugin) {
        this.plugin = plugin;
        this.styles = new HashMap<>();

        this.reloadStyles();
    }

    /**
     * Load styles from our configuration
     */
    private void reloadStyles() {
        this.styles.clear();

        FileConfiguration config = plugin.getConfigManager().getStylesConfig().yaml();
        ConfigurationSection sec = config.getConfigurationSection("styles");
        if (sec == null) return;

        sec.getKeys(false).forEach(
                id -> {
                    String typeName = sec.getString(id + ".type");
                    if (typeName == null) {
                        plugin.getLogger().warning("Type for Style ID (" + id + ") is null. Skipping over.");
                        return;
                    }

                    Style.Type type;

                    try {
                        type = Style.Type.valueOf(typeName.toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        plugin.getLogger().warning("Specified invalid style for ID (" + id + "). Skipping over.");
                        return;
                    }

                    @NotNull List<String> colors = sec.getStringList(id + ".colors");

                    Style style = new StyleImpl(id, type, colors.toArray(new String[0]));

                    this.styles.put(id.toLowerCase(), style);
                }
        );
    }

    /**
     *
     * @param id id of the style
     * @param type type of style
     * @param colors colors of the style
     *
     * @return If successfully created as long as id isn't already taken
     */
    public boolean create(String id, Style.Type type, String... colors) {
        StyleImpl style = new StyleImpl(id, type, colors);

        if (styles.containsKey(id.toLowerCase())) {
            return false;
        }

        this.styles.put(id.toLowerCase(), style);
        return true;
    }

    /**
     *
     * @return Map of all loaded styles
     */
    public @NotNull HashMap<String, Style> getStyles() {
        return styles;
    }

    /**
     *
     * @param id id to search for
     * @return Style found from id
     */
    public @Nullable Style getStyle(String id) {
        return styles.get(id.toLowerCase());
    }
}
