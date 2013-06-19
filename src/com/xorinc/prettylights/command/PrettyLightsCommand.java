package com.xorinc.prettylights.command;

import java.util.Locale;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xorinc.prettylights.PrettyLightsPlugin;

public class PrettyLightsCommand implements CommandExecutor {
	
	private PrettyLightsPlugin plugin;

	public PrettyLightsCommand(PrettyLightsPlugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1){
			sender.sendMessage("/prettylights [effect|off|reload]");
			return true;
		}
			
		
		if(args[0].equalsIgnoreCase("reload")){
			if(sender.hasPermission("prettylights.reload")){
				plugin.getEffectsConfig().relaod();
				sender.sendMessage(ChatColor.GOLD + "Config reloaded.");
			}
			else
				sender.sendMessage(ChatColor.RED + "You do not have permission to reload the config.");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("effects")){
			
			String message = ChatColor.GREEN + "Available effects: " + ChatColor.RESET;
			
			Set<String> effects = plugin.getEffectsConfig().getEffects();
			
			for(String s : effects){
				if(sender.hasPermission("prettylights.use." + s)){					
					message += s.toLowerCase(Locale.ENGLISH) + ", ";
				}
			}
			
			message = message.substring(0, message.length() - 2);
			
			sender.sendMessage(message);
			
			return true;
		}
		
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Only the player may execute this command.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args[0].equalsIgnoreCase("off")){
			plugin.getPlayerMap().remove(player.getName());
			sender.sendMessage(ChatColor.GOLD + "Particles cease to whirl around you...");
		}
		else{
			Set<String> effects = plugin.getEffectsConfig().getEffects();
			
			for(String s : effects){
				if(s.equalsIgnoreCase(args[0])){
					if(player.hasPermission("prettylights.use." + s)){					
						plugin.getPlayerMap().put(player.getName(), s);
						sender.sendMessage(ChatColor.GOLD + "Particles begin to float around you...");
						return true;
					}
					else{
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this effect.");
					}
				}
			}
			
			sender.sendMessage(ChatColor.RED + "The effect " + args[0] + " does not exist.");
		}
				
		return true;
	}

}
