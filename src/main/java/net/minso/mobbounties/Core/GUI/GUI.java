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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class GUI implements Listener {

    private Main main = Main.getInstance();
    private Economy eco = Main.getEconomy();

    public String inventoryTitle = ChatColor.LIGHT_PURPLE + "Bounties";

    private HashMap<UUID, Integer> cooldownList = new HashMap<UUID, Integer>();

    public void openGUI(Player player) {
        cancelCooldownUpdateTask(player);

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

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> updateCooldowns(player, inventory), 0, 20L);
        setCooldownUpdateTask(player, taskId);
    }

    private void updateCooldowns(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 10 || i == 11 || i == 12 || i == 14 || i == 15 || i == 16) {
                Slots slotType = getSlotTypeForInventorySlot(i);
                ItemStack slotItem = getSlotItem(player, slotType);
                inventory.setItem(i, slotItem);
            } else if (i == 22) {
                inventory.setItem(i, getCloseItem());
            }
        }

    }

    private ItemStack getSlotItem(Player player, Slots slots) {
        PlayerData playerData = main.playerManager.getPlayerData(player.getUniqueId());
        Slot slot = playerData.getSlot(slots);
        Quest quest = slot.getQuest();

        if (!slot.onCooldown() && quest == null) {
            main.playerManager.setRandomQuests(player);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String progressPercentage = null;
        if (quest != null) {
            progressPercentage = df.format(((double) slot.getProgress() / quest.getCount()) * 100);
        }

        if (slot.onCooldown()) {
            return new ItemBuilder(Material.CLOCK)
                    .setName(ChatColor.RED + "Cooldown")
                    .setLore(ChatColor.GRAY + "Not available on cooldown",
                           ChatColor.GRAY + "Time remaining: " + ChatColor.RED + slot.getFormattedCooldown())
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

    private ItemStack getCloseItem() {
        return new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "Close").build();
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

            if (itemStack.equals(getCloseItem())) {
                player.closeInventory();
                cancelCooldownUpdateTask(player);
                return;
            }

            int itemModel = getItemModelInt(itemStack);
            if (itemModel == -1) return;

            //player.sendMessage(Slots.values()[itemModel].name());//

            PlayerData playerData = main.playerManager.getPlayerData(player.getUniqueId());
            Slot slot = playerData.getSlot(Slots.values()[itemModel]);
            if (slot.getQuest() == null) return;
            Quest quest = slot.getQuest();

            if (slot.getProgress() == quest.getCount()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2); //allows click on complete quest
                player.sendMessage(ChatColor.GRAY + "You have recieved"+ ChatColor.GREEN +" $" + quest.getRewardAmount() + ChatColor.GRAY +" for completing a bounty!");
                eco.depositPlayer(player, quest.getRewardAmount());
                slot.setCooldown();
                slot.removeQuest();
                slot.setProgress(0);
                player.closeInventory();
                openGUI(player);
            }

        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(inventoryTitle)) {
            Player player = (Player) event.getPlayer();
            cancelCooldownUpdateTask(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        cancelCooldownUpdateTask(event.getPlayer());
    }

    private int getItemModelInt(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.hasCustomModelData()) {
            return meta.getCustomModelData();
        } else {
            return -1;
        }
    }

    public void setCooldownUpdateTask(Player player, int taskId) {
        cooldownList.put(player.getUniqueId(), taskId);
    }

    public void cancelCooldownUpdateTask(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldownList.containsKey(playerId)) {
            int taskId = cooldownList.get(playerId);
            Bukkit.getScheduler().cancelTask(taskId);
            cooldownList.remove(playerId);
        }
    }


}
