package org.Dragonphase.Template;

import java.util.logging.Logger;

import org.Dragonphase.Template.Util.FileManager;
import org.Dragonphase.Template.Util.Message;
import org.Dragonphase.Template.Commands.TemplateCommandExecutor;
import org.Dragonphase.Template.Listeners.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Template extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Template plugin;
	public FileManager configurationFile;
	
	@Override
	public void onDisable(){
		logger.info(Message.message("Version " + getPluginVersion() + " disabled."));
	}

	@Override
	public void onEnable(){
		Message.setParent(this);
		logger.info(Message.message("Version " + getPluginVersion() + " enabled."));
		
		saveDefaultConfig();
		configurationFile = new FileManager(this, "config.yml");
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		getCommand("template").setExecutor(new TemplateCommandExecutor(this));
	}
	
	public void reload(){
		reloadConfig();
		configurationFile.loadFile();
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
}
