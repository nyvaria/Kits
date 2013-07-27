package com.dragonphase.Kits;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dragonphase.Kits.Commands.KitCommandExecutor;
import com.dragonphase.Kits.Commands.KitsCommandExecutor;
import com.dragonphase.Kits.Listeners.EventListener;
import com.dragonphase.Kits.Util.FileManager;
import com.dragonphase.Kits.Util.KitManager;
import com.dragonphase.Kits.Util.Message;
import com.dragonphase.Kits.metrics.Metrics;

public class Kits extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
    private FileManager config, kits;
    private KitManager kitManager;
    
    private HashMap<String, Long> delayedPlayers;
	
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
		
        config = new FileManager(this, "config.yml");
        setKits(new FileManager(this, "kits.yml"));
        kitManager = new KitManager(this);
        
        delayedPlayers = new HashMap<String, Long>();
        
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("kits").setExecutor(new KitsCommandExecutor(this));
        getCommand("kit").setExecutor(new KitCommandExecutor(this));
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (Exception e) {
            // Failed to submit the stats :-(
        }
	}
	
	public void reload(){
		reloadConfig();
		config.loadFile();
		getKitsConfig().loadFile();
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
    
    public void addDelayedPlayer(Player player){
        delayedPlayers.put(player.getName(), System.currentTimeMillis());
    }
    
    public void removeDelayedPlayer(Player player){
        delayedPlayers.remove(player.getName());
    }
	
	public boolean playerDelayed(Player player){
	    return delayedPlayers.containsKey(player.getName());
	}
	
	public Long getPlayerDelay(Player player){
	    return delayedPlayers.get(player.getName());
	}
	
	public int getDelay(int delay, int multiplier){
	    return delay*multiplier;
	}
	
	public int getRemainingTime(int delay, Player player){
	    return (int) (getDelay(delay, 1) - ((System.currentTimeMillis() - getPlayerDelay(player))/1000));
	}

    public FileManager getKitsConfig() {
        return kits;
    }

    public void setKits(FileManager kits) {
        this.kits = kits;
    }
    
    public KitManager getKitManager(){
    	return kitManager;
    }
}
