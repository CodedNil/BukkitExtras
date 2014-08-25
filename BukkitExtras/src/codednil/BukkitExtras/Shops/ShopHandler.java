package codednil.BukkitExtras.Shops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import codednil.BukkitExtras.Main.BukkitExtras;
import codednil.BukkitExtras.Main.Util;

public class ShopHandler {
	// Todo rewrites
	// Stockpiles, just a sign with [Stockpile] written on it
	// Shops take all inventory from owners stockpile
	// Shops give all inventory to owners stockpile
	// Shops store data, and open as a custom gui

	static Map<Player, Block> shopsOpen = new HashMap<Player, Block>();

	public static void updateInventory(Inventory inventory, Player player,
			Block signBlock) {
		Block chestBlock = Util.findNearest(signBlock.getLocation(),
				Material.CHEST, -1, 5);
		if (chestBlock == null)
			return;
		Block stockBlock = Util.findNearest(chestBlock.getLocation(),
				Material.CHEST, -1, 5);
		if (stockBlock == null)
			return;
		Chest chest = (Chest) chestBlock.getState();
		Inventory chestInv = chest.getBlockInventory();
		Chest stock = (Chest) stockBlock.getState();
		Inventory stockInv = stock.getBlockInventory();
		for (int i = 0; i < 9; i++) {
			ItemStack item = chestInv.getItem(i);
			ItemStack costItem = chestInv.getItem(i + 9);
			if (item != null) {
				inventory.setItem(i, item);
				item = inventory.getItem(i);
				ItemMeta itemMeta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if (itemMeta.hasLore())
					lore = itemMeta.getLore();
				String price = "N/A";
				double inStock = 0;
				if (costItem != null) {
					String itemName = costItem.getType().toString();
					itemName = itemName.replaceAll("_", " ");
					itemName = WordUtils.capitalizeFully(itemName);
					if (costItem.hasItemMeta())
						if (costItem.getItemMeta().hasDisplayName())
							itemName = costItem.getItemMeta().getDisplayName();
					String amount = String.valueOf(costItem.getAmount());
					price = itemName + " x " + amount;
				}
				for (int x = 0; x < stockInv.getSize(); x++) {
					ItemStack stockItem = stockInv.getItem(x);
					if (stockItem != null) {
						if (stockItem.isSimilar(item))
							inStock += Double.valueOf(stockItem.getAmount())
									/ Double.valueOf(item.getAmount());
					}
				}
				int inStockInt = (int) Math.round(inStock - 0.5);
				boolean canPurchase = true;
				if (costItem != null)
					if (player.getInventory().containsAtLeast(costItem,
							costItem.getAmount()))
						lore.add("§cPrice: " + "§2" + price);
					else {
						lore.add("§cPrice: " + "§4" + price);
						canPurchase = false;
					}
				else {
					lore.add("§cPrice: " + "§4" + price);
					canPurchase = false;
				}
				if (inStockInt != 0)
					lore.add("§cStock: " + "§2" + inStockInt);
				else {
					lore.add("§cStock: " + "§4" + inStockInt);
					canPurchase = false;
				}
				if (canPurchase)
					lore.add("§2Can purchase");
				else
					lore.add("§4Cannot purchase");
				itemMeta.setLore(lore);
				item.setItemMeta(itemMeta);
			}
		}
	}

	public static void PlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (!player.hasPermission("bukkitextras.shops"))
			return;
		if (block == null)
			return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Material blocktype = block.getType();
		if (blocktype != Material.WALL_SIGN)
			return;
		Sign sign = (Sign) block.getState();
		if (!sign.getLine(0).equalsIgnoreCase("[shop]"))
			return;
		event.setCancelled(true);
		Location loc = sign.getLocation();
		String locStr = loc.getBlockX() + "," + loc.getBlockY() + ","
				+ loc.getBlockZ();
		shopsOpen.put(player, block);
		Inventory inventory = BukkitExtras.Plugin.createInventory(null, 9,
				"Shop " + locStr);
		updateInventory(inventory, player, block);
		player.openInventory(inventory);
	}

	public static void InventoryClose(InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player))
			return;
		Player player = (Player) event.getPlayer();
		shopsOpen.remove(player);
	}

	public static void InventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Inventory inventory = event.getInventory();
		ItemStack item = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		if (inventory.getHolder() instanceof Chest) {
			Block chestBlock = ((Chest) inventory.getHolder()).getBlock();
			Block signBlock = Util.findNearest(chestBlock.getLocation(),
					Material.WALL_SIGN, 1, 10);
			if (signBlock == null)
				return;
			for (Map.Entry<Player, Block> entry : shopsOpen.entrySet())
				if (entry.getValue().equals(signBlock))
					updateInventory(entry.getKey().getOpenInventory()
							.getTopInventory(), entry.getKey(), signBlock);
			return;
		}
		if (!inventory.getTitle().startsWith("Shop"))
			return;
		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			event.setCancelled(true);
			return;
		}
		if (slot > 8)
			return;
		event.setCancelled(true);
		if (event.getAction() != InventoryAction.PICKUP_ALL
				&& event.getAction() != InventoryAction.SWAP_WITH_CURSOR)
			return;
		if (!item.hasItemMeta())
			return;
		ItemMeta itemMeta = item.getItemMeta();
		if (!itemMeta.hasLore())
			return;
		List<String> lore = itemMeta.getLore();
		if (!lore.get(lore.size() - 1).equals("§2Can purchase"))
			return;
		String locStr = inventory.getTitle().split(" ")[1];
		int x = Integer.parseInt(locStr.split(",")[0]);
		int y = Integer.parseInt(locStr.split(",")[1]);
		int z = Integer.parseInt(locStr.split(",")[2]);
		Location loc = new Location(player.getWorld(), x, y, z);
		if (loc.getBlock() == null)
			return;
		if (loc.getBlock().getType() != Material.WALL_SIGN) {
			player.closeInventory();
			return;
		}
		Block chestBlock = Util.findNearest(loc, Material.CHEST, -1, 5);
		if (chestBlock == null) {
			player.closeInventory();
			return;
		}
		Block stockBlock = Util.findNearest(chestBlock.getLocation(),
				Material.CHEST, -1, 5);
		if (stockBlock == null) {
			player.closeInventory();
			return;
		}
		Chest chest = (Chest) chestBlock.getState();
		Inventory chestInv = chest.getBlockInventory();
		Chest stock = (Chest) stockBlock.getState();
		Inventory stockInv = stock.getBlockInventory();
		ItemStack chestItem = chestInv.getItem(slot);
		ItemStack chestPrice = chestInv.getItem(slot + 9);
		if (chestItem == null || chestPrice == null)
			return;
		if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR)
			if (!player.getItemOnCursor().isSimilar(chestItem))
				return;
		if (Util.spaceInInventory(player.getInventory())) {
			if (!Util.spaceInInventory(stockInv)) {
				player.sendMessage("Payment chest full");
				player.closeInventory();
				return;
			}
			ItemStack cursor = chestItem.clone();
			int amount = cursor.getAmount();
			int newamount = player.getItemOnCursor().getAmount();
			if (player.getItemOnCursor() != null)
				if (amount + newamount <= cursor.getMaxStackSize())
					cursor.setAmount(amount + newamount);
				else
					return;
			player.setItemOnCursor(cursor);
			Util.removeItem(player, chestPrice, chestPrice.getAmount());
			stockInv.addItem(chestPrice);
			int stockLeft = chestItem.getAmount();
			for (int i = 0; i < stockInv.getSize(); i++) {
				ItemStack stockItem = stockInv.getItem(i);
				if (stockItem != null)
					if (stockItem.isSimilar(chestItem)) {
						int toRemove = stockLeft;
						stockLeft = Math.max(0,
								stockLeft - stockItem.getAmount());
						toRemove -= stockLeft;
						if (toRemove >= stockItem.getAmount())
							stockInv.setItem(i, null);
						else
							stockItem.setAmount(stockItem.getAmount()
									- toRemove);
					}
			}
			for (Map.Entry<Player, Block> entry : shopsOpen.entrySet())
				if (entry.getValue().equals(loc.getBlock()))
					updateInventory(entry.getKey().getOpenInventory()
							.getTopInventory(), entry.getKey(), loc.getBlock());
		} else {
			player.sendMessage("Your inventory is full");
			player.closeInventory();
		}
	}
}