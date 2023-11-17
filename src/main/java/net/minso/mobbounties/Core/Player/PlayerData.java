package net.minso.mobbounties.Core.Player;

import net.minso.mobbounties.Core.Quests.Quest;
import net.minso.mobbounties.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    Main main = Main.getInstance();

    private UUID uuid;

    private Slot slot1;
    private Slot slot2;
    private Slot slot3;

    private Slot slot4;
    private Slot slot5;
    private Slot slot6;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void setSlot(Slots numSlot, Slot slot) {
        switch (numSlot) {
            case SLOT1:
                this.slot1 = slot;
                break;
            case SLOT2:
                this.slot2 = slot;
                break;
            case SLOT3:
                this.slot3 = slot;
                break;
            case SLOT4:
                this.slot4 = slot;
                break;
            case SLOT5:
                this.slot5 = slot;
                break;
            case SLOT6:
                this.slot6 = slot;
                break;
        }
    }

    public Slot getSlot(Slots numSlot) {
        switch (numSlot) {
            case SLOT1:
                return slot1;
            case SLOT2:
                return slot2;
            case SLOT3:
                return slot3;
            case SLOT4:
                return slot4;
            case SLOT5:
                return slot5;
            case SLOT6:
                return slot6;
        }
        return null;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void savePlayerDataToFile() {
        File file = new File("plugins/" + main.getPluginName() + "/players/" + uuid + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (Slots slot : Slots.values()) {
            String slotPath = uuid.toString() + "." + slot.name();
            Slot currentSlot = getSlot(slot);

            cfg.set(slotPath + ".cooldown", currentSlot.getCooldown());

            if (currentSlot.getQuest() == null) {
                cfg.set(slotPath + ".quest", "[]");
            } else {
                Quest quest = currentSlot.getQuest();
                cfg.set(slotPath + ".quest.name", quest.getName());
                cfg.set(slotPath + ".quest.mobType", quest.getMobType().toString().toUpperCase());
                cfg.set(slotPath + ".quest.count", quest.getCount());
                cfg.set(slotPath + ".quest.rewardAmount", quest.getRewardAmount());
            }
            cfg.set(slotPath + ".progress", currentSlot.getProgress());
        }

        try {
            cfg.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerDataFromFile() {
        File file = new File("plugins/" + main.getPluginName() + "/players/" + uuid + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (Slots slot : Slots.values()) {
            String slotPath = uuid.toString() + "." + slot.name();

            long cooldown = cfg.getLong(slotPath + ".cooldown");
            Quest quest;
            if (cfg.getString(slotPath + ".quest").equals("[]")) {
                quest = null;
            } else {
                String questName = cfg.getString(slotPath + ".quest.name");
                String mobType = cfg.getString(slotPath + ".quest.mobType").toUpperCase();
                // Convert mobType string to EntityType
                EntityType entityType = EntityType.valueOf(mobType);
                int count = cfg.getInt(slotPath + ".quest.count");
                int rewardAmount = cfg.getInt(slotPath + ".quest.rewardAmount");

                quest = new Quest(questName, entityType, count, rewardAmount);
            }
            int progress = cfg.getInt(slotPath + ".progress");

            Slot currentSlot = new Slot(cooldown, quest, progress);
            setSlot(slot, currentSlot);
        }

    }

    public boolean playerDataFileExists() {
        File file = new File("plugins/" + main.getPluginName() +"/players/" + uuid + ".yml");
        return file.exists();
    }

    public void setDefaults() {
        for (Slots slot : Slots.values()) {
            Slot newSlot = new Slot(0, null, 0);
            setSlot(slot, newSlot);
        }
    }

    public void printPlayerData() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        console.sendMessage("Player Data for UUID: " + uuid);

        for (Slots slot : Slots.values()) {
            String slotPath = "uuid." + slot.name();
            Slot currentSlot = getSlot(slot);

            console.sendMessage("Slot: " + slot.name());
            console.sendMessage("Cooldown: " + currentSlot.getCooldown());

            if (currentSlot.getQuest() == null) {
                console.sendMessage("Quest: None");
            } else {
                Quest quest = currentSlot.getQuest();
                console.sendMessage("Quest Name: " + quest.getName());
                console.sendMessage("Quest Mob Type: " + quest.getMobType());
                console.sendMessage("Quest Count: " + quest.getCount());
                console.sendMessage("Quest Reward Amount: " + quest.getRewardAmount());
            }

            console.sendMessage("Progress: " + currentSlot.getProgress());
            console.sendMessage("");
        }
    }

}

