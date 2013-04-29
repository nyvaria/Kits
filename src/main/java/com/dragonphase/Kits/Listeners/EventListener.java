package com.dragonphase.Kits.Listeners;

import com.dragonphase.Kits.Kits;
import com.dragonphase.Kits.Util.Kit;
import com.dragonphase.Kits.Util.Message;
import java.util.logging.Logger;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.setMetadata("editingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));
        player.setMetadata("creatingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player)event.getPlayer();
        try
        {
            if (((MetadataValue)player.getMetadata("editingKit").get(0)).asBoolean()){
                String kit = inventory.getTitle();

                Kits.kitsFile.set(kit, inventory.getContents(), false);

                player.sendMessage(Message.info("Kit " + kit + " has been updated."));
                player.setMetadata("editingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));

                Kits.kitsFile.loadFile();
            }
            if (((MetadataValue)player.getMetadata("creatingKit").get(0)).asBoolean()){
                String kit = inventory.getTitle();

                Kits.kitsFile.set(kit, inventory.getContents(), false);

                player.sendMessage(Message.info("Kit " + kit + " has been created."));
                player.setMetadata("creatingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));

                Kits.kitsFile.loadFile();
            }
        }
        catch (Exception ex)
        {
            this.logger.info(ex.getLocalizedMessage());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if ((event.getAction() != Action.RIGHT_CLICK_BLOCK) && (event.getAction() != Action.RIGHT_CLICK_AIR)) return;

        if ((event.getClickedBlock().getState() instanceof Sign)){
            for (int i = 0; i < 4; i++){
                if (((Sign)event.getClickedBlock().getState()).getLines()[i].equalsIgnoreCase("[kit]") && i != 3){
                    try{
                        Player player = event.getPlayer();
                        String arg = ((Sign)event.getClickedBlock().getState()).getLines()[i+1];
                        if (player.hasPermission("kits.spawn." + arg)){
                            if (Kits.playerDelayed(player)){
                                if (Kits.getRemainingTime(player) < 1){
                                    Kits.removeDelayedPlayer(player);
                                }else{
                                    int remaining = Kits.getRemainingTime(player);
                                    String seconds = remaining == 1 ? " second" : " seconds";
                                    player.sendMessage(Message.warning("You must wait " + remaining + seconds + " before spawning another kit."));
                                    return;
                                }
                            }
                            if (Kit.exists(arg)){
                                ItemStack[] itemList = Kit.getKit(arg);
                                for (int x = 0; x < itemList.length; x++){
                                    player.getInventory().setItem(x, itemList[x]);
                                }
                                player.updateInventory();
                                player.sendMessage(Message.info("Kit " + arg + " spawned."));

                                if ((!player.hasPermission("kits.bypassdelay")) && (Kits.getDelay(1) > 0)) Kits.addDelayedPlayer(player); 
                            }
                            else{ player.sendMessage(Message.warning("Kit " + arg + " does not exist.")); }
                        }
                        else{
                            player.sendMessage(Message.warning("Incorrect Permissions."));
                        }
                    }catch (Exception ex){
                        continue;
                    }
                }
            }
        }
    }
}