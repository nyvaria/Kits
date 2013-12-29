package com.dragonphase.Kits.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.dragonphase.Kits.Kits;
import com.dragonphase.Kits.Util.Message;
//import com.dragonphase.Kits.metrics.Metrics;

public class KitCommandExecutor implements CommandExecutor{
	
    private Kits plugin;
    
	public KitCommandExecutor(Kits instance) {
	    plugin = instance;
	}

	@SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
	    if (!(sender instanceof Player)){
	        if (args.length == 2){
	            String arg = args[0];
	            
                Player receiver = getPlayer(args[1]);
                
                if (plugin.getKitManager().exists(arg)){
                    if (receiver != null){
                        ItemStack[] itemList = plugin.getKitManager().getKit(arg);
                        if (plugin.getKitManager().getOverwrite(arg)){
                            for (int i = 0; i < itemList.length; i ++){
                                receiver.getInventory().setItem(i, itemList[i]);
                            }
                        }else{
                            try{
                                for (int i = 0; i < itemList.length; i ++){
                                    receiver.getInventory().addItem(itemList[i]);
                                }
                            }catch (Exception ex){
                                if (receiver.getInventory().firstEmpty() == -1) sender.sendMessage(Message.warning(receiver.getName() + " didn't have enough space in their inventory to spawn the entire kit."));
                            }
                        }
                        receiver.updateInventory();
                        receiver.sendMessage(Message.info("Kit " + arg + " spawned by Console."));
                        sender.sendMessage(Message.info("Kit " + arg + " spawned for " + receiver.getName() + "."));
                        
                        //addKitMetric();
                    }else{
                        sender.sendMessage(Message.warning(args[1] + " is not online."));
                    }
                }else{
                    sender.sendMessage(Message.warning("Kit " + arg + " does not exist."));
                }
	        }
	        return false;
	    }
	    
        Player player = (Player) sender;
	    
	    if (args.length == 0){
	        player.sendMessage(Message.info("/kit <kitname|create|edit|remove>"));
	    }else{
	        String arg = args[0];
	        
	        if (args.length == 1){
                if (arg.equalsIgnoreCase("create")){
                    player.sendMessage(Message.info("/kit create <kitname> [bars] [overwrite] [delay]"));
                }else if (arg.equalsIgnoreCase("edit")){
                    player.sendMessage(Message.info("/kit edit <kitname>"));
                }else if (arg.equalsIgnoreCase("remove")){
                    player.sendMessage(Message.info("/kit remove <kitname>"));
                }else if (arg.equalsIgnoreCase("overwrite")){
                    player.sendMessage(Message.info("/kit overwrite <kitname> <on/true | off/false>"));
                }else if (arg.equalsIgnoreCase("delay")){
                    player.sendMessage(Message.info("/kit remove <kitname> <delay>"));
                }else{
                    if (player.hasPermission("kits.spawn." + arg)){
                        if (plugin.playerDelayed(player)){
                            if (plugin.getRemainingTime(plugin.getKitManager().getDelay(arg), player) < 1){
                                plugin.removeDelayedPlayer(player);
                            }else{
                                int remaining = plugin.getRemainingTime(plugin.getKitManager().getDelay(arg), player);
                                String seconds = remaining == 1 ? " second" : " seconds";
                                player.sendMessage(Message.warning("You must wait " + remaining + seconds + " before spawning another kit."));
                                return false;
                            }
                        }
                        if (plugin.getKitManager().exists(arg)){
                            ItemStack[] itemList = plugin.getKitManager().getKit(arg);
                            if (plugin.getKitManager().getOverwrite(arg)){
                                for (int i = 0; i < itemList.length; i ++){
                                    player.getInventory().setItem(i, itemList[i]);
                                }
                            }else{
                                try{
                                    for (int i = 0; i < itemList.length; i ++){
                                        player.getInventory().addItem(itemList[i]);
                                    }
                                }catch (Exception ex){
                                    if (player.getInventory().firstEmpty() == -1) sender.sendMessage(Message.warning("You don't have enough space in your inventory to spawn the entire kit."));
                                }
                            }
                            player.updateInventory();
                            player.sendMessage(Message.info("Kit " + arg + " spawned."));
                            
                            if (!player.hasPermission("kits.bypassdelay." + arg) && plugin.getDelay(plugin.getKitManager().getDelay(arg), 1) > 0) plugin.addDelayedPlayer(player);
                        }else{
                            player.sendMessage(Message.warning("Kit " + arg + " does not exist."));
                        }
                    }else{
                        player.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }
	        }else if (args.length > 1){
	            if (arg.equalsIgnoreCase("create")){
	                if (player.hasPermission("kits.admin")){
                        if (!plugin.getKitManager().exists(args[1])){
                            if (args.length > 2){
                                try{
                                    plugin.getKitManager().create(player, args[1], Integer.parseInt(args[2]));
                                }catch (Exception ex){
                                    player.sendMessage(Message.warning("Bars amount must be from 1-4"));
                                }
                            }else{
                                plugin.getKitManager().create(player, args[1], 1);
                            }
                            if (args.length > 3){
                            	if (args[3].equalsIgnoreCase("on") || args[3].equalsIgnoreCase("true")){
                            		plugin.getKitManager().setOverwrite(args[1], true);
                            	}else if (args[3].equalsIgnoreCase("off") || args[3].equalsIgnoreCase("false")){
                            		plugin.getKitManager().setOverwrite(args[1], false);
                            	}else{
                            		plugin.getKitManager().setOverwrite(args[1], false);
                                    player.sendMessage(Message.info("Kit " + args[1] + " overwrite is currently " + (plugin.getKitManager().getOverwrite(args[1]) ? "enabled." : "disabled.")));
                            	}
                            	if (args.length > 4){
                                	try{
                                		plugin.getKitManager().setDelay(args[1], Integer.parseInt(args[4]));
                                	}catch (Exception ex){
                                        player.sendMessage(Message.warning("Delay must be an integer."));
                                        plugin.getKitManager().setDelay(args[1], 0);
                                	}
                            	}
                            }else{
                                plugin.getKitManager().setOverwrite(args[1], false);
                                plugin.getKitManager().setDelay(args[1], 0);
                            }
                            player.setMetadata("editingKit", new FixedMetadataValue(plugin, false));
                            player.setMetadata("creatingKit", new FixedMetadataValue(plugin, true));
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " already exists."));
                        }
	                }else{
	                    sender.sendMessage(Message.warning("Incorrect Permissions."));
	                }
	            }else if (arg.equalsIgnoreCase("edit")){
                    if (player.hasPermission("kits.admin")){
    	                if (plugin.getKitManager().exists(args[1])){
                            plugin.getKitManager().edit(player, args[1]);
                            player.setMetadata("editingKit", new FixedMetadataValue(plugin, true));
                            player.setMetadata("creatingKit", new FixedMetadataValue(plugin, false));
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
                        }
                    }else{
                        sender.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }else if (arg.equalsIgnoreCase("remove")){
                    if (player.hasPermission("kits.admin")){
                        if (plugin.getKitManager().exists(args[1])){
                            plugin.getKitsConfig().set(args[1], null, false);
                            player.sendMessage(Message.info("Kit " + args[1] + " has been removed."));
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
                        }
                    }else{
                        sender.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }else if (arg.equalsIgnoreCase("overwrite")){
                    if (player.hasPermission("kits.admin")){
                        if (plugin.getKitManager().exists(args[1])){
                            if (args.length > 2){
                            	if (args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true")){
                            		plugin.getKitManager().setOverwrite(args[1], true);
                                    player.sendMessage(Message.info("Kit " + args[1] + " overwrite enabled."));
                            	}else if (args[2].equalsIgnoreCase("off") || args[2].equalsIgnoreCase("false")){
                            		plugin.getKitManager().setOverwrite(args[1], false);
                                    player.sendMessage(Message.info("Kit " + args[1] + " overwrite disabled."));
                            	}else{
                                    player.sendMessage(Message.info("Kit " + args[1] + " overwrite is currently " + (plugin.getKitManager().getOverwrite(args[1]) ? "enabled." : "disabled.")));
                            	}
                            }else{
                                player.sendMessage(Message.info("Kit " + args[1] + " overwrite is currently " + (plugin.getKitManager().getOverwrite(args[1]) ? "enabled." : "disabled.")));
                        	}
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
                        }
                    }else{
                        sender.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }else if (arg.equalsIgnoreCase("delay")){
                    if (player.hasPermission("kits.admin")){
                        if (plugin.getKitManager().exists(args[1])){
                            if (args.length > 2){
                            	try{
                            		plugin.getKitManager().setDelay(args[1], Integer.parseInt(args[2]));
                                    player.sendMessage(Message.info("Kit " + args[1] + " delay set to " + args[2] + "."));
                            	}catch (Exception ex){
                                    player.sendMessage(Message.warning("Delay must be an integer."));
                            	}
                            }else{
                                player.sendMessage(Message.info("Kit " + args[1] + " delay is currently " + plugin.getKitManager().getDelay(args[1]) + "."));
                        	}
                        }else{
                            player.sendMessage(Message.warning("Kit " + args[1] + " does not exist."));
                        }
                    }else{
                        sender.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }else{
                    if (player.hasPermission("kits.others.spawn." + arg)){
                        Player receiver = getPlayer(args[1]);

                        if (receiver != null){
	                        if (plugin.playerDelayed(receiver)){
	                            if (plugin.getRemainingTime(plugin.getKitManager().getDelay(arg), receiver) < 1){
	                                plugin.removeDelayedPlayer(receiver);
	                            }else{
	                                int remaining = plugin.getRemainingTime(plugin.getKitManager().getDelay(arg), receiver);
	                                String seconds = remaining == 1 ? " second" : " seconds";
	                                player.sendMessage(Message.warning(receiver.getName() + " must wait " + remaining + seconds + " before spawning another kit."));
	                                return false;
	                            }
	                        }
	                        
	                        if (plugin.getKitManager().exists(arg)){
	                                ItemStack[] itemList = plugin.getKitManager().getKit(arg);

	                                if (plugin.getKitManager().getOverwrite(arg)){
	                                    for (int i = 0; i < itemList.length; i ++){
	                                        receiver.getInventory().setItem(i, itemList[i]);
	                                    }
	                                }else{
	                                    try{
	                                        for (int i = 0; i < itemList.length; i ++){
	                                            receiver.getInventory().addItem(itemList[i]);
	                                        }
	                                    }catch (Exception ex){
	                                        if (receiver.getInventory().firstEmpty() == -1) sender.sendMessage(Message.warning(receiver.getName() + " didn't have enough space in their inventory to spawn the entire kit."));
	                                    }
	                                }
	                                receiver.updateInventory();
	                                receiver.sendMessage(Message.info("Kit " + arg + " spawned by " + player.getName() + "."));
	                                player.sendMessage(Message.info("Kit " + arg + " spawned for " + receiver.getName() + "."));
	                                
	                                if (!receiver.hasPermission("kits.bypassdelay." + arg) && plugin.getDelay(plugin.getKitManager().getDelay(arg), 1) > 0) plugin.addDelayedPlayer(receiver);
	                                
	                                //addKitMetric();
	                        }else{
	                            player.sendMessage(Message.warning("Kit " + arg + " does not exist."));
	                        }
	                    }else{
                            player.sendMessage(Message.warning(args[1] + " is not online."));
                        }
                    }else{
                        player.sendMessage(Message.warning("Incorrect Permissions."));
                    }
                }
	        }
	    }
		return false;
	}
	
	/*
	public void addKitMetric(){
		if (!plugin.getMetrics().isOptOut()){
			plugin.getMetrics().createGraph("Number of Kits Spawned").addPlotter(new Metrics.Plotter("Kit Spawn") {
				
				@Override
				public int getValue() {
					return 1;
				}
			});
		}
	}
	*/
	
	/**
	 * Bukkit's default getPlayer method does not search the entire player's name for a substring being searched for, whereas this method does.
	 * @param name The player's name
	 * @return the matched player, or null if no match
	 */
	public Player getPlayer(String playerName){
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.getName().toLowerCase().contains(playerName.toLowerCase())){
				return player;
			}
		}
		return null;
	}
}
