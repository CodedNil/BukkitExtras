package codednil.BukkitExtras.Farming;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import codednil.BukkitExtras.Main.BukkitExtras;
import codednil.BukkitExtras.Main.Compat;
import codednil.BukkitExtras.Main.Util;

@SuppressWarnings("deprecation")
public class BetterHoe implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void BlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (!player.hasPermission("bukkitextras.modules.betterhoe.harvest"))
			return;
		if (!Util.isA(item.getType(), "Hoe"))
			return;
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		ItemStack handItem = player.getItemInHand();
		event.setCancelled(true);
		for (int i = 0; i < 9; i++) {
			Location loc = block.getLocation();
			Util.addLoc(loc, i);
			Block cropBlock = loc.getBlock();
			Block belowBlock = Util.getBelow(cropBlock.getLocation())
					.getBlock();
			Block aboveBlock = Util.getAbove(cropBlock.getLocation())
					.getBlock();
			if (belowBlock.getType() == Material.SUGAR_CANE_BLOCK) {
				Location locb = cropBlock.getLocation();
				Location lowest = locb;
				for (int x = 0; x < 5; x++) {
					locb.subtract(0, 1, 0);
					Block newBlock = locb.getBlock();
					if (newBlock.getType() != Material.SUGAR_CANE_BLOCK) {
						lowest = locb;
						lowest.add(0, 2, 0);
						break;
					}
				}
				if (Compat
						.playerCanBuild(player, locb.getBlock().getLocation()))
					locb.getBlock().breakNaturally();
			} else if (aboveBlock.getType() == Material.SUGAR_CANE_BLOCK) {
				if (Compat.playerCanBuild(player, aboveBlock.getLocation()))
					aboveBlock.breakNaturally();
			} else {
				if (Compat.playerCanBuild(player, cropBlock.getLocation())) {
					if (Util.isA(cropBlock.getType(), "Crop")) {
						if (cropBlock.getData() == 7) {
							cropBlock.breakNaturally(handItem);
							if (Math.round(Math.random() * 2) == 0)
								Util.damageItem(player, handItem, player
										.getInventory().getHeldItemSlot());
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public static void growCrop(Block block, Player player) {
		if (!player.hasPermission("bukkitextras.modules.betterhoe.grow"))
			return;
		for (int i = 0; i < 9; i++) {
			Location loc = block.getLocation();
			Util.addLoc(loc, i);
			Block cropBlock = loc.getBlock();
			if (Compat.playerCanBuild(player, cropBlock.getLocation())) {
				if (Util.isA(cropBlock.getType(), "GrowCrop")) {
					if (cropBlock.getData() != 7) {
						int bonemealuse = (int) Math.round((7 - cropBlock
								.getData()) / 5 + 0.5);
						if (player.getGameMode() == GameMode.CREATIVE) {
							cropBlock.setData((byte) 7);
						} else if (player.getInventory().containsAtLeast(
								BukkitExtras.Plugin.BONE_MEAL, bonemealuse)) {
							Util.removeItem(player,
									BukkitExtras.Plugin.BONE_MEAL, bonemealuse);
							cropBlock.setData((byte) 7);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public static void plantCrop(Block block, Player player) {
		if (!player.hasPermission("bukkitextras.modules.betterhoe.plant"))
			return;
		PlayerInventory inventory = player.getInventory();
		int nextSlot = inventory.getHeldItemSlot() + 1;
		ItemStack seed = inventory.getItem(nextSlot);
		if (seed == null)
			return;
		Material seedBlock = Util.getBlockFromItem(seed.getType());
		if (!Util.isA(seed.getType(), "Seed"))
			return;
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		if (seedBlock == null)
			return;
		for (int i = 0; i < 9; i++) {
			Location loc = block.getLocation();
			Util.addLoc(loc, i);
			Block cropBlock = loc.getBlock();
			if (Compat.playerCanBuild(player, cropBlock.getLocation())) {
				if (cropBlock.getType() == Material.SOIL) {
					Location above = Util.getAbove(loc);
					Block aboveBlock = above.getBlock();
					if (aboveBlock.isEmpty()) {
						Util.removeItem(player, seed, 1);
						aboveBlock.setType(seedBlock);
						aboveBlock.setData((byte) 0);
					}
				}
			}
		}
	}
}