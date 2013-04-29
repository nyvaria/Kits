package com.dragonphase.Kits;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dragonphase.Kits.Commands.KitCommandExecutor;
import com.dragonphase.Kits.Commands.KitsCommandExecutor;
import com.dragonphase.Kits.Listeners.EventListener;
import com.dragonphase.Kits.Util.FileManager;
import com.dragonphase.Kits.Util.Message;

public class Kits extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Kits plugin;
    public static FileManager configurationFile;
    public static FileManager kitsFile;
    
    private static HashMap<Player, Long> delayedPlayers;
    private static int delay;
	
	@Override
	public void onDisable(){
		logger.info(Message.message("Version " + getPluginVersion() + " disabled."));
	}

	@Override
	public void onEnable(){
		Message.setParent(this);
		logger.info(Message.message("Version " + getPluginVersion() + " enabled."));
		
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		
        configurationFile = new FileManager(this, "config.yml");
        kitsFile = new FileManager(this, "kits.yml");
        
        delayedPlayers = new HashMap<Player, Long>();
        delay = Kits.configurationFile.getInt("options.delay");
        
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("kits").setExecutor(new KitsCommandExecutor(this));
        getCommand("kit").setExecutor(new KitCommandExecutor(this));
	}
	
	public void reload(){
		reloadConfig();
		configurationFile.loadFile();
		kitsFile.loadFile();
		
        delay = Kits.configurationFile.getInt("options.delay");
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
    
    public static void addDelayedPlayer(Player player){
        delayedPlayers.put(player, System.currentTimeMillis());
    }
    
    public static void removeDelayedPlayer(Player player){
        delayedPlayers.remove(player);
    }
	
	public static boolean playerDelayed(Player player){
	    return delayedPlayers.containsKey(player);
	}
	
	public static Long getPlayerDelay(Player player){
	    return delayedPlayers.get(player);
	}
	
	public static int getDelay(int multiplier){
	    return delay*multiplier;
	}
	
	public static int getRemainingTime(Player player){
	    return (int) (getDelay(1) - ((System.currentTimeMillis() - getPlayerDelay(player))/1000));
	}
}
