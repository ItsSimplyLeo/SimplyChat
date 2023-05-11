package cx.leo.simplychat.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    private final JavaPlugin plugin;
    private final String name;
    private YamlConfiguration yamlConfiguration;

    public Config(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.reload();
    }

    public YamlConfiguration yaml() {
        return yamlConfiguration;
    }

    public String name() {
        return name;
    }

    public String getFullName() {
        return name + ".yml";
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), getFullName());

        if (!file.exists()) {
            boolean success = file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        File file = new File(plugin.getDataFolder(), getFullName());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
