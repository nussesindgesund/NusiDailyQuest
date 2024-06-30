package pl.nusse.nusidailyquest;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.nusse.nusidailyquest.command.openQuestGui;
import pl.nusse.nusidailyquest.listener.onInventoryClick;
import pl.nusse.nusidailyquest.listener.onPlayerJoin;
import pl.nusse.nusidailyquest.manager.ConfigManager;
import pl.nusse.nusidailyquest.manager.DataManager;
import pl.nusse.nusidailyquest.user.UserManager;

import javax.xml.datatype.DatatypeConstants;

public final class NusiDailyQuest extends JavaPlugin {

    @Getter
    public static NusiDailyQuest instance;

    @Getter
    public static FileConfiguration configuration;

    @Getter
    public static FileConfiguration dataConfiguration;

    @Override
    public void onEnable() {

        instance = this;
        ConfigManager.start();
        DataManager.start();

        configuration = ConfigManager.getConfig();
        dataConfiguration = DataManager.getConfig();

        UserManager userManager = new UserManager(dataConfiguration);

        Bukkit.getPluginManager().registerEvents(new onInventoryClick(userManager), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(userManager), this);

        getCommand("dailyquest").setExecutor(new openQuestGui(userManager));

        for (Player player : Bukkit.getOnlinePlayers()) {
            userManager.loadUser(player.getUniqueId());
        }

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }
}
