package pl.nusse.nusidailyquest.listener;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.nusse.nusidailyquest.manager.ConfigManager;
import pl.nusse.nusidailyquest.manager.DataManager;
import pl.nusse.nusidailyquest.user.User;
import pl.nusse.nusidailyquest.user.UserManager;
import pl.nusse.nusidailyquest.util.ColorFixer;

public class onInventoryClick implements Listener {
    UserManager userManager;
    public onInventoryClick(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getView().getTitle().equals(ColorFixer.fixColor(ConfigManager.getConfig().getString("gui.title")))) {
            int slot = event.getSlot();
            event.setCancelled(true);

            if (slot == ConfigManager.getConfig().getInt("gui.mainitem.slot")) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                User user = userManager.userHashMap.get(player.getUniqueId());

                if (!user.isComplete()) {

                    if (userManager.userHashMap.containsKey(player.getUniqueId())) {
                        long lastUsageTime = user.getCurrentTime();
                        long currentTime = System.currentTimeMillis();
                        long cooldownTime = ConfigManager.getConfig().getInt("cooldown") * 1000;
                        if (currentTime - lastUsageTime < cooldownTime) {
                            long timeLeft = (cooldownTime - (currentTime - lastUsageTime)) / 1000;
                            player.sendMessage(ColorFixer.fixColor(ConfigManager.getConfig().getString("messages.timeleft")).replace("[TIMELEFT]", String.valueOf(timeLeft)));
                        }
                    }
                    
                    event.setCancelled(true);
                    return;
                }

                int stats = user.getQuestname().ordinal();
                int statsbefore = user.getBefore();
                int queststype = user.getQueststype();

                if ((stats - statsbefore) >= ConfigManager.getConfig().getInt("missions." + queststype + ".value")) {
                    new User(player.getUniqueId(), true);
                    DataManager.getConfig().set("players." + player.getUniqueId() + ".complete", true);
                    new User(player.getUniqueId(), System.currentTimeMillis());

                    StringList list = (StringList) ConfigManager.getConfig().getStringList("missions." + queststype + ".commands");
                    for (String command : list) {
                        String parsedCommand = command.replace("[PLAYER]", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
                    }
                }
                return;
            }
        }
    }
}
