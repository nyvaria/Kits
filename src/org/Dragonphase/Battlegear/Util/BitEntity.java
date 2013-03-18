package org.Dragonphase.Battlegear.Util;

import java.util.HashMap;

import org.Dragonphase.Battlegear.Battlegear;
import org.apache.commons.lang.StringUtils;

public class BitEntity {
	
	private static int defaultMaxHealth, defaultCritChance;
	private static String defaultDamage;
	
	private static HashMap<String, Integer> entityMaxHealth, entityCritChance;
	private static HashMap<String, String> entityDamage;
	
	public static void load(){
		defaultMaxHealth = Battlegear.mainConfig.getInt("options.max-health");
		defaultDamage = Battlegear.mainConfig.getString("options.default-damage");
		defaultCritChance = Battlegear.mainConfig.getInt("options.default-crit-chance");
		
		entityMaxHealth = new HashMap<String, Integer>();
		entityCritChance = new HashMap<String, Integer>();
		entityDamage = new HashMap<String, String>();
		
		for (String key : Battlegear.mainConfig.getKeys(true)){
			if (key.startsWith("options.mobs.") && StringUtils.countMatches(key, ".") == 2){
				String entityName = key.replace("options.mobs.", "");
				entityMaxHealth.put(entityName, Battlegear.mainConfig.getInt(key + ".max-health"));
				entityCritChance.put(entityName, Battlegear.mainConfig.getInt(key + ".crit-chance"));
				entityDamage.put(entityName, Battlegear.mainConfig.getString(key + ".default-damage"));
			}
		}
	}

	public static int getDefaultMaxHealth(){
		return defaultMaxHealth;
	}
	
	public static int getEntityMaxHealth(String entityName){
		return entityMaxHealth.get(entityName);
	}
	
	public static String getDefaultDamage(){
		return defaultDamage;
	}
	
	public static String getEntityDamage(String entityName){
		return entityDamage.get(entityName);
	}
	
	public static int getDefaultCritChance(){
		return defaultCritChance;
	}
	
	public static int getEntityCritChance(String entityName){
		return entityCritChance.get(entityName);
	}

	public static void reload(){
		load();
	}
}
