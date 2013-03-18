package org.Dragonphase.Battlegear.Util;

import java.util.Map;

import org.Dragonphase.Battlegear.Battlegear;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Details{
	
	public static String capitalize(String string){
		String[] NameList = string.toLowerCase().replace("_", " ").replace("-", " ").split(" ");
		String Name = "";
		
		for (String Word : NameList){
			Name += Word.substring(0, 1).toUpperCase() + Word.substring(1).toLowerCase() + " ";
		}
		
		if (Name.endsWith(" ")) Name = Name.substring(0, Name.length()-1);
		return Name;
	}
	
	public static Map<String, Object> getItemMeta(ItemStack item){
		return item.getItemMeta().serialize();
	}
	
	public static boolean isInWorld(LivingEntity entity){
		boolean inWorld = false;
		for (String world : Battlegear.mainConfig.getStringList("options.worlds")){
			if (world.equalsIgnoreCase(entity.getWorld().getName())){
				inWorld = true;
			}
		}
		return inWorld;
	}
	
	public static boolean isInWorld(Block block){
		boolean inWorld = false;
		for (String world : Battlegear.mainConfig.getStringList("options.worlds")){
			if (world.equalsIgnoreCase(block.getWorld().getName())) inWorld = true;
		}
		return inWorld;
	}
}
