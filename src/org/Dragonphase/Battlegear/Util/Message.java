package org.Dragonphase.Battlegear.Util;

import org.Dragonphase.Battlegear.Battlegear;
import org.bukkit.ChatColor;

public class Message {

	public static Battlegear plugin;
	
	public static void setParent(Battlegear instance){
		plugin = instance;
	}
	
	public static String message(String message){
		return ChatColor.DARK_GRAY + plugin.getPluginName() + ": " + ChatColor.GRAY + message;
	}
	
	public static String warning(String message){
		return ChatColor.DARK_GRAY + plugin.getPluginName() + ": " + ChatColor.RED + message;
	}
	
	public static String info(String message){
		return ChatColor.DARK_GRAY + plugin.getPluginName() + ": " + ChatColor.DARK_AQUA + message;
	}
}
