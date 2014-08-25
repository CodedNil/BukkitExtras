package codednil.BukkitExtras.Wands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import codednil.BukkitExtras.Main.Compat;
import codednil.BukkitExtras.Main.Util;

public class Trading implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (block == null)
			return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getItem() == null)
			return;
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
}