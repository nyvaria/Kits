package org.Dragonphase.Kits.Listeners;

import java.util.logging.Logger;

import org.Dragonphase.Kits.Kits;
import org.Dragonphase.Kits.Util.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EventListener implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Kits plugin;

	public EventListener(Kits instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player attacker = (Player)event.getDamager();
        
        if (Kit.getLeech(attacker.getItemInHand()) != 0){
            try{
                attacker.setHealth(attacker.getHealth() + event.getDamage());
            }catch (Exception ex){
                attacker.setHealth(attacker.getMaxHealth());
            }
        }
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
        event.getEntity().setMetadata("kit", new FixedMetadataValue(plugin, false));
	}
}
