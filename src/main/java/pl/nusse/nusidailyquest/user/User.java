package pl.nusse.nusidailyquest.user;

import org.bukkit.Statistic;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

import static org.bukkit.Statistic.DEATHS;

public class User {
    private UUID uuid;
    private Statistic questname;
    private int queststype;
    private int before;
    private boolean complete;
    private long currentTime;

    public User(UUID uuid, String questname, int queststype, int before, boolean b, int i) {
        this.uuid = uuid;

        this.questname = null;
        this.queststype = 0;
        this.before = 0;
        this.complete = false;
        this.currentTime = 0;
    }

    public User(UUID uuid, boolean complete) {
        this.uuid = uuid;
        this.questname = DEATHS;
        this.queststype = 1;
        this.before = 0;
        this.complete = complete;
        this.currentTime = 1;
    }

    public User(UUID uuid, long currentTime) {
        this.uuid = uuid;
        this.questname = DEATHS;
        this.queststype = 1;
        this.before = 0;
        this.complete = true;
        this.currentTime = currentTime;
    }

    public User(UUID uuid, Statistic questname, int queststype, int before, Inventory inventory, boolean complete, long currentTime) {
        this.uuid = uuid;
        this.questname = questname;
        this.queststype = queststype;
        this.before = before;
        this.complete = complete;
        this.currentTime = currentTime;
    }

    public Statistic getQuestname() {
        return questname;
    }
    public int getQueststype() {
        return queststype;
    }
    public int getBefore() {
        return before;
    }
    public boolean isComplete() {
        return complete;
    }

    public long getCurrentTime() {
        return currentTime;
    }
}

    //    public User(UUID uuid) {
//        this.uuid = uuid;
//        this.inventory = Bukkit.createInventory((InventoryHolder) this, ConfigManager.getConfig().getInt("gui.size"),
//                ConfigManager.getConfig().getString("gui.title"));
//
//        this.questname = null;
//        this.queststype = 0;
//        this.before = 0;
//    }
