package org.Dragonphase.Kits.Util;

import java.util.ArrayList;

import org.Dragonphase.Kits.Kits;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Kit {
    
    public static boolean exists(String kitName){
        boolean exists = false;
        for (String key : Kits.kitsFile.getKeys(false)){
            if (key.equals(kitName)) exists = true;
        }
        return exists;
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<ItemStack> getKit(String kitName){
        return (ArrayList<ItemStack>) Kits.kitsFile.getList(kitName);
    }

    public static void create(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        player.openInventory(inventory);
    }
    
    public static void edit(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        
        for (int i = 0; i < 9; i++) {
            try{
                inventory.setItem(i, getKit(kitName).get(i));
            }catch (Exception ex){
                continue;
            }
        }
        
        player.openInventory(inventory);
    }
    
    public static int kitSize(String kit){
        return Kits.kitsFile.getConfigurationSection(kit).getKeys(false).size();
    }
}