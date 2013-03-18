package org.Dragonphase.Battlegear;

import java.util.logging.Logger;

import org.Dragonphase.Battlegear.Commands.GearCommandExecutor;
import org.Dragonphase.Battlegear.Listeners.EventListener;
import org.Dragonphase.Battlegear.Util.BitEntity;
import org.Dragonphase.Battlegear.Util.FileManager;
import org.Dragonphase.Battlegear.Util.Gear;
import org.Dragonphase.Battlegear.Util.Message;
import org.bukkit.plugin.java.JavaPlugin;

public class Battlegear extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Battlegear plugin;
	public static FileManager mainConfig;
	
	@Override
	public void onDisable(){
		logger.info(Message.message("Version " + getPluginVersion() + " disabled."));
	}

	@Override
	public void onEnable(){
		plugin = this;
		
		Message.setParent(this);
		logger.info(Message.message("Version " + getPluginVersion() + " enabled."));
		
		saveDefaultConfig();
		mainConfig = new FileManager(this.getDataFolder(), "config.yml");
		Gear.load();
		BitEntity.load();
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		getCommand("gear").setExecutor(new GearCommandExecutor(this));
	}
	
	public void reload(){
		reloadConfig();
		mainConfig.loadFile();
		Gear.reload();
		BitEntity.reload();
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
