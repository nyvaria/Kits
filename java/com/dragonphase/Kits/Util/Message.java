package com.dragonphase.Kits.Util;

import org.bukkit.ChatColor;

import com.dragonphase.Kits.Kits;

public class Message {

	public static Kits plugin;
	
	public static void setParent(Kits instance){
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
