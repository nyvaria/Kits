package org.Dragonphase.Kits.Listeners;

import java.util.logging.Logger;
import org.Dragonphase.Kits.Kits;
import org.Dragonphase.Kits.Util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class EventListener implements Listener
{
    public final Logger logger = Logger.getLogger("Minecraft");
    public static Kits plugin;

    public EventListener(Kits instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setMetadata("editingKit", new FixedMetadataValue(plugin, false));
        player.setMetadata("creatingKit", new FixedMetadataValue(plugin, false));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player)event.getPlayer();
        try
        {
            if (((MetadataValue)player.getMetadata("editingKit").get(0)).asBoolean()) {
                String kit = inventory.getTitle();
                
                Kits.kitsFile.set(kit, inventory.getContents(), false);
                
                player.sendMessage(Message.info("Kit " + kit + " has been updated."));
                player.setMetadata("editingKit", new FixedMetadataValue(plugin, false));
                
                Kits.kitsFile.loadFile();
            }
            if (((MetadataValue)player.getMetadata("creatingKit").get(0)).asBoolean()) {
                String kit = inventory.getTitle();
                
                Kits.kitsFile.set(kit, inventory.getContents(), false);
                
                player.sendMessage(Message.info("Kit " + kit + " has been created."));
                player.setMetadata("creatingKit", new FixedMetadataValue(plugin, false));
                
                Kits.kitsFile.loadFile();
            }
        }
        catch (Exception ex)
        {
            logger.info(ex.getLocalizedMessage());
        }
    }
}