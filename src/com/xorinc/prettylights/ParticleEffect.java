package com.xorinc.prettylights;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class ParticleEffect {

	private static final Random random = new Random();
	
	private List<Particle> particles;
	
	public ParticleEffect(List<Particle> particles){
		this.particles = particles;
	}
	
	public void play(Player p, long tick){
		
		List<Player> players = new ArrayList<Player>();
		
		players.add(p);
		
		for(Entity e : p.getNearbyEntities(16, 16, 16)){		
			if(e instanceof Player && !((Player) e).spigot().getHiddenPlayers().contains(p))
				players.add((Player) e);
		}
		
		
		
		for(Particle par : particles){
			
			if(tick % par.period != 0)
				continue;			
			
			Location loc = p.getLocation().add(par.locations.get(random.nextInt(par.locations.size())));
			
			for(Player p1 : players){
				
				p1.spigot().playEffect(loc, par.effect, par.id, par.data, (float) par.offset.getX(), (float) par.offset.getY(), (float) par.offset.getZ(), par.speed, par.countMin + random.nextInt(par.countMax - par.countMin + 1), 16);
				
			}
			
		}
		
	}
	
	
	public static class Particle {
		
		private Effect effect;
		
		private int id, data;
		
		private List<Vector> locations;
		
		private Vector offset;
				
		private float speed;
				
		private int countMin, countMax;
		
		private int period;

		public Particle(Effect effect, int id, int data,
				List<Vector> locations, Vector offset, float speed,
				int countMin, int countMax, int period) {

			super();
			this.effect = effect;
			this.id = id;
			this.data = data;
			this.locations = locations;
			this.offset = offset;
			this.speed = speed;
			this.countMin = countMin;
			this.countMax = countMax;
			this.period = period;
		}

		public static enum RandomType {
			
			UNIFORM, GAUSSIAN
		}
	}
}
