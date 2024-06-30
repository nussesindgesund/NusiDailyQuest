package pl.nusse.nusidailyquest.user;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.nusse.nusidailyquest.manager.DataManager;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final FileConfiguration fileConfiguration;
    public UserManager(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Getter
    public HashMap<UUID, User> userHashMap = new HashMap<>();

    public void removeUser(UUID uuid) {
        userHashMap.remove(uuid);
    }

    public void createUser(UUID uuid, String questname, int questtype, int before, boolean complete, long time) {
        userHashMap.put(uuid, new User(uuid, questname, questtype, before, false, 1));

        fileConfiguration.set("players." + uuid.toString() + ".questname", questname);
        fileConfiguration.set("players." + uuid.toString() + ".questtype", questtype);
        fileConfiguration.set("players." + uuid.toString() + ".before", before);
        fileConfiguration.set("players." + uuid.toString() + ".time", time);
        fileConfiguration.set("players." + uuid.toString() + ".complete", complete);
        DataManager.save();
    }


    public User getUser(UUID uuid) {
        return userHashMap.get(uuid);
    }

    public void loadUser(UUID uuid) {
        ConfigurationSection userSection = fileConfiguration.getConfigurationSection("players." + uuid.toString());
        int intQuestType = userSection.getInt(".questtype");
        String intQuestName = userSection.getString(".questname");
        int intBefore = userSection.getInt(".before");
        boolean complete = userSection.getBoolean(".complete");
        long time = userSection.getLong(".time");
        User user = new User(uuid, intQuestName, intQuestType, intBefore, complete, (int) time);
        userHashMap.put(uuid, user);
    }

    public boolean isExist(UUID uuid) {
        User user = getUser(uuid);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
}
