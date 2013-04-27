package org.Dragonphase.Kits;

import java.util.logging.Logger;

import org.Dragonphase.Kits.Commands.KitCommandExecutor;
import org.Dragonphase.Kits.Commands.KitsCommandExecutor;
import org.Dragonphase.Kits.Listeners.EventListener;
import org.Dragonphase.Kits.Util.FileManager;
import org.Dragonphase.Kits.Util.Message;
import org.bukkit.plugin.java.JavaPlugin;

public class Kits extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Kits plugin;
    public static FileManager configurationFile;
    public static FileManager kitsFile;
	
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
        kitsFile = new FileManager(this, "kits.yml");
        
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("kits").setExecutor(new KitsCommandExecutor(this));
        getCommand("kit").setExecutor(new KitCommandExecutor(this));
	}
	
	public void reload(){
		reloadConfig();
		configurationFile.loadFile();
		kitsFile.loadFile();
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
