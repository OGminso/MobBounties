package net.minso.mobbounties.Core.Quests;

import net.minso.mobbounties.Core.Player.PlayerData;
import net.minso.mobbounties.Core.Player.Slot;
import net.minso.mobbounties.Core.Player.Slots;
import net.minso.mobbounties.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.text.DecimalFormat;

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
                    player.sendMessage(playerSlot.getProgress() + "/" + quest.getCount());
                    DecimalFormat df = new DecimalFormat("#.##");
                    String progressPercentage = df.format(((double) playerSlot.getProgress() / quest.getCount()) * 100);
                    player.sendMessage(String.valueOf(progressPercentage + "%"));
                } else {
                    player.sendMessage("Done");
                }



                // Check if the quest is completed
                if (playerSlot.getProgress() >= quest.getCount()) {
                    // Quest completed, provide rewards
                    //completeQuest(player, playerSlot);
                }

                return; // Exit the loop since we found the matching quest
            }
        }

    }

}
