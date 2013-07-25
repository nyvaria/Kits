package com.dragonphase.Kits.Listeners;

import com.dragonphase.Kits.Kits;
import com.dragonphase.Kits.Util.Kit;
import com.dragonphase.Kits.Util.Message;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
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

                plugin.getKitsConfig().set(kit, inventory.getContents(), false);

                player.sendMessage(Message.info("Kit " + kit + " has been updated."));
                player.setMetadata("editingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));

                plugin.getKitsConfig().loadFile();
            }
            if (((MetadataValue)player.getMetadata("creatingKit").get(0)).asBoolean()){
                String kit = inventory.getTitle();

                plugin.getKitsConfig().set(kit, inventory.getContents(), false);

                player.sendMessage(Message.info("Kit " + kit + " has been created."));
                player.setMetadata("creatingKit", new FixedMetadataValue(plugin, Boolean.valueOf(false)));

                plugin.getKitsConfig().loadFile();
            }
        }
        catch (Exception ex)
        {
            this.logger.info(ex.getLocalizedMessage());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST)){
            handleSignClick(event.getPlayer(), (Sign)event.getClickedBlock().getState());

            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
        }else if (event.getAction() == Action.RIGHT_CLICK_AIR && (event.getPlayer().getTargetBlock(null, 5).getType() == Material.WALL_SIGN || event.getPlayer().getTargetBlock(null, 5).getType() == Material.SIGN_POST)){
            handleSignClick(event.getPlayer(), (Sign)event.getPlayer().getTargetBlock(null,  5).getState());

            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
        }
    }
    
    @SuppressWarnings("deprecation")
    public void handleSignClick(Player player, Sign sign){
        for (int i = 0; i < 4; i++){
            if (sign.getLines()[i].equalsIgnoreCase("[kit]") && i != 3){
                try{
                    String arg = sign.getLines()[i+1];
                    if (player.hasPermission("kits.spawn." + arg)){
                        if (plugin.playerDelayed(player)){
                            if (plugin.getRemainingTime(player) < 1){
                                plugin.removeDelayedPlayer(player);
                            }else{
                                int remaining = plugin.getRemainingTime(player);
                                String seconds = remaining == 1 ? " second" : " seconds";
                                player.sendMessage(Message.warning("You must wait " + remaining + seconds + " before spawning another kit."));
                                return;
                            }
                        }
                        if (Kit.exists(arg)){
                            ItemStack[] itemList = Kit.getKit(arg);
                            if (plugin.getOverwrite()){
                                for (int x = 0; x < itemList.length; x ++){
                                    player.getInventory().setItem(x, itemList[x]);
                                }
                            }else{
                                try{
                                    for (int x = 0; x < itemList.length; x ++){
                                        player.getInventory().addItem(itemList[x]);
                                    }
                                }catch (Exception ex){
                                    if (player.getInventory().firstEmpty() == -1) player.sendMessage(Message.warning("You don't have enough space in your inventory to spawn the entire kit."));
                                }
                            }
                            player.updateInventory();
                            player.sendMessage(Message.info("Kit " + arg + " spawned."));

                            if ((!player.hasPermission("kits.bypassdelay")) && (plugin.getDelay(1) > 0)) plugin.addDelayedPlayer(player);
                        }
                        else{
                            player.sendMessage(Message.warning("Kit " + arg + " does not exist."));
                        }
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