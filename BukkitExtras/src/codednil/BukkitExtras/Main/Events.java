package codednil.BukkitExtras.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import codednil.BukkitExtras.Farming.BetterHoe;
import codednil.BukkitExtras.Wands.WandHandler;

public class Events implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void CreatureSpawn(CreatureSpawnEvent event) {
		LivingEntity creature = event.getEntity();
		if (event.getSpawnReason() == SpawnReason.BREEDING)
			if (creature.getType() == EntityType.PIG)
				for (int i = 0; i < Math.random() * 3 + 1; i++) {
					Pig pig = (Pig) creature;
					Pig animal = creature.getWorld().spawn(
							creature.getLocation(), Pig.class);
					animal.setAge(pig.getAge());
				}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void ShearEntity(PlayerShearEntityEvent event) {
		if (!event.getPlayer().hasPermission("bukkitextras.usefulanimals"))
			return;
		Entity entity = event.getEntity();
		if (entity instanceof Sheep)
			for (int i = 0; i < Math.random() * 3; i++)
				entity.getWorld().dropItemNaturally(entity.getLocation(),
						new ItemStack(Material.STRING));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void BlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		Material blocktype = block.getType();
		ItemStack item = player.getItemInHand();
		if (Util.isA(blocktype, "Crop")) {
			BetterHoe.breakCrop(block, player, blocktype, item, event);
			return;
		}
		if (event.isCancelled())
			return;
		if (blocktype == Material.MOB_SPAWNER
				&& item.containsEnchantment(Enchantment.SILK_TOUCH)
				&& Util.isA(item.getType(), "Pickaxe")) {
			event.setExpToDrop(0);
			CreatureSpawner state = (CreatureSpawner) block.getState();
			ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
			ItemMeta itemMeta = spawner.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(state.getCreatureTypeName());
			itemMeta.setLore(lore);
			spawner.setItemMeta(itemMeta);
			player.getWorld().dropItemNaturally(block.getLocation(), spawner);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void BlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		Material blocktype = block.getType();
		ItemStack item = event.getItemInHand();
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasLore())
			return;
		if (blocktype == Material.MOB_SPAWNER) {
			CreatureSpawner state = (CreatureSpawner) block.getState();
			state.setCreatureTypeByName(item.getItemMeta().getLore().get(0));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (event.isSneaking()) {
			if (!event.getPlayer().hasPermission("bukkitextras.elevators"))
				return;
			Block block = player.getLocation().getBlock()
					.getRelative(BlockFace.DOWN);
			if (block.getType() == BukkitExtras.Plugin.elevatorBlock) {
				if (!Compat.playerCanAccess(player, block.getLocation()))
					return;
				Material elevatorBlock = BukkitExtras.Plugin.elevatorBlock;
				Block target = Util.findNearest(block.getLocation(),
						elevatorBlock, -1, true);
				if (target != null) {
					Location playerLoc = player.getLocation();
					float yaw = playerLoc.getYaw();
					float pitch = playerLoc.getPitch();
					Location location = target.getLocation().add(
							new Vector(0.5, 1, 0.5));
					location.setYaw(yaw);
					location.setPitch(pitch);
					player.teleport(location);
					player.getWorld().playSound(target.getLocation(),
							Sound.ENDERMAN_TELEPORT, (float) 0.5, 1);
					player.getWorld().playEffect(
							target.getLocation().add(0, 1, 0),
							Effect.ENDER_SIGNAL, 1);
					player.getWorld().playEffect(
							block.getLocation().add(0, 1, 0),
							Effect.ENDER_SIGNAL, 1);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location movement = event.getTo().clone().subtract(event.getFrom());
		if (movement.getY() > 0.4) {
			Block block = player.getLocation().getBlock()
					.getRelative(BlockFace.DOWN);
			if (block.getType() == BukkitExtras.Plugin.elevatorBlock) {
				if (!event.getPlayer().hasPermission("bukkitextras.elevators"))
					return;
				if (!Compat.playerCanAccess(player, block.getLocation()))
					return;
				Material elevatorBlock = BukkitExtras.Plugin.elevatorBlock;
				Block target = Util.findNearest(block.getLocation(),
						elevatorBlock, 1, true);
				if (target != null) {
					Location playerLoc = player.getLocation();
					float yaw = playerLoc.getYaw();
					float pitch = playerLoc.getPitch();
					Location location = target.getLocation().add(
							new Vector(0.5, 1, 0.5));
					location.setYaw(yaw);
					location.setPitch(pitch);
					player.teleport(location);
					player.getWorld().playSound(target.getLocation(),
							Sound.ENDERMAN_TELEPORT, (float) 0.5, 1);
					player.getWorld().playEffect(
							target.getLocation().add(0, 1, 0),
							Effect.ENDER_SIGNAL, 1);
					player.getWorld().playEffect(
							block.getLocation().add(0, 1, 0),
							Effect.ENDER_SIGNAL, 1);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (block == null)
			return;
		Material blocktype = block.getType();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			if (Util.isA(event.getClickedBlock().getType(), "Crop")) {
				event.setCancelled(false);
				return;
			}
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getItem() == null)
			return;
		if (Util.isA(player.getItemInHand().getType(), "Hoe")) {
			if (!event.getPlayer().hasPermission("bukkitextras.betterhoes"))
				return;
			if (Util.isA(blocktype, "GrowCrop"))
				BetterHoe.growCrop(block, player);
			else if (blocktype == Material.SOIL)
				BetterHoe.plantCrop(block, player);
		}
		if (event.isCancelled())
			return;
		if (WandHandler.isWandOf(item, "Trading")) {
			if (!player.hasPermission("bukkitextras.wands"))
				return;
			if (Compat.playerCanBuild(player, block.getLocation()))
				if (player.isSneaking()) {
					ItemMeta itemMeta = item.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(block.getType().toString());
					lore.add("" + block.getData());
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
				} else {
					ItemMeta itemMeta = item.getItemMeta();
					List<String> lore = itemMeta.getLore();
					Inventory inventory = player.getInventory();
					if (itemMeta.hasLore()) {
						Material material = Material.valueOf(lore.get(0));
						byte data = Byte.parseByte(lore.get(1));
						ItemStack itemstack = new ItemStack(material);
						itemstack.setDurability(data);
						if ((block.getType() == material)
								&& (block.getData() == data))
							return;
						if (player.getGameMode() == GameMode.CREATIVE) {
							block.setType(material);
							block.setData(data);
							player.getWorld().playSound(block.getLocation(),
									Sound.DIG_STONE, (float) 0.5, 1);
						} else if (inventory.containsAtLeast(itemstack, 1)) {
							Collection<ItemStack> drops = block
									.getDrops(new ItemStack(
											Material.IRON_PICKAXE));
							block.setType(material);
							block.setData(data);
							Util.removeItem(player, itemstack, 1);
							player.getWorld().playSound(block.getLocation(),
									Sound.DIG_STONE, (float) 0.5, 1);
							for (ItemStack itemDrop : drops)
								Util.giveItem(player, itemDrop);
						}
						player.updateInventory();
					}
				}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("givewand")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length != 1) {
					player.sendMessage("Invalid number of arguments");
					return false;
				} else {
					for (int i = 0; i < WandHandler.wands.size(); i++) {
						ItemStack wand = WandHandler.wands.get(i);
						String wandName = wand.getItemMeta().getDisplayName()
								.replaceAll("Wand of ", "");
						if (args[0].equalsIgnoreCase(wandName)) {
							Util.giveItem(player, wand);
							return true;
						}
					}
					String message = "Wand not found, valid wands are: ";
					for (int i = 0; i < WandHandler.wands.size(); i++) {
						ItemStack wand = WandHandler.wands.get(i);
						String wandName = wand.getItemMeta().getDisplayName()
								.replaceAll("Wand of ", "");
						message = message.concat("§6" + wandName);
						if (i != WandHandler.wands.size() - 1)
							message = message.concat(", ");
					}
					player.sendMessage(message);
				}
				return true;
			}
		}
		return false;
	}
}