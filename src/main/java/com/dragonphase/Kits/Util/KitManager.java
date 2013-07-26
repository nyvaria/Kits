package com.dragonphase.Kits.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.dragonphase.Kits.Kits;

public class KitManager {
    
    private Kits plugin;
    
    public KitManager(Kits instance){
        plugin = instance;
    }
    
    public boolean exists(String kitName){
        boolean exists = false;
        for (String key : plugin.getKitsConfig().getKeys(false)){
            if (key.equals(kitName)) exists = true;
        }
        return exists;
    }
    
    public ItemStack[] getKit(String kitName){
        return plugin.getKitsConfig().getInventory(kitName + ".kit");
    }
    
    public int getDelay(String kitName){
    	return plugin.getKitsConfig().getInt(kitName + ".delay");
    }
    
    public void setDelay(String kitName, int delay){
    	plugin.getKitsConfig().set(kitName + ".delay", delay, false);
    }
    
    public boolean getOverwrite(String kitName){
    	return plugin.getKitsConfig().getBoolean(kitName + ".overwrite");
    }
    
    public void setOverwrite(String kitName, boolean overwrite){
    	plugin.getKitsConfig().set(kitName + ".overwrite", overwrite, false);
    }

    public void create(Player player, String kitName, int bars) {
        Inventory inventory = plugin.getServer().createInventory(player, 9*(bars < 1 ? 1 : bars > 4 ? 4 : bars), kitName);
        player.openInventory(inventory);
    }
    
    public void edit(Player player, String kitName) {
        ItemStack[] itemList = getKit(kitName);
        int bars = (int) Math.ceil(itemList.length / 9);
        Inventory inventory = plugin.getServer().createInventory(player, 9*(bars < 1 ? 1 : bars > 4 ? 4 : bars), kitName);
        
        inventory.setContents(itemList);
        
        player.openInventory(inventory);
    }
    
    public int kitSize(String kit){
        return plugin.getKitsConfig().getConfigurationSection(kit).getKeys(false).size();
    }
}