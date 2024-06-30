package pl.nusse.nusidailyquest.manager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.nusse.nusidailyquest.NusiDailyQuest;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static File file;
    private static FileConfiguration config;

    public static void start() {
        setup();
        save();
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void setup() {
        file = new File(NusiDailyQuest.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            NusiDailyQuest.getInstance().saveResource("config.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException error) {
            error.printStackTrace();
        }
    }

    public static void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException error) {
            error.printStackTrace();
        }
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}