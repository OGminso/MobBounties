package net.minso.mobbounties;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener {

    public void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 36, "Bouties");

        ItemStack bounty1 = new ItemBuilder(Material.FILLED_MAP)
                .setName(ChatColor.GREEN + "Bounty #1")
                .setLore("Lore 1", "lOre 2")
                .build();

        inventory.setItem(10, bounty1);

        ItemStack bounty2 = new ItemBuilder(Material.FILLED_MAP)
                .setName(ChatColor.GREEN + "Bounty #2")
                .setLore("Lore 1", "lOre 2")
                .build();

        inventory.setItem(11, bounty2);

        ItemStack bounty3 = new ItemBuilder(Material.FILLED_MAP)
                .setName(ChatColor.GREEN + "Bounty #3")
                .setLore("Lore 1", "lOre 2")
                .build();

        inventory.setItem(12, bounty3);

        player.openInventory( inventory);
    }

}
