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
    private FileManager config, kits;
    
    private HashMap<String, Long> delayedPlayers;
    private int delay;
    private boolean overwrite;
	
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
        
        delayedPlayers = new HashMap<String, Long>();
        delay = config.getInt("options.delay");
        overwrite = config.getBoolean("options.overwrite");
        
		getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getCommand("kits").setExecutor(new KitsCommandExecutor(this));
        getCommand("kit").setExecutor(new KitCommandExecutor(this));
	}
	
	public void reload(){
		reloadConfig();
		config.loadFile();
		getKitsConfig().loadFile();
		
        delay = config.getInt("options.delay");
        overwrite = config.getBoolean("options.overwrite");
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
	
	public int getDelay(int multiplier){
	    return delay*multiplier;
	}
	
	public boolean getOverwrite(){
	    return overwrite;
	}
	
	public int getRemainingTime(Player player){
	    return (int) (getDelay(1) - ((System.currentTimeMillis() - getPlayerDelay(player))/1000));
	}

    public FileManager getKitsConfig() {
        return kits;
    }

    public void setKits(FileManager kits) {
        this.kits = kits;
    }
}
