package org.Dragonphase.Template.Util;

import org.Dragonphase.Template.Template;
import org.bukkit.ChatColor;

public class Message {

	public static Template plugin;
	
	public static void setParent(Template instance){
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
