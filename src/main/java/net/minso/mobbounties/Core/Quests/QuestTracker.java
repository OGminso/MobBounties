package net.minso.mobbounties.Core.Quests;

import net.minso.mobbounties.Core.Player.PlayerData;
import net.minso.mobbounties.Core.Player.Slot;
import net.minso.mobbounties.Core.Player.Slots;
import net.minso.mobbounties.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class QuestTracker implements Listener {

    Main main = Main.getInstance();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() instanceof Player) {
            Player player = entity.getKiller();
            handleMobKill(player, entity);
        }
    }

    private void handleMobKill(Player player, LivingEntity mob) {
        PlayerData playerData = main.playerManager.getPlayerData(player.getUniqueId());

        for (Slots slot : Slots.values()) {
            Slot playerSlot = playerData.getSlot(slot);
            Quest quest = playerSlot.getQuest();
            if (quest != null && quest.getMobType() == mob.getType()) {
                // The killed mob matches the quest's target mob
                if (playerSlot.getProgress() <= quest.getCount() -1) {
                    playerSlot.setProgress(playerSlot.getProgress() + 1);
                    if (playerSlot.getProgress() == quest.getCount()) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f); //allows click on complete quest
                        player.sendMessage(ChatColor.GREEN + "Bounty Completed! " + ChatColor.YELLOW + "Visit the QuestKeeper Seraphina at spawn.");
                    }
                }
            }
        }

    }

}
