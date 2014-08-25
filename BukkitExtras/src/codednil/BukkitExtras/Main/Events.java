package codednil.BukkitExtras.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import codednil.BukkitExtras.Farming.BetterHoe;

public class Events implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void BlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		if (event.getBlock().getType() != Material.MOB_SPAWNER)
			return;
		if (!Util.isA(event.getPlayer().getItemInHand().getType(), "Pickaxe"))
			return;
		if (!event.getPlayer().getItemInHand()
				.containsEnchantment(Enchantment.SILK_TOUCH))
			return;
		event.setExpToDrop(0);
		CreatureSpawner state = (CreatureSpawner) event.getBlock().getState();
		ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
		ItemMeta itemMeta = spawner.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(state.getCreatureTypeName());
		itemMeta.setLore(lore);
		spawner.setItemMeta(itemMeta);
		event.getPlayer().getWorld()
				.dropItemNaturally(event.getBlock().getLocation(), spawner);
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
		if (blocktype != Material.MOB_SPAWNER)
			return;
		CreatureSpawner state = (CreatureSpawner) block.getState();
		state.setCreatureTypeByName(item.getItemMeta().getLore().get(0));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
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
	}
}