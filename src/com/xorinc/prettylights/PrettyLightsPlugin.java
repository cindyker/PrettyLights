package com.xorinc.prettylights;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.xorinc.prettylights.command.PrettyLightsCommand;
import com.xorinc.prettylights.config.EffectsConfig;
import com.xorinc.prettylights.task.ParticleTask;

public class PrettyLightsPlugin extends JavaPlugin {

	private EffectsConfig config;
	private HashMap<String, String> playerMap = new HashMap<String, String>();
	
	public void onEnable(){
		
		config = new EffectsConfig(this);
		getCommand("prettylights").setExecutor(new PrettyLightsCommand(this));
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new ParticleTask(this), 0, 5);
	}
	
	public EffectsConfig getEffectsConfig(){
		return config;
	}
	
	public HashMap<String, String> getPlayerMap(){
		return playerMap;
	}
	
}
