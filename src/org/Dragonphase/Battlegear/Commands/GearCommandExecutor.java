package org.Dragonphase.Battlegear.Commands;

import org.Dragonphase.Battlegear.Battlegear;
import org.Dragonphase.Battlegear.Util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GearCommandExecutor implements CommandExecutor{
	
	private Battlegear plugin;

	public GearCommandExecutor(Battlegear template) {
		plugin = template;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if (args.length > 0){
			if (args[0].startsWith("r")){
				if (sender.hasPermission("battlegear.reload")){
					try {
						plugin.reload();
						sender.sendMessage(Message.info("Reloaded."));
					} catch (Exception e) {
						sender.sendMessage(Message.warning("Could not reload."));
					}
				}else{
					sender.sendMessage(Message.warning("Incorrect Permissions."));
				}
			}
		}else{
			sender.sendMessage(Message.info("Version " +  plugin.getPluginVersion() + "."));
		}
		return false;
	}
}
