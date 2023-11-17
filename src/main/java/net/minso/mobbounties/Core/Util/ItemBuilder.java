package net.minso.mobbounties.Core.Util;

import net.minso.mobbounties.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemBuilder {

    private Main main = Main.getInstance();

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearFlags() {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getItemFlags().clear();
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomModelData(int i) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(i);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

}
