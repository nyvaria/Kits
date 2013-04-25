package org.Dragonphase.Kits.Commands;

import java.util.ArrayList;

import org.Dragonphase.Kits.Kits;
import org.Dragonphase.Kits.Util.Kit;
import org.Dragonphase.Kits.Util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class KitCommandExecutor implements CommandExecutor{
	
    public static Kits plugin;
    
	public KitCommandExecutor(Kits instance) {
	    plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
	    if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
	    
	    if (args.length == 0){
	        player.sendMessage(Message.info("/kit <kitname|create|edit|remove>"));
	    }else{
	        String arg = args[0].toLowerCase();
	        
	        if (args.length == 1){
                if (arg.equalsIgnoreCase("create")){
                    player.sendMessage(Message.info("/kit create <kitname>"));
                }else if (arg.equalsIgnoreCase("edit")){
                    player.sendMessage(Message.info("/kit edit <kitname>"));
                }else if (arg.equalsIgnoreCase("remove")){
                    player.sendMessage(Message.info("/kit remove <kitname>"));
                }else{
                    if (player.hasPermission("kits.spawn." + arg)){
                        if (Kit.exists(arg)){
                            ArrayList<ItemStack> itemList = Kit.getKit(arg);
                            for (int i = 0; i < itemList.size(); i ++){
                                player.getInventory().setItem(i, itemList.get(i));
                            }
                            player.sendMessage(Message.info("Kit " + arg + " spawned."));
                        }else{
                            player.sendMessage(Message.warning("Kit " + arg + " does not exist."));
                        }
                    }else{
                        player.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }
	        }else if (args.length > 1){
	            if (player.hasPermission("kits.admin")){
	                if (arg.equalsIgnoreCase("create")){
                        if (!Kit.exists(args[1])){
                            Kit.create(plugin, player, args[1]);
                            player.setMetadata("editingKit", new FixedMetadataValue(plugin, false));
                            player.setMetadata("creatingKit", new FixedMetadataValue(plugin, true));
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " already exists."));
                        }
	                }else if (arg.equalsIgnoreCase("edit")){
	                    if (Kit.exists(args[1])){
	                        Kit.edit(plugin, player, args[1]);
	                        player.setMetadata("editingKit", new FixedMetadataValue(plugin, true));
	                        player.setMetadata("creatingKit", new FixedMetadataValue(plugin, false));
	                    }else{
	                        player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
	                    }
	                }else if (arg.equalsIgnoreCase("remove")){
                        if (Kit.exists(args[1])){
                            Kits.configurationFile.set(args[1], null, false);
                            player.sendMessage(Message.info("Kit " + args[1] + " has been removed."));
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
                        }
	                }
	            }else{
                    sender.sendMessage(Message.warning("Incorrect Permissions."));
                }
	        }
	    }
        
		return false;
	}
}
