package com.dragonphase.Kits.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.dragonphase.Kits.Kits;

public class Kit {
    
    public static boolean exists(String kitName){
        boolean exists = false;
        for (String key : Kits.kitsFile.getKeys(false)){
            if (key.equals(kitName)) exists = true;
        }
        return exists;
    }
    
    public static ItemStack[] getKit(String kitName){
        return Kits.kitsFile.getInventory(kitName);
    }

    public static void create(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        player.openInventory(inventory);
    }
    
    public static void edit(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        
        inventory.setContents(getKit(kitName));
        
        player.openInventory(inventory);
    }
    
    public static int kitSize(String kit){
        return Kits.kitsFile.getConfigurationSection(kit).getKeys(false).size();
    }
}