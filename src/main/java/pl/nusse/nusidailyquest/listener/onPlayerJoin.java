package pl.nusse.nusidailyquest.listener;

import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.nusse.nusidailyquest.manager.ConfigManager;
import pl.nusse.nusidailyquest.manager.DataManager;
import pl.nusse.nusidailyquest.user.User;
import pl.nusse.nusidailyquest.user.UserManager;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class onPlayerJoin implements Listener {

    UserManager userManager;

    public onPlayerJoin(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (userManager.isExist(player.getUniqueId())) {
            return;
        }

        if (userExist(player.getUniqueId())) {
            userManager.loadUser(player.getUniqueId());
            return;
        }
//
//        if (user.isComplete() || DataManager.getConfig().getString("players." + player.getUniqueId() + ".complete").contains("true")) return;

        ConfigurationSection missionsSection = ConfigManager.getConfig().getConfigurationSection("missions");
        if (missionsSection == null) {
            return;
        }

        Set<String> missionKeys = missionsSection.getKeys(false);
        int maxMissionNumber = findMaxMissionNumber(missionKeys);
        int mission = drawValue(maxMissionNumber);

        ConfigurationSection section = ConfigManager.getConfig().getConfigurationSection("missions");
        if (section == null) {
            System.err.println("Sekcja 'missions' jest null");
            return;
        }

        String missionPath = ConfigManager.getConfig().getString("missions." + mission + ".quest");
        player.sendMessage(String.valueOf(mission));
        if (missionPath == null || missionPath.isEmpty()) {
            System.err.println("Niepoprawna wartość missionPath: " + missionPath);
            return;
        }

        try {
            Statistic statistic = Statistic.valueOf(missionPath);
            userManager.createUser(
                    player.getUniqueId(),
                    missionPath, // Nazwa questa ud
                    mission, // 2 wartosc questa ud
                    player.getStatistic(statistic), // statystyka do pobrania
                    false,
                    0
            );
        } catch (IllegalArgumentException e) {
            System.err.println("Niepoprawna nazwa statystyki: " + missionPath);
            e.printStackTrace();
        }
    }

    public int drawValue(int maximum) {
        int minimum = 1;
        Random random = new Random();
        return random.nextInt(maximum - minimum + 1) + minimum;
    }

    public boolean userExist(UUID uuid) {
        ConfigurationSection section = DataManager.getConfig().getConfigurationSection("players");
        return section != null && section.getKeys(false).contains(uuid.toString());
    }

    private static int findMaxMissionNumber(Set<String> missionKeys) {
        int maxNumber = 0;
        for (String key : missionKeys) {
            try {
                int number = Integer.parseInt(key);
                if (number > maxNumber) {
                    maxNumber = number;
                }
            } catch (NumberFormatException e) {
                System.err.println("Błąd w pluginie! Klucz: " + key);
            }
        }
        return maxNumber;
    }
}