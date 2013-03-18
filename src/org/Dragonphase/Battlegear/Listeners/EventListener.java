package org.Dragonphase.Battlegear.Listeners;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.Dragonphase.Battlegear.Battlegear;
import org.Dragonphase.Battlegear.Util.BitEntity;
import org.Dragonphase.Battlegear.Util.Details;
import org.Dragonphase.Battlegear.Util.Gear;
import org.Dragonphase.Battlegear.Util.Message;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class EventListener implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Battlegear plugin;
	
	private enum AttackerType{
		NONE, PLAYER, MOB
	}

	public EventListener(Battlegear instance){
		plugin = instance;
	}
	
	/**
	 * Handle creature default health
	 **/
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event){
		Entity e = event.getEntity();
		if (!Details.isInWorld((LivingEntity)e)) return;
		if (e instanceof Player) return;
		if (!(e instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity)e;
		
		//Get entity name
		
		String entityName = entity.getType().getName().toLowerCase().replace("_", "");
		if (entityName.equalsIgnoreCase("skeleton")){
			Skeleton skeleton = (Skeleton)e;
			entityName = (skeleton.getSkeletonType() == SkeletonType.WITHER) ? "witherskeleton" : "skeleton";
		}
		
		//Set entity health
		
		try{
			entity.setMaxHealth(BitEntity.getEntityMaxHealth(entityName));
		}catch(Exception ex){
			entity.setMaxHealth(BitEntity.getDefaultMaxHealth());
		}
		entity.setHealth(entity.getMaxHealth());
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		World world = event.getBlock().getWorld();
		if (!Details.isInWorld(event.getBlock())) return;
		
		//Refund item metadata from block.
		
		if (event.getBlock().hasMetadata("display-name")){
			ItemStack itemStack = new ItemStack(event.getBlock().getTypeId());
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (event.getBlock().getMetadata("display-name").get(0).asString() != "") itemMeta.setDisplayName(event.getBlock().getMetadata("display-name").get(0).asString());
			if (event.getBlock().getMetadata("lore").get(0).asString() != "") itemMeta.setLore((List<String>) event.getBlock().getMetadata("lore").get(0).value());
			itemStack.setItemMeta(itemMeta);
			try{
				if (event.getBlock().getMetadata("enchants").get(0).asString() != "") itemStack.addUnsafeEnchantments(Gear.getEnchantments(event.getBlock().getMetadata("enchants").get(0).value().toString()));
			}catch(Exception ex){}

			try{
				if (event.getBlock().getMetadata("Durability").get(0).asInt() != 0) itemStack = Gear.setDurability(itemStack, event.getBlock().getMetadata("Durability").get(0).asInt());
			}catch(Exception ex){}
			
			if (event.getBlock().getMetadata("ID").get(0).asInt() != 0){
				world.dropItemNaturally(event.getBlock().getLocation(), Gear.setID(itemStack, event.getBlock().getMetadata("ID").get(0).asInt()));
				event.setCancelled(true);
				world.getBlockAt(event.getBlock().getLocation()).setMetadata("display-name", new FixedMetadataValue(plugin, ""));
				world.getBlockAt(event.getBlock().getLocation()).setMetadata("lore", new FixedMetadataValue(plugin, ""));
				world.getBlockAt(event.getBlock().getLocation()).setMetadata("enchants", new FixedMetadataValue(plugin, ""));
				world.getBlockAt(event.getBlock().getLocation()).setMetadata("Durability", new FixedMetadataValue(plugin, 0));
				world.getBlockAt(event.getBlock().getLocation()).setMetadata("ID", new FixedMetadataValue(plugin, 0));
				world.getBlockAt(event.getBlock().getLocation()).setTypeId(0);
			}
		}
		
		//Detect whether block is natural or player-placed
		
		try{
			if (!event.getBlock().getMetadata("placed").get(0).asBoolean()){
				for (ItemStack item : Gear.getItems(event.getBlock())){
					world.dropItemNaturally(event.getBlock().getLocation(), item);
				}
			}
		}catch(Exception ex){
			try{
				for (ItemStack item : Gear.getItems(event.getBlock())){
					world.dropItemNaturally(event.getBlock().getLocation(), item);
				}
			}catch (Exception e){}
		}
		updateItemInHand(event.getPlayer(), event.getPlayer().getItemInHand());
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		if (!Details.isInWorld((LivingEntity)event.getPlayer())) return;
		fixHealth((Player) event.getPlayer());
	}
	
	/*@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		BitPlayer bitPlayer = BitPlayer.getBitPlayer(player);
		
		String message = event.getMessage();
		
		if (bitPlayer.getSalvaging()){
			if (message.equalsIgnoreCase("yes")){
				ItemStack item = bitPlayer.getSalvageItem();
				int ID = Gear.getID(item);
				int index = 0;
				boolean hasItem = false;
				for (int i = 0; i < player.getInventory().getContents().length; i ++){
					ItemStack itemStack = player.getInventory().getItem(i);
					if (Gear.getID(itemStack) == ID){
						index = i;
						hasItem = true;
						break;
					}
				}
				if (hasItem){
					for (String saleItem : Gear.getSalvageItems(ID)){
						String[] itemDetails = saleItem.split("x");
						ItemStack newItem = new ItemStack(Integer.parseInt(itemDetails[0]));
						int itemAmount = 1;
						if (itemDetails.length > 1){
							itemAmount = Integer.parseInt(itemDetails[0]);
							newItem = new ItemStack(Integer.parseInt(itemDetails[1]));
						}
						newItem.setAmount(itemAmount);
						player.getInventory().addItem(newItem);
					}
					player.getInventory().setItem(index, new ItemStack(0));
					player.sendMessage(ChatColor.DARK_GRAY + "Battlegear: " +  ChatColor.GRAY + "Salvaged " + Gear.getName(ID) + "!");
				}else{
					player.sendMessage(ChatColor.DARK_GRAY + "Battlegear: " +  ChatColor.GRAY + "Could not salvage " + Gear.getName(ID) + "!");
				}
				event.setCancelled(true);
			}else{
				player.sendMessage(ChatColor.DARK_GRAY + "Battlegear: " +  ChatColor.GRAY + "Salvage cancelled!");
			}
			bitPlayer.setSalvageItem(new ItemStack(0));
			bitPlayer.setSalvaging(false);
		}
	}*/

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		Entity e = event.getEntity();
		if (!Details.isInWorld((LivingEntity)e)) return;
		World world = e.getWorld();
		if (e instanceof Player) return;
		
		String entityName = e.getType().getName();
		if (entityName.equalsIgnoreCase("skeleton")){
			Skeleton skeleton = (Skeleton)e;
			entityName = (skeleton.getSkeletonType() == SkeletonType.WITHER) ? "witherskeleton" : "skeleton";
		}
		for (ItemStack item : Gear.getItems(entityName)){
			world.dropItemNaturally(e.getLocation(), item);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		Entity attacker = event.getDamager();
		Entity victim = event.getEntity();
		if (!(victim instanceof LivingEntity)) return;
		if (!Details.isInWorld((LivingEntity)victim)) return;
		
		if (victim instanceof Player){
			if (((Player)victim).getGameMode() != GameMode.SURVIVAL) return;
		}
		
		int ID = 0;
		ItemStack item = new ItemStack(0);
		AttackerType attackerType = AttackerType.NONE;
		
		if (attacker instanceof Player){
			item = ((Player)attacker).getItemInHand();
			ID = Gear.getID(item);
			attackerType = AttackerType.PLAYER;
		}
		else if (attacker instanceof LivingEntity){
			item = ((LivingEntity)attacker).getEquipment().getItemInHand();
			ID = Gear.getID(item);
			attackerType = AttackerType.MOB;
		}
		else if (attacker.getType() == EntityType.ARROW){
			try{
				Arrow arrow = (Arrow)attacker;
				item = arrow.getShooter().getEquipment().getItemInHand();
				ID = Gear.getID(item);
				attacker = arrow.getShooter();
			}catch(Exception ex){}
			attackerType = AttackerType.MOB;
		}
		
		int minDamage = 0;
		int maxDamage = 0;
		int critDamage = 0;
		
		if (attackerType == AttackerType.PLAYER || attackerType == AttackerType.MOB){
			if (ID == 0){
				minDamage = Gear.getMinimum(attacker);
				maxDamage = Gear.getMaximum(attacker);
				critDamage = Gear.getCritical(attacker);
			}else{
				minDamage = Gear.getMinimum(ID);
				maxDamage = Gear.getMaximum(ID);
				critDamage = Gear.getCritical(ID);
			}
		}
		
		Random random = new Random();
		int damage = 0;
		Random crit = new Random();
		if (ID == 0){
			if (crit.nextInt(Gear.getMaximumChance()) <= Gear.getCritChance(attacker)){
				damage = random.nextInt(critDamage-(critDamage-(maxDamage-minDamage)))+(critDamage-(maxDamage-minDamage));
			}else{
				damage = random.nextInt(maxDamage-minDamage)+minDamage;
			}
		}else{
			if (crit.nextInt(Gear.getMaximumChance()) <= Gear.getCritChance(ID)){
				damage = random.nextInt(critDamage-(critDamage-(maxDamage-minDamage)))+(critDamage-(maxDamage-minDamage));
			}else{
				damage = random.nextInt(maxDamage-minDamage)+minDamage;
			}
		}
		
		try{
			((LivingEntity)victim).addPotionEffects(Gear.getVictimEffects(ID));
		}catch (Exception ex){}
		try{
			((LivingEntity)attacker).addPotionEffects(Gear.getAttackerEffects(ID));
		}catch (Exception ex){}
		
		event.setDamage(damage);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if (event.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("_hoe")) return;
		event.getBlock().setMetadata("placed", new FixedMetadataValue(plugin, true));
		if (!Details.isInWorld(event.getBlock())) return;
		for (String key : Details.getItemMeta(event.getPlayer().getItemInHand()).keySet()){
			event.getBlock().setMetadata(key, new FixedMetadataValue(plugin, Details.getItemMeta(event.getPlayer().getItemInHand()).get(key)));
		}
		event.getBlock().setMetadata("ID", new FixedMetadataValue(plugin, Gear.getID(event.getPlayer().getItemInHand())));
		try{
			event.getBlock().setMetadata("Durability", new FixedMetadataValue(plugin, Gear.getDurability(event.getPlayer().getItemInHand())));
		}catch(Exception ex){
			
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		if (!Details.isInWorld((LivingEntity)event.getPlayer())) return;
		if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		Player player = event.getPlayer();
		
		fixHealth(player);
	}
	
	public void fixHealth(Player player){		
		int baseMaxHealth = BitEntity.getDefaultMaxHealth();
		int armor = 0;
		int baseArmorMultiplier = baseMaxHealth/100;
		
		int currentMaxHealth = player.getMaxHealth();
		
		for (ItemStack item : player.getInventory().getArmorContents()){
			if (item.getTypeId() == 0) continue;
			int ID = Gear.getID(item);
			if (ID > 0){
				armor += Gear.getArmor(ID);
			}else{
				String itemType = item.getType().toString().toLowerCase().substring(0, item.getType().toString().indexOf("_"));
				if (itemType.equalsIgnoreCase("leather")) armor += baseArmorMultiplier*10;
				if (itemType.equalsIgnoreCase("iron")) armor += baseArmorMultiplier*20;
				if (itemType.equalsIgnoreCase("chainmail")) armor += baseArmorMultiplier*20;
				if (itemType.equalsIgnoreCase("gold")) armor += baseArmorMultiplier*35;
				if (itemType.equalsIgnoreCase("diamond")) armor += baseArmorMultiplier*50;
			}
		}
		
		player.setMaxHealth(baseMaxHealth + armor);
		if (player.getMaxHealth() > currentMaxHealth){
			player.sendMessage(Message.info("Maximum health increased by " + armor + "!"));
		}else if (player.getMaxHealth() < currentMaxHealth){
			player.sendMessage(Message.warning("Maximum health decreased by " + (currentMaxHealth - player.getMaxHealth()) + "!"));
		}
	}
	
	public void updateItemInHand(Entity attacker, ItemStack item){
		int ID = Gear.getID(item);
		if (ID == 0) return;
		if (Gear.getDurability(item) == 0) return;
		if (attacker instanceof Player){
			if (Gear.getDurability(item)-1 < 1){
				if (item.getAmount() > 1){
					item.setAmount(item.getAmount()-1);
					((Player)attacker).setItemInHand(Gear.setDurability(item, Gear.getDurability(ID)));
				}else{
					((Player)attacker).setItemInHand(new ItemStack(0));
				}
			}else{
				ItemStack newItem = Gear.updateItem(ID, Gear.getDurability(item)-1);
				newItem.setAmount(item.getAmount());
				((Player)attacker).setItemInHand(newItem);
			}
		}
		else if (attacker instanceof LivingEntity){
			if (Gear.getDurability(item)-1 < 1){
				((LivingEntity)attacker).getEquipment().setItemInHand(new ItemStack(0));
			}else{
				((LivingEntity)attacker).getEquipment().setItemInHand(Gear.updateItem(ID, Gear.getDurability(item)-1));
			}
		}
		else if (attacker.getType() == EntityType.ARROW){
			if (Gear.getDurability(item)-1 < 1){
				((Arrow)attacker).getShooter().getEquipment().setItemInHand(new ItemStack(0));
			}else{
				 ((Arrow)attacker).getShooter().getEquipment().setItemInHand(Gear.updateItem(ID, Gear.getDurability(item)-1));
			}
		}
		item.setDurability(item.getDurability());
	}
}
