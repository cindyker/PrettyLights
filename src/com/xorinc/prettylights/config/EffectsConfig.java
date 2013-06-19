package com.xorinc.prettylights.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.xorinc.prettylights.PrettyLightsPlugin;

public class EffectsConfig {
	
	private static final String CONFIG_NAME = "effects.yml";
	private PrettyLightsPlugin plugin;
	private String configVersion;
	private boolean DEBUG = false;
	
	private HashMap<String, Effect> effects = new HashMap<String, Effect>();
	private HashMap<String, Integer> particleNum = new HashMap<String, Integer>();
	private HashMap<String, Float> radii = new HashMap<String, Float>();
	private HashMap<String, Float> heights = new HashMap<String, Float>();
	private HashMap<String, Float> yMods = new HashMap<String, Float>();
	
	
	public EffectsConfig(PrettyLightsPlugin plugin){
		this.plugin = plugin;
		this.loadSettings();
	}
	
	public void relaod(){
		
		effects.clear();
		particleNum.clear();
		plugin.getPlayerMap().clear();
		this.loadSettings();
		
	}
	
	public void loadSettings(){
		if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    	
        File f = new File(plugin.getDataFolder(), CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to create " + CONFIG_NAME + "! No config options were loaded!");
                return;
            }
        }
        
        YamlConfiguration conf = new YamlConfiguration();
        conf.options().pathSeparator('/');
        conf.options().header(plugin.getDescription().getName() +  " configuration" + System.getProperty("line.separator") +
        					  "define new effects as follows, under the \"effects\" section:" + System.getProperty("line.separator") +
        					  "  <key>:                     #config identifier" + System.getProperty("line.separator") +
        					  "    name: <name>             #name of the effect displayed to players" + System.getProperty("line.separator") +
        					  "    type: <type>             #constant from the Effect enum" + System.getProperty("line.separator") +
        					  "    particlesPerTick: <int>  #number of particles to spawn every five ticks" + System.getProperty("line.separator") +
        					  "    radius: <float>          #length of a square centered on the player in which particles spawn" + System.getProperty("line.separator") +
        					  "    height: <float>          #y height to spawn the particles at" +  System.getProperty("line.separator") +
        					  "    yMod: <float>            #variation in height to spawn particles at" + System.getProperty("line.separator"));
        
        try {
            conf.load(f);
        } catch (Exception e) {
            plugin.getLogger().severe("==================== " + plugin.getDescription().getName() + " ====================");
            plugin.getLogger().severe("Unable to load " + CONFIG_NAME);
            plugin.getLogger().severe("Check your config for formatting issues!");
            plugin.getLogger().severe("No config options were loaded!");
            plugin.getLogger().severe("Error: " + e.getMessage());
            plugin.getLogger().severe("====================================================");
            return;
        }
        
        if (!conf.contains("version"))
        	conf.set("version", 0.1);
        configVersion = conf.getString("version");
        
        if (!conf.contains("debugMode"))
        	conf.set("debugMode", false);
        DEBUG = conf.getBoolean("debugMode");
        
        if(!conf.contains("effects"))
        	conf.createSection("effects");
        ConfigurationSection effectlist = conf.getConfigurationSection("effects");
        Set<String> effectset = effectlist.getKeys(false);
        
        for(String key : effectset){
        	
        	if(!effectlist.isConfigurationSection(key))
            	continue;
            ConfigurationSection currentEffect = effectlist.getConfigurationSection(key);
        	
        	String name = currentEffect.getString("name", null);
        	if(name == null || name.equalsIgnoreCase("off") || name.equalsIgnoreCase("reload") || name.equalsIgnoreCase("effects"))
        		continue;
        	
        		Effect effect = null;
        	try {
				effect = Effect.valueOf(currentEffect.getString("type", null));
			} catch (IllegalArgumentException e) {
				continue;
			} catch (NullPointerException e) {
				continue;
			}
        	if(effect == null)
        		continue;
        	
        	if(!currentEffect.contains("particlesPerTick"))
        		currentEffect.set("particlesPerTick", 1);
        	
        	int particlesPerTick = currentEffect.getInt("particlesPerTick", 1);
        	particlesPerTick = Math.max(particlesPerTick, 1);
        	
        	if(!currentEffect.contains("radius"))
        		currentEffect.set("radius", 1F);
        	
        	float radius = currentEffect.getInt("radius", 1);
        	radius = Math.abs(radius);
        	radius = radius==0?1:radius;
        	
        	if(!currentEffect.contains("height"))
        		currentEffect.set("height", 1.5F);
        	
        	float height = (float) currentEffect.getDouble("height", 1.5);
        	height = Math.abs(height);
        	
        	if(!currentEffect.contains("yMod"))
        		currentEffect.set("yMod", 0F);
        	
        	float yMod = (float) currentEffect.getDouble("yMod", 0);
        	yMod = Math.abs(yMod);
        	
        	effects.put(name, effect);
        	particleNum.put(name, particlesPerTick); 
        	radii.put(name, radius);
        	heights.put(name, height);
        	yMods.put(name, yMod);
        }
        
        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	public Set<String> getEffects(){
		return effects.keySet();
	}
	
	public Effect getEffect(String name){
		return effects.get(name);
	}
	
	public int getParticlesPerTick(String name){
		return particleNum.get(name);
	}
	
	public float getRadius(String name){
		return radii.get(name);
	}
	
	public float getHeight(String name){
		return heights.get(name);
	}
	
	public float getYMod(String name){
		return yMods.get(name);
	}
}
