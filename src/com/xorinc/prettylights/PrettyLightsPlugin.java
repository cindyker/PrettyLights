package com.xorinc.prettylights;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PrettyLightsPlugin extends JavaPlugin {

	private EffectsConfig config;
	private Map<Player, ParticleEffect> playerMap = Collections.synchronizedMap(new HashMap<Player, ParticleEffect>());
	
	private Map<String, ParticleEffect> effects = null;
	
	public void onEnable(){
		
		config = new EffectsConfig(this);
		
		effects = config.loadSettings();
		
		new BukkitRunnable() {

			int tick = 0;
					
			@Override
			public void run() {

				for(Entry<Player, ParticleEffect> e : playerMap.entrySet()){
					
					e.getValue().play(e.getKey(), tick);
					
				}
				
				tick++;
			}
			
		}.runTaskTimer(this, 0, 2);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1){
			sender.sendMessage("/prettylights [effect|off]");
			return true;
		}
			
		
		if(args[0].equalsIgnoreCase("reload")){
			
			if(sender.hasPermission("prettylights.reload")){
				
				playerMap.clear();
				effects = this.config.loadSettings();
				
				sender.sendMessage(ChatColor.GOLD + "Config reloaded.");
			}
			
			else{
				sender.sendMessage(ChatColor.RED + "You do not have permission to reload the config.");
			}
			
			return true;
		}
		
		if(!(sender instanceof Player)){
			
			sender.sendMessage(ChatColor.RED + "Only the player may execute this command.");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("effects")){
			
			String message = ChatColor.GREEN + "Available effects: " + ChatColor.RESET;
						
			for(String s : effects.keySet()){
				if(sender.hasPermission("prettylights.use." + s) || sender.hasPermission("prettylights.use.*")){					
					message += s.toLowerCase(Locale.ENGLISH) + ", ";
				}
			}
			
			message = message.substring(0, message.length() - 2);
			
			sender.sendMessage(message);
			
			return true;
		}
		
		
		Player player = (Player) sender;
		
		if(args[0].equalsIgnoreCase("off")){
			
			playerMap.remove(player);
			
			sender.sendMessage(ChatColor.GOLD + "Particles cease to whirl around you...");
		}
		else{
			
			String effectName = args[0].toLowerCase(Locale.ENGLISH);
			
			if(effects.containsKey(args[0].toLowerCase(Locale.ENGLISH))){
				
				if(player.hasPermission("prettylights.use." + effectName) || player.hasPermission("prettylights.use.*")){
					
					playerMap.put(player, effects.get(effectName));
					sender.sendMessage(ChatColor.GOLD + "Particles begin to float around you...");
					return true;
				}
				
				else{
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this effect.");
				}
			}
			
			else
				sender.sendMessage(ChatColor.RED + "The effect " + args[0] + " does not exist.");
		}
				
		return true;
	}
	
}
