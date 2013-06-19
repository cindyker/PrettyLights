package com.xorinc.prettylights.task;

import java.util.Random;

import org.bukkit.entity.Player;

import com.xorinc.prettylights.PrettyLightsPlugin;

public class ParticleTask implements Runnable {

	private PrettyLightsPlugin plugin;
	private Random random = new Random();
	
	public ParticleTask(PrettyLightsPlugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		
		for(Player player : plugin.getServer().getOnlinePlayers()){
			
			if(!plugin.getPlayerMap().containsKey(player.getName()))
				continue;
			
			String effect = plugin.getPlayerMap().get(player.getName());
			float radius = plugin.getEffectsConfig().getRadius(effect);
			float height = plugin.getEffectsConfig().getHeight(effect);
			float yMod = plugin.getEffectsConfig().getYMod(effect);
			
			for(int i = 0; i < plugin.getEffectsConfig().getParticlesPerTick(effect); i++){ 			
				player.getWorld().playEffect(player.getLocation().add((float) (random.nextFloat() * Math.signum(random.nextInt()) * radius), 
						height + (float) (random.nextFloat() * Math.signum(random.nextInt()) * yMod), (float) random.nextFloat() * Math.signum(random.nextInt()) * radius), 
						plugin.getEffectsConfig().getEffect(effect), new Random().nextInt(8), new Random().nextInt(8));				
			}		
		}		
	}
}
