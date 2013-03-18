package org.Dragonphase.Battlegear.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.v1_4_R1.NBTTagCompound;

import org.Dragonphase.Battlegear.Battlegear;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gear {
	
	public static FileManager gearConfig;
	
	private static int numItems, maximumChance;
	private static List<String> itemName, itemType, itemLadder, ladderColor, loreColor, color, damage;
	private static List<Integer> crit, chance, itemID, amount, armor, durability;
	private static List<ArrayList<String>> lore, enchantment, effect, mob, blockID, salvage;
	private static List<Boolean> unique, junk;
	
	public static void load(){

		gearConfig = new FileManager(Battlegear.plugin.getDataFolder(), "gear.yml");
		
		itemName  = new ArrayList<String>();
		itemType  = new ArrayList<String>();
		itemLadder  = new ArrayList<String>();
		ladderColor = new ArrayList<String>();
		loreColor = new ArrayList<String>();

		crit = new ArrayList<Integer>();
		chance = new ArrayList<Integer>();
		itemID = new ArrayList<Integer>();
		amount = new ArrayList<Integer>();
		armor = new ArrayList<Integer>();
		durability = new ArrayList<Integer>();
		color = new ArrayList<String>();
		damage = new ArrayList<String>();
		
		lore = new ArrayList<ArrayList<String>>();
		enchantment = new ArrayList<ArrayList<String>>();
		effect = new ArrayList<ArrayList<String>>();
		blockID = new ArrayList<ArrayList<String>>();
		mob = new ArrayList<ArrayList<String>>();
		salvage = new ArrayList<ArrayList<String>>();

		unique = new ArrayList<Boolean>();
		junk = new ArrayList<Boolean>();
		
		numItems = getNumItems(true);
		
		maximumChance = Battlegear.mainConfig.getInt("options.maximum-chance");
		
		for (int ID = 1; ID < numItems+1; ID ++){
			
			itemName.add(gearConfig.getString("items." + ID + ".name"));
			itemType.add(gearConfig.getString("items." + ID + ".type"));
			itemLadder.add(gearConfig.getString("items." + ID + ".ladder"));
			ladderColor.add(gearConfig.getString("ladder." + getLadder(ID) + ".color"));
			loreColor.add(gearConfig.getString("ladder." + getLadder(ID) + ".lore"));
			color.add(gearConfig.getString("items." + ID + ".color"));
			if (itemType.get(ID-1).equalsIgnoreCase("weapon")){
				damage.add(gearConfig.getString("items." + ID + ".stats.damage"));
				armor.add(0);
			}else{
				damage.add("");
				armor.add(gearConfig.getInt("items." + ID + ".stats.armor"));
			}

			crit.add(gearConfig.getInt("items." + ID + ".stats.crit-chance"));
			chance.add((gearConfig.getInt("items." + ID + ".chance") != 0 ? gearConfig.getInt("items." + ID + ".chance") : gearConfig.getInt("ladder." + getLadder(ID) + ".chance")));
			itemID.add(gearConfig.getInt("items." + ID + ".id"));
			amount.add(gearConfig.getInt("items." + ID + ".amount"));
			durability.add(gearConfig.getInt("items." + ID + ".stats.durability"));
			
			lore.add(gearConfig.getStringList("items." + ID + ".lore"));
			blockID.add(gearConfig.getStringList("items." + ID + ".blocks"));
			mob.add(gearConfig.getStringList("items." + ID + ".mobs"));
			salvage.add(gearConfig.getStringList("items." + ID + ".salvage"));
			
			unique.add((gearConfig.getInt("items." + ID + ".chance") > 0));
			junk.add(gearConfig.getBoolean("ladder." + getLadder(ID) + ".junk"));

			enchantment.add(gearConfig.getStringList("items." + ID + ".stats.enchantments"));
			effect.add(gearConfig.getStringList("items." + ID + ".stats.effects"));
		}
	}
	
	public static void reload(){
		load();
	}
	
	public static String getName(int ID){
		return itemName.get(ID-1);
	}
	
	public static String getType(int ID){
		return itemType.get(ID-1);
	}
	
	public static String getLadder(int ID){
		return itemLadder.get(ID-1);
	}
	
	public static String getLadderColor(int ID){
		return ladderColor.get(ID-1);
	}
	
	public static String displayLadder(int ID){
		return ChatColor.translateAlternateColorCodes('&', getLadderColor(ID)) + Details.capitalize(getLadder(ID));
	}
	
	public static String getLadderLoreColor(int ID){
		return loreColor.get(ID-1);
	}
	
	public static List<String> getLore(int ID){
		List<String> Lore = new ArrayList<String>();
		for (String loreLine : lore.get(ID-1)){
			Lore.add(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', getLadderLoreColor(ID)) + ChatColor.translateAlternateColorCodes('&', loreLine));
		}
		return Lore;
	}
	
	public static Map<Enchantment, Integer> getEnchantments(int ID){
		List<String> enchantments = enchantment.get(ID-1);
		Map<Enchantment, Integer> Enchantments = new HashMap<Enchantment, Integer>();
		
		for (String enchantment : enchantments){
			String[] enchantmentDetail = enchantment.split(" ");
			Enchantment enchantmentType;
			try{
				enchantmentType = Enchantment.getByName(enchantmentDetail[0]);
			}catch(Exception ex){
				enchantmentType = Enchantment.getById(Integer.parseInt(enchantmentDetail[0]));
			}
			 Enchantments.put(enchantmentType, Integer.parseInt(enchantmentDetail[1]));
			/*try{
				if (Integer.parseInt(enchantmentDetail[1]) > enchantmentType.getMaxLevel()) Enchantments.put(enchantmentType, enchantmentType.getMaxLevel());
				else if (Integer.parseInt(enchantmentDetail[1]) < 1) Enchantments.put(enchantmentType, 1);
				else Enchantments.put(enchantmentType, Integer.parseInt(enchantmentDetail[1]));
			}catch(Exception ex){
				 Enchantments.put(enchantmentType, 1);
			}*/
		}
		return Enchantments;
	}
	
	public static Map<Enchantment, Integer> getEnchantments(String enchantmentString){
		String[] enchantments = enchantmentString.replace("{", "").replace("}", "").replace(" ", "").split(",");
		Map<Enchantment, Integer> Enchantments = new HashMap<Enchantment, Integer>();
		
		for (String enchantment : enchantments){
			String[] enchantmentDetail = enchantment.split("=");
			Enchantment enchantmentType;
			try{
				enchantmentType = Enchantment.getByName(enchantmentDetail[0]);
			}catch(Exception ex){
				enchantmentType = Enchantment.getById(Integer.parseInt(enchantmentDetail[0]));
			}
			 Enchantments.put(enchantmentType, Integer.parseInt(enchantmentDetail[1]));
			/*try{
				if (Integer.parseInt(enchantmentDetail[1]) > enchantmentType.getMaxLevel()) Enchantments.put(enchantmentType, enchantmentType.getMaxLevel());
				else if (Integer.parseInt(enchantmentDetail[1]) < 1) Enchantments.put(enchantmentType, 1);
				else Enchantments.put(enchantmentType, Integer.parseInt(enchantmentDetail[1]));
			}catch(Exception ex){
				 Enchantments.put(enchantmentType, 1);
			}*/
		}
		return Enchantments;
	}
	
	public static ArrayList<PotionEffect> getAttackerEffects(int ID){
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		for (String potionEffect : effect.get(ID-1)){
			String[] effectDetail = potionEffect.split(":");
			if (effectDetail.length == 1) continue;
			if (effectDetail[0].startsWith("a")){
				String[] effectStats = effectDetail[1].split(" ");
				PotionEffectType effectType;
				try{
					effectType = PotionEffectType.getByName(effectStats[0]);
				}catch(Exception ex){
					effectType = PotionEffectType.getById(Integer.parseInt(effectStats[0]));
				}
				int duration = 100;
				int amplifier = 1;
				if (effectStats.length == 2){
					amplifier = Integer.parseInt(effectStats[1]);
				}
				if (effectStats.length == 3){
					amplifier = Integer.parseInt(effectStats[1]);
					duration = Integer.parseInt(effectStats[2]) * 20;
				}
				potionEffects.add(new PotionEffect(effectType, duration, amplifier));
			}
		}
		return potionEffects;
	}
	
	public static ArrayList<PotionEffect> getVictimEffects(int ID){
		ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
		for (String potionEffect : effect.get(ID-1)){
			String[] effectDetail = potionEffect.split(":");
			if (effectDetail.length == 1) continue;
			if (effectDetail[0].startsWith("v")){
				String[] effectStats = effectDetail[1].split(" ");
				PotionEffectType effectType;
				try{
					effectType = PotionEffectType.getByName(effectStats[0]);
				}catch(Exception ex){
					effectType = PotionEffectType.getById(Integer.parseInt(effectStats[0]));
				}
				int duration = 5*20;
				int amplifier = 5;
				if (effectStats.length == 2){
					duration = Integer.parseInt(effectStats[1]);
				}
				if (effectStats.length == 3){
					duration = Integer.parseInt(effectStats[1]);
					amplifier = Integer.parseInt(effectStats[2]);
				}
				potionEffects.add(new PotionEffect(effectType, duration, amplifier));
			}
		}
		return potionEffects;
	}
	
	public static int getMaximumChance(){
		return maximumChance;
	}
	
	public static int getChance(int ID){
		return chance.get(ID-1);
	}
	
	public static int getCritChance(Entity attacker){
		if (!(attacker instanceof LivingEntity)) return 0;
		try{
			String entityName = attacker.getType().getName().toLowerCase().replace("_", "");
			return BitEntity.getEntityCritChance(entityName);
		}catch(Exception ex){
			return BitEntity.getDefaultCritChance();
		}
	}
	
	public static int getCritChance(int ID){
		return crit.get(ID-1);
	}
	
	public static int getItemID(int ID){
		return itemID.get(ID-1);
	}
	
	public static int getAmount(int ID){
		return amount.get(ID-1);
	}
	
	public static Map<String, Integer> getDamage(Entity entity){
		if (!(entity instanceof LivingEntity)) return null;
		String Damage = BitEntity.getDefaultDamage();
		if (!(entity instanceof Player)){
			String entityName = entity.getType().getName().toLowerCase().replace("_", "");
			Damage = BitEntity.getEntityDamage(entityName) == "" ? BitEntity.getDefaultDamage() : BitEntity.getEntityDamage(entityName);
		}
		String[] damageRange = Damage.split("-");
		int minimumDamage = Integer.parseInt(damageRange[0]);
		int maximumDamage = Integer.parseInt(damageRange[1]);
		int criticalDamage = minimumDamage + maximumDamage;
		
		Map<String, Integer> damage = new HashMap<String, Integer>();
		damage.put("minimum", minimumDamage);
		damage.put("maximum", maximumDamage);
		damage.put("critical", criticalDamage);
		
		return damage;
	}
	
	public static Map<String, Integer> getDamage(int ID){
		
		String Damage = damage.get(ID-1) == "" ? BitEntity.getDefaultDamage() : damage.get(ID-1);
		
		String[] damageRange = Damage.split("-");
		int minimumDamage = Integer.parseInt(damageRange[0]);
		int maximumDamage = Integer.parseInt(damageRange[1]);
		int criticalDamage = minimumDamage + maximumDamage;
		
		Map<String, Integer> damage = new HashMap<String, Integer>();
		damage.put("minimum", minimumDamage);
		damage.put("maximum", maximumDamage);
		damage.put("critical", criticalDamage);
		
		return damage;
	}

	public int getArmor(){
		return Gear.gearConfig.getInt("items." + itemID + ".stats.armor");
	}
	
	public static int getMinimum(Entity entity){
		return getDamage(entity).get("minimum");
	}
	
	public static int getMaximum(Entity entity){
		return getDamage(entity).get("maximum");
	}
	
	public static int getCritical(Entity entity){
		return getDamage(entity).get("critical");
	}
	
	public static int getMinimum(int ID){
		return getDamage(ID).get("minimum");
	}
	
	public static int getMaximum(int ID){
		return getDamage(ID).get("maximum");
	}
	
	public static int getCritical(int ID){
		return getDamage(ID).get("critical");
	}
	
	public static int getArmor(int ID){
		return armor.get(ID-1);
	}
	
	public static String getDamageRange(int ID){
		return damage.get(ID-1);
	}
	
	public static int getDurability(int ID){
		return durability.get(ID-1);
	}
	
	public static String getColor(int ID){
		return color.get(ID-1);
	}
	
	public static boolean isUnique(int ID){
		return unique.get(ID-1);
	}
	
	public static boolean isJunk(int ID){
		return junk.get(ID-1);
	}
	
	public static int getNumItems(boolean load){
		if (load){
			int items = 0;
			for (String key : gearConfig.getKeys(true)){
				int position = StringUtils.countMatches(key, ".");
				if (position == 1 && key.startsWith("items.")){
					items++;
				}
			}
			return items;
		}else{
			return numItems;
		}
	}
	
	public static ItemStack updateItem(int ID, int durability){
		ItemStack item = new ItemStack(getItemID(ID));
		ItemMeta itemMeta = item.getItemMeta();
		try{
			itemMeta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', getLadderColor(ID)) + ChatColor.translateAlternateColorCodes('&', getName(ID)));
		}catch(Exception ex){
			itemMeta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', getName(ID)));
		}
		List<String> lore = new ArrayList<String>();
		if (getLore(ID) != null) lore = getLore(ID);
		try{
			if (getType(ID).equalsIgnoreCase("armor")){
				if (getArmor(ID) > 0) lore.add(ChatColor.GREEN + "Armor: " + ChatColor.AQUA + getArmor(ID));
			}else{
				if (getDamageRange(ID) != "") lore.add(ChatColor.GREEN + "Damage: " + ChatColor.AQUA + getDamageRange(ID));
			}
		}catch(Exception ex){}
		if (getDurability(ID) > 0){
			lore.add(ChatColor.GREEN + "Durability: " + ChatColor.AQUA + durability);
		}
		try{
			if (!isUnique(ID)) lore.add(displayLadder(ID));
		}catch(Exception ex){}
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		if (getEnchantments(ID) != null) item.addUnsafeEnchantments(getEnchantments(ID));
		try{
			if (getColor(ID).length() > 4){
				LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
				
				String[] color = getColor(ID).split(",");
				lam.setColor(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
				item.setItemMeta(lam);
			}
		}catch(Exception ex){}
		try{
			if (getAmount(ID) > 0) item.setAmount(getAmount(ID));
			else item.setAmount(1);
		}catch(Exception ex){}
		return setID(setDurability(item, durability), ID);
	}
	
	public static ItemStack getItem(int ID){
		return updateItem(ID, getDurability(ID));
	}
	
	public static List<Integer> getBlockIDs(int ID){
		List<Integer> intList = new ArrayList<Integer>();
		for (String blockIDs : blockID.get(ID-1)){
			intList.add(Integer.parseInt(blockIDs));
		}
		return intList;
	}
	
	public static List<String> getMobs(int ID){
		List<String> mobList = new ArrayList<String>();
		for (String mobName : mob.get(ID-1)){
			mobList.add(mobName);
		}
		return mobList;
	}
	
	public static List<String> getSalvageItems(int ID){
		List<String> sale = new ArrayList<String>();
		for (String saleItem : salvage.get(ID-1)){
			sale.add(saleItem);
		}
		return sale;
	}
	
	public static ArrayList<ItemStack> getItems(Block block){
		Random randomChance = new Random();
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 1; i < getNumItems(false)+1; i ++){
			for (int blockID : getBlockIDs(i)){
				if (blockID == block.getTypeId()){
					items.add(i);
					break;
				}
			}
		}
		ArrayList<ItemStack> itemStack = new ArrayList<ItemStack>();
		for (int i = 0; i < items.size(); i ++){
			int chance = randomChance.nextInt(getMaximumChance()) + 1;
			if (chance <= getChance(items.get(i))){
				itemStack.add(getItem(items.get(i)));
			}
		}
		return itemStack;
	}
	
	public static ArrayList<ItemStack> getItems(String mobName){
		Random randomChance = new Random();
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 1; i < getNumItems(false)+1; i ++){
			for (String mob : getMobs(i)){
				
				if (mob.equalsIgnoreCase(mobName.toLowerCase().replace(" ", "_"))){
					items.add(i);
					break;
				}
			}
		}
		ArrayList<ItemStack> itemStack = new ArrayList<ItemStack>();
		for (int i = 0; i < items.size(); i ++){
			int chance = randomChance.nextInt(getMaximumChance()) + 1;
			if (chance <= getChance(items.get(i))){
				itemStack.add(getItem(items.get(i)));
			}
		}
		return itemStack;
	}
	
	public static ItemStack setID(ItemStack itemStack, int ID){
		if (itemStack.getTypeId() == 0) return itemStack;
		net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if(nms.tag != null)tag = nms.tag;
		else
		{
		    nms.tag = new NBTTagCompound();
		    nms.tag = tag;
		}
		tag.setInt("ID", ID);
		return CraftItemStack.asCraftMirror(nms);
	}
	
	public static int getID(ItemStack itemStack){
		if (itemStack.getTypeId() == 0) return 0;
		net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if(nms.tag != null)tag = nms.tag;
		else
		{
		    nms.tag = new NBTTagCompound();
		    nms.tag = tag;
		}
		try{
			return tag.getInt("ID");
		}catch(Exception ex){
			return 0;
		}
	}
	
	public static ItemStack setDurability(ItemStack itemStack, int durability){
		if (itemStack.getTypeId() == 0) return itemStack;
		net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if(nms.tag != null)tag = nms.tag;
		else
		{
		    nms.tag = new NBTTagCompound();
		    nms.tag = tag;
		}
		tag.setInt("Durability", durability);
		return CraftItemStack.asCraftMirror(nms);
	}
	
	public static int getDurability(ItemStack itemStack){
		if (itemStack.getTypeId() == 0) return 0;
		net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if(nms.tag != null)tag = nms.tag;
		else
		{
		    nms.tag = new NBTTagCompound();
		    nms.tag = tag;
		}
		try{
			return tag.getInt("Durability");
		}catch(Exception ex){
			return 0;
		}
	}
}
