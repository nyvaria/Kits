package com.dragonphase.Kits.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.dragonphase.Kits.Kits;

public class Kit {
    
    private static Kits plugin;
    
    public static void setParent(Kits instance){
        plugin = instance;
    }
    
    public static boolean exists(String kitName){
        boolean exists = false;
        for (String key : plugin.getKitsConfig().getKeys(false)){
            if (key.equals(kitName)) exists = true;
        }
        return exists;
    }
    
    public static ItemStack[] getKit(String kitName){
        return plugin.getKitsConfig().getInventory(kitName);
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
        return plugin.getKitsConfig().getConfigurationSection(kit).getKeys(false).size();
    }
}