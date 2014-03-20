package com.xorinc.prettylights;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import com.xorinc.prettylights.ParticleEffect.Particle;

public class EffectsConfig {
	
	private static final String CONFIG_NAME = "effects.yml";
	private PrettyLightsPlugin plugin;
	
	
	public EffectsConfig(PrettyLightsPlugin plugin){
		this.plugin = plugin;
		this.loadSettings();
	}
	
	
	public Map<String, ParticleEffect> loadSettings(){
		if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    	
        File f = new File(plugin.getDataFolder(), CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to create " + CONFIG_NAME + "! No config options were loaded!");
                return Collections.emptyMap();
            }
        }
        
        YamlConfiguration conf = new YamlConfiguration();
        conf.options().pathSeparator('.');
        
        try {
            conf.load(f);
        } catch (Exception e) {
            plugin.getLogger().severe("==================== " + plugin.getDescription().getName() + " ====================");
            plugin.getLogger().severe("Unable to load " + CONFIG_NAME);
            plugin.getLogger().severe("Check your config for formatting issues!");
            plugin.getLogger().severe("No config options were loaded!");
            plugin.getLogger().severe("Error: " + e.getMessage());
            plugin.getLogger().severe("====================================================");
            return Collections.emptyMap();
        }
        
        Map<String, ParticleEffect> effects = new HashMap<String, ParticleEffect>();
        
        if(!conf.contains("effects"))
        	conf.createSection("effects");
        ConfigurationSection effectlist = conf.getConfigurationSection("effects");
        Set<String> effectset = effectlist.getKeys(false);
        
        for(String key : effectset){
        	
        	try {
        	
        	if(!effectlist.isConfigurationSection(key))
            	continue;
        	
        	List<Particle> particles = new ArrayList<Particle>();
        	
            ConfigurationSection eff = effectlist.getConfigurationSection(key);
        	
	            for(String key1 : eff.getKeys(false)){
	            
	            	if(!eff.isConfigurationSection(key1))
	                	continue;
	            	
	            	ConfigurationSection particle = eff.getConfigurationSection(key1);
	            	
	            	Effect effect = Effect.valueOf(particle.getString("particle"));
	            	
	            	int id = particle.getInt("id");
	            	int data = particle.getInt("data");
	            	
	            	List<String> rawlocs = particle.getStringList("locs");
	            	List<Vector> locs = new ArrayList<Vector>();
	            	
	            	for(String raw : rawlocs){
	            		
	            		String[] split = raw.split("\\|");
	            		
	            		locs.add(new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2])));
	            		
	            	}
	            	
	            	if(locs.isEmpty())
	            		locs.add(new Vector(0, 0, 0));
	            	
	            	String rawoffset = particle.getString("offset");
	            	Vector offset = new Vector(0, 0, 0);
	            	
	            	{           		
	            		String[] split = rawoffset.split("\\|");
	            		
	            		offset = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));	            		
	            	}
	            	
	            	float speed = (float) particle.getDouble("speed", 1.0);
	            	
	            	int countMin = particle.getInt("countMin", 1);
	            	int countMax = particle.getInt("countMax", 1);
	            	
	            	int period = particle.getInt("period", 1);
	            	
	            	particles.add(new Particle(effect, id, data, locs, offset, speed, countMin, countMax, period));
	        	}
	            
	            effects.put(key.toLowerCase(Locale.ENGLISH), new ParticleEffect(particles));
        	}
        	catch (Exception e) {
        		
        		plugin.getLogger().warning("Particle " + key + " generated exception:");
        		e.printStackTrace();
        	}
            
        }
        
        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		return effects;
	}
}
