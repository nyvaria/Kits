package com.dragonphase.Kits.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    public static void create(Player player, String kitName, int bars) {
        System.out.println(bars);
        System.out.println(bars*9);
        Inventory inventory = plugin.getServer().createInventory(player, 9*(bars < 1 ? 1 : bars > 4 ? 4 : bars), kitName);
        player.openInventory(inventory);
    }
    
    public static void edit(Player player, String kitName) {
        ItemStack[] itemList = getKit(kitName);
        int bars = (int) Math.ceil(itemList.length / 9);
        Inventory inventory = plugin.getServer().createInventory(player, 9*(bars < 1 ? 1 : bars > 4 ? 4 : bars), kitName);
        
        inventory.setContents(itemList);
        
        player.openInventory(inventory);
    }
    
    public static int kitSize(String kit){
        return plugin.getKitsConfig().getConfigurationSection(kit).getKeys(false).size();
    }
}