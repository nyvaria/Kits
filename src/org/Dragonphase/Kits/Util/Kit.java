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
        for (String key : Kits.configurationFile.getKeys(false)){
            if (key.equals(kitName)) exists = true;
        }
        return exists;
    }
	
	public static ArrayList<ItemStack> getKit(String kitName){
	    ArrayList<ItemStack> kit = new ArrayList<ItemStack>();
	    for (int i = 0; i < 10; i ++){
	        kit.add(Kits.configurationFile.getItemStack(kitName + "." + i));
	    }
	    return kit;
	}

    public static void create(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        player.openInventory(inventory);
    }
    
    public static void edit(Plugin plugin, Player player, String kitName) {
        Inventory inventory = plugin.getServer().createInventory(player, 9, kitName);
        
        for (int i = 0; i < 10; i++) {
            try{
                inventory.setItem(i, Kits.configurationFile.getItemStack(kitName + "." + i));
            }catch (Exception ex){
                continue;
            }
        }
        
        player.openInventory(inventory);
    }
    
    public static int kitSize(String kit){
        return Kits.configurationFile.getConfigurationSection(kit).getKeys(false).size();
    }
}