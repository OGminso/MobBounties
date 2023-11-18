package net.minso.mobbounties.Core.Player;

import net.minso.mobbounties.Core.Quests.Quest;
import net.minso.mobbounties.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager implements Listener {

    private Main main = Main.getInstance();

    private HashMap<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData playerData = new PlayerData(uuid);
        if (playerData.playerDataFileExists()) {
            playerData.loadPlayerDataFromFile();
            //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+ "PlayerJoinEvent: Loaded player "+  uuid.toString());
        } else {
            playerData.setDefaults();
        }

        playerDataHashMap.put(uuid, playerData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerData playerData = playerDataHashMap.get(uuid);
        playerData.savePlayerDataToFile();
        playerDataHashMap.remove(uuid);
        //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+  "Quit Event: player saved "+  uuid.toString());

    }

    /*
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = getPlayerData(player.getUniqueId());
        playerData.printPlayerData();
    }*/
    

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataHashMap.get(uuid);
    }

    public void onEnable() {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            UUID uuid = onlinePlayers.getUniqueId();
            PlayerData playerData = new PlayerData(uuid);

            if (playerData.playerDataFileExists()) {
                playerData.loadPlayerDataFromFile();
            } else {
                playerData.setDefaults();
            }
            playerDataHashMap.put(uuid, playerData);
        }
    }

    public void onDisable() {
        for (PlayerData playerData : playerDataHashMap.values()) {
            playerData.savePlayerDataToFile();
            playerDataHashMap.remove(playerData.getUUID());
        }

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (onlinePlayers.getOpenInventory().getTitle().equalsIgnoreCase(main.gui.inventoryTitle)) {
                onlinePlayers.closeInventory();
            }
        }

        playerDataHashMap.clear();

    }

    public void setRandomQuests(Player player) {
        PlayerData playerData = getPlayerData(player.getUniqueId());
        for (Slots slot : Slots.values()) {
            String permission = "mobbounties.slot." + slot.name().toLowerCase();
            if (slot.ordinal() <= 2 || player.hasPermission(permission)) {
                if (playerData.getSlot(slot).onCooldown()) return;
                if (playerData.getSlot(slot).getQuest() == null) {
                    Quest randomQuest = main.questManager.getRandomQuest();
                    Slot newSlot = new Slot(0, randomQuest, 0);
                    playerData.setSlot(slot, newSlot);
                }
            }
        }
    }

}
