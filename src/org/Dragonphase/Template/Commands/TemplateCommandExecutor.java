package org.Dragonphase.Template.Commands;

import org.Dragonphase.Template.Template;
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
		if (args.length == 0){
			sender.sendMessage(plugin.pluginMessageFormat("Version " +  plugin.getPluginVersion() + "."));
		}else if (args.length > 0){
			if (args[0].startsWith("r")){
				if (sender.hasPermission("template.op")){
					try {
						plugin.reload();
						sender.sendMessage(plugin.pluginMessageFormat("Reloaded."));
					} catch (Exception e) {
						sender.sendMessage(plugin.pluginMessageFormat("Could not reload."));
					}
				}else{
					sender.sendMessage(plugin.pluginMessageFormat("Incorrect Permissions."));
				}
			}
		}
		return false;
	}

}
