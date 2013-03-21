package org.Dragonphase.Template.Commands;

import org.Dragonphase.Template.Template;
import org.Dragonphase.Template.Util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TemplateCommandExecutor implements CommandExecutor{
	
	private Template plugin;

	public TemplateCommandExecutor(Template template) {
		plugin = template;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if (args.length > 0){
			if (args[0].startsWith("r")){
				if (sender.hasPermission("template.op")){
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
