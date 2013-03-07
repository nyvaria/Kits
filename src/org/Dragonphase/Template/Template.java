package org.Dragonphase.Template;

import java.util.logging.Logger;

import org.Dragonphase.Template.Commands.TemplateCommandExecutor;
import org.Dragonphase.Template.Listeners.EventListener;
import org.Dragonphase.Template.Util.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Template extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Template plugin;
	public FileManager configurationFile;
	
	@Override
	public void onDisable(){
		PluginDescriptionFile PDF = this.getDescription();
		logger.info(PDF.getName() + " disabled.");
	}

	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		saveDefaultConfig();
		configurationFile = new FileManager(this, "config.yml");
		
		getCommand("template").setExecutor(new TemplateCommandExecutor(this));
		
		logger.info(pluginMessageFormat("Version " + getPluginVersion() + " enabled."));
	}
	
	public void reload(){
		reloadConfig();
		configurationFile.loadFile("config.yml");
	}
	
	public String getPluginDetails(){
		return getPluginName() + " " + getPluginVersion();
	}
	
	public String getPluginName(){
		return getDescription().getName();
	}
	
	public String getPluginVersion(){
		return getDescription().getVersion();
	}
	
	public String pluginMessageFormat(String message){
		return ChatColor.DARK_GRAY + getPluginName() + ": " + ChatColor.GRAY + message;
	}
}
