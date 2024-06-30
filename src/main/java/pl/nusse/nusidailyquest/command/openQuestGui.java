package pl.nusse.nusidailyquest.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.nusse.nusidailyquest.manager.ConfigManager;
import pl.nusse.nusidailyquest.manager.DataManager;
import pl.nusse.nusidailyquest.user.User;
import pl.nusse.nusidailyquest.user.UserManager;
import pl.nusse.nusidailyquest.util.ColorFixer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountedCompleter;

public class openQuestGui implements CommandExecutor {

    UserManager userManager;

    public openQuestGui(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        User user = userManager.userHashMap.get(player.getUniqueId());
        if (user != null) {
            Statistic questname = user.getQuestname();
            int queststype = user.getQueststype();
            int before = user.getBefore();
        }

        player.openInventory(createGui(((Player) sender).getPlayer(), user.getQuestname(), user.getQueststype(), user.getBefore()));

        return false;
    }

    public Inventory createGui(Player player, Statistic questname, int queststype, int before) {
        Inventory gui = Bukkit.createInventory(player, ConfigManager.getConfig().getInt("gui.size"), ColorFixer.fixColor(ConfigManager.getConfig().getString("gui.title")));

        // Pobranie dekoracyjnych bloków z konfiguracji
        ConfigurationSection decorationBlocks = ConfigManager.getConfig().getConfigurationSection("gui.decorationblock");
        if (decorationBlocks != null) {
            Set<String> keys = decorationBlocks.getKeys(false);
            for (String key : keys) {
                ConfigurationSection block = decorationBlocks.getConfigurationSection(key);
                if (block != null) {
                    Material type = Material.valueOf(block.getString("type"));
                    String name = block.getString("name");
                    List<String> lore = block.getStringList("lore");
                    List<String> fixcolorlore = new ArrayList<>();
                    for (String text : lore) {
                        fixcolorlore.add(ColorFixer.fixColor(text));
                    }
                    List<Integer> slots = block.getIntegerList("slots");
                    ItemStack item = new ItemStack(type);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName(ColorFixer.fixColor(name));
                        meta.setLore(fixcolorlore);
                        item.setItemMeta(meta);
                    }
                    for (int slot : slots) {
                        gui.setItem(slot, item);
                    }
                }
            }
        }

        ConfigurationSection missions = ConfigManager.getConfig().getConfigurationSection("missions");
        if (missions != null) {
            for (String key : missions.getKeys(false)) {
                ConfigurationSection mission = missions.getConfigurationSection(key);
                if (mission != null) {
                    String itemName = ColorFixer.fixColor(mission.getString("name"));
                    Material itemType = Material.valueOf(mission.getString("item"));
                    List<String> itemLore = mission.getStringList("lore");
                    List<String> fixedLore = new ArrayList<>();
                    for (String line : itemLore) {
                        fixedLore.add(ColorFixer.fixColor(line));
                    }
                    ItemStack missionItem = new ItemStack(itemType);
                    ItemMeta missionMeta = missionItem.getItemMeta();

                    int statistic = 0;
                    if (player != null && questname != null) {
                        statistic = player.getStatistic(questname) - before;
                        if (statistic < 0) {
                            statistic = 0;
                        }
                    }

                    if (missionMeta != null) {
                        int data = ConfigManager.getConfig().getInt("missions." + queststype + ".value");
                        missionMeta.setDisplayName(itemName.replace("[STATS]", String.valueOf(statistic)).replace("[VALUE]", String.valueOf(data)));

                        User user = userManager.userHashMap.get(player.getUniqueId());
                        String replaceValue;

                        if (user.isComplete() || DataManager.getConfig().getString("players." + player.getUniqueId() + ".complete").contains("true")) {
                            replaceValue = ColorFixer.fixColor(ConfigManager.getConfig().getString("messages.complete"));
                        } else {
                            replaceValue = ColorFixer.fixColor(ConfigManager.getConfig().getString("messages.nocomplete"));
                        }

                        List<String> newLore = new ArrayList<>();
                        for (String line : fixedLore) {
                            newLore.add(line.replace("[COMPLETE]", replaceValue != null ? replaceValue.toString() : null));
                        }

                        missionMeta.setLore(newLore);
                        missionItem.setItemMeta(missionMeta);
                    }

                    int slot = ConfigManager.getConfig().getInt("gui.mainitem.slot", -1);
                    if (slot != -1) {
                        gui.setItem(slot, missionItem);
                    }
                }
            }
        }
        return gui;
    }
}