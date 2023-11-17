package net.minso.mobbounties.Core.GUI;

import net.milkbowl.vault.economy.Economy;
import net.minso.mobbounties.Core.Player.PlayerData;
import net.minso.mobbounties.Core.Player.Slot;
import net.minso.mobbounties.Core.Player.Slots;
import net.minso.mobbounties.Core.Quests.Quest;
import net.minso.mobbounties.Core.Quests.QuestTracker;
import net.minso.mobbounties.Core.Util.ItemBuilder;
import net.minso.mobbounties.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public class GUI implements Listener {

    Main main = Main.getInstance();
    Economy eco = Main.getEconomy();

    String inventoryTitle = ChatColor.LIGHT_PURPLE + "Bounties";

    public void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, inventoryTitle);
        main.playerManager.setRandomQuests(player);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 10 || i == 11 || i == 12 || i == 14 || i == 15 || i == 16) {
                Slots slotType = getSlotTypeForInventorySlot(i);
                ItemStack slotItem = getSlotItem(player, slotType);
                inventory.setItem(i, slotItem);
            } else {
                inventory.setItem(i, getFillerItem());
            }
        }
        player.openInventory(inventory);

    }

    private ItemStack getSlotItem(Player player, Slots slots) {
        PlayerData playerData = main.playerManager.getPlayerData(player.getUniqueId());
        Slot slot = playerData.getSlot(slots);
        Quest quest = slot.getQuest();

        DecimalFormat df = new DecimalFormat("#.##");
        String progressPercentage = df.format(((double) slot.getProgress() / quest.getCount()) * 100);

        if (slot.onCooldown()) {
            return new ItemBuilder(Material.REDSTONE_BLOCK)
                    .setName("Cooldown")
                    .setLore("Not available on cooldown", "Time remaining: " + slot.getFormattedCooldown())
                    .setCustomModelData(slots.ordinal())
                    .build();
        } else if (quest != null && slot.getProgress() == quest.getCount()) {
            return new ItemBuilder(Material.ENCHANTED_BOOK)
                    .setName(ChatColor.GOLD + quest.getName() + ChatColor.GRAY +" (" + ChatColor.GREEN + "Completed" + ChatColor.GRAY + ")")
                    .setLore(ChatColor.GRAY + "Reward: " + ChatColor.GREEN + "$" + quest.getRewardAmount(),
                            ChatColor.GRAY + "Progress: " + ChatColor.WHITE + slot.getProgress() + ChatColor.GRAY + "/" + ChatColor.WHITE + quest.getCount() + " " + ChatColor.GRAY + "(" + progressPercentage + "%)",
                            " ", ChatColor.YELLOW + "Click to claim!")
                    .setCustomModelData(slots.ordinal())
                    .build();
        } else if (quest != null && (slots.ordinal() <= 2 || player.hasPermission("mobbounties.slot." + slots.name().toLowerCase()))) {
            return new ItemBuilder(Material.FILLED_MAP)
                    .setName(ChatColor.GOLD + quest.getName())
                    .setLore(/*ChatColor.GRAY +"Kill " + quest.getCount() + "x " + quest.getMobType().getName().toString(),*/
                            ChatColor.GRAY + "Reward: " + ChatColor.GREEN + "$" + quest.getRewardAmount(),
                            ChatColor.GRAY + "Progress: " + ChatColor.WHITE + slot.getProgress() + ChatColor.GRAY + "/" + ChatColor.WHITE + quest.getCount() + " " + ChatColor.GRAY + "(" + progressPercentage + "%)")
                    .setCustomModelData(slots.ordinal())
                    .build();
        } else {
            return new ItemBuilder(Material.BEDROCK)
                    .setName(ChatColor.RED + "Locked Slot")
                    .setLore(ChatColor.GRAY + "Not available to non-premium members")
                    .setCustomModelData(slots.ordinal())
                    .build();
        }
    }


    private ItemStack getFillerItem() {
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
    }

    private Slots getSlotTypeForInventorySlot(int inventorySlot) {
        switch (inventorySlot) {
            case 10:
                return Slots.SLOT1;
            case 11:
                return Slots.SLOT2;
            case 12:
                return Slots.SLOT3;
            case 14:
                return Slots.SLOT4;
            case 15:
                return Slots.SLOT5;
            case 16:
                return Slots.SLOT6;
            default:
                return Slots.SLOT1;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(inventoryTitle)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();

            int itemModel = getItemModelInt(itemStack);
            if (itemModel == -1) return;

            PlayerData playerData = main.playerManager.getPlayerData(player.getUniqueId());
            Slot slot = playerData.getSlot(Slots.values()[itemModel]);
            Quest quest = slot.getQuest();

            if (slot.getProgress() == quest.getCount()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2); //allows click on complete quest
                player.sendMessage("You have recieved $" + quest.getRewardAmount() + " for complete a bounty!");
                eco.depositPlayer(player, quest.getRewardAmount());
                slot.setCooldown(); //doesnt work?????? tf
                slot.removeQuest();
                player.closeInventory();
                openGUI(player);
            }




            //player.sendMessage(slots.name());

        }
    }

    private int getItemModelInt(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.hasCustomModelData()) {
            return meta.getCustomModelData();
        } else {
            return -1;
        }
    }


}
