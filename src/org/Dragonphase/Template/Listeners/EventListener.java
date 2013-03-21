package org.Dragonphase.Template.Listeners;

import java.util.logging.Logger;

import org.Dragonphase.Template.Template;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListener implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Template plugin;

	public EventListener(Template instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		
	}
}
