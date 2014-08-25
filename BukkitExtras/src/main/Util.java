package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {
	public static List<Material> Pickaxes = Arrays.asList(
			Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
			Material.IRON_PICKAXE, Material.GOLD_PICKAXE,
			Material.DIAMOND_PICKAXE);
	public static List<Material> Spades = Arrays.asList(Material.WOOD_SPADE,
			Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE,
			Material.DIAMOND_SPADE);
	public static List<Material> Axes = Arrays.asList(Material.WOOD_AXE,
			Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE,
			Material.DIAMOND_AXE);
	public static List<Material> Hoes = Arrays.asList(Material.WOOD_HOE,
			Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE,
			Material.DIAMOND_HOE);
	public static List<Material> Tools = joinLists(Pickaxes, Spades, Axes, Hoes);
	public static List<Material> MineTools = joinLists(Pickaxes, Spades);
	public static List<Material> GrowableCrops = Arrays.asList(Material.CROPS,
			Material.CARROT, Material.POTATO);
	public static List<Material> UngrowableCrops = Arrays.asList(
			Material.NETHER_WARTS, Material.SUGAR_CANE_BLOCK);
	public static List<Material> Crops = joinLists(GrowableCrops,
			UngrowableCrops);
	public static List<Material> CropSeeds = Arrays.asList(Material.SEEDS,
			Material.CARROT_ITEM, Material.POTATO_ITEM);

	@SafeVarargs
	public static List<Material> joinLists(List<Material>... args) {
		List<Material> list0 = new ArrayList<Material>();
		for (int i = 0; i < args.length; i++)
			for (int x = 0; x < args[i].size(); x++)
				list0.add(args[i].get(x));
		return list0;
	}

	public static boolean isA(Material object, String type) {
		List<Material> list = null;
		if (type.equalsIgnoreCase("pickaxe"))
			list = Pickaxes;
		else if (type.equalsIgnoreCase("spade"))
			list = Spades;
		else if (type.equalsIgnoreCase("axe"))
			list = Axes;
		else if (type.equalsIgnoreCase("hoe"))
			list = Hoes;
		else if (type.equalsIgnoreCase("tool"))
			list = Tools;
		else if (type.equalsIgnoreCase("minetool"))
			list = MineTools;
		else if (type.equalsIgnoreCase("growcrop"))
			list = GrowableCrops;
		else if (type.equalsIgnoreCase("crop"))
			list = Crops;
		else if (type.equalsIgnoreCase("seed"))
			list = CropSeeds;
		if (list == null)
			return false;
		for (int i = 0; i < list.size(); i++)
			if (object == list.get(i))
				return true;
		return false;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static Block findNearest(Location location, Material type,
			double direction, boolean needsSpace) {
		Location loc = location.getBlock().getLocation();
		for (int i = 0; i < 50; i++) {
			Location above = loc;
			above.setY(loc.getY() + direction);
			if (above.getBlock().getType() == type)
				if (needsSpace) {
					if (hasSpace(above))
						return loc.getBlock();
				} else
					return loc.getBlock();

		}
		return null;
	}

	public static Block findNearest(Location location, Material type,
			double direction, double distance) {
		Location loc = location.getBlock().getLocation();
		for (int i = 0; i < distance; i++) {
			Location above = loc;
			above.setY(loc.getY() + direction);
			if (above.getBlock().getType() == type)
				return loc.getBlock();

		}
		return null;
	}

	public static int getAllOfBlock(Location location, Material type,
			double direction, int distance) {
		Location loc = location.getBlock().getLocation();
		int total = 0;
		for (int i = 0; i < distance; i++) {
			Location above = loc;
			above.setY(loc.getY() + direction);
			if (above.getBlock().getType() == type)
				total++;
			else
				break;

		}
		return total;
	}

	public static Location getBelow(Location location) {
		Location loc = location.getBlock().getLocation();
		loc.setY(loc.getY() - 1);
		return loc;
	}

	public static Location getAbove(Location location) {
		Location loc = location.getBlock().getLocation();
		loc.setY(loc.getY() + 1);
		return loc;
	}

	public static boolean hasSpace(Location location) {
		Location loc = location.getBlock().getLocation();
		loc.setY(loc.getY() + 1);
		if (!loc.getBlock().isEmpty())
			return false;
		loc.setY(loc.getY() + 1);
		if (!loc.getBlock().isEmpty())
			return false;
		return true;
	}

	public static boolean hasAdjacent(Block block, Material material,
			boolean vert) {
		for (int i = 0; i < 4; i++) {
			Location loc = block.getLocation();
			addLoc(loc, i);
			Block adjBlock = loc.getBlock();
			if (adjBlock.getType() == material)
				return true;
		}
		if (getBelow(block.getLocation()).getBlock().getType() == material)
			return true;
		if (getAbove(block.getLocation()).getBlock().getType() == material)
			return true;
		return false;
	}

	public static Material getBlockFromItem(Material material) {
		if (material == Material.SEEDS)
			return Material.CROPS;
		if (material == Material.CARROT_ITEM)
			return Material.CARROT;
		if (material == Material.POTATO_ITEM)
			return Material.POTATO;
		if (material == Material.NETHER_STALK)
			return Material.NETHER_WARTS;
		if (material == Material.SUGAR_CANE)
			return Material.SUGAR_CANE_BLOCK;
		return null;
	}

	public static Material getSmeltedForm(Material material) {
		if (material == Material.COBBLESTONE)
			return Material.STONE;
		if (material == Material.IRON_ORE)
			return Material.IRON_INGOT;
		if (material == Material.GOLD_ORE)
			return Material.GOLD_INGOT;
		if (material == Material.SAND)
			return Material.GLASS;
		return null;
	}

	public static void addLoc(Location loc, int i) {
		if (i == 0)
			loc.add(-1, 0, 0);
		else if (i == 1)
			loc.add(0, 0, -1);
		else if (i == 2)
			loc.add(0, 0, 1);
		else if (i == 3)
			loc.add(1, 0, 0);
		else if (i == 4)
			loc.add(0, 0, 0);
		else if (i == 5)
			loc.add(-1, 0, 1);
		else if (i == 6)
			loc.add(1, 0, -1);
		else if (i == 7)
			loc.add(-1, 0, -1);
		else if (i == 8)
			loc.add(1, 0, 1);
	}

	public static void removeOneOf(Player player) {
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		if (player.getItemInHand().getAmount() == 1)
			player.setItemInHand(null);
		else
			player.getItemInHand().setAmount(
					player.getItemInHand().getAmount() - 1);
	}

	public static boolean spaceInInventory(Inventory inventory) {
		return inventory.firstEmpty() != -1;
	}

	@SuppressWarnings("deprecation")
	public static void removeItem(Player player, ItemStack itemstack, int amount) {
		Inventory inventory = player.getInventory();
		for (int i = 0; i < amount; i++)
			for (int x = 0; x < inventory.getSize(); x++) {
				ItemStack item = inventory.getItem(x);
				if (item != null)
					if ((item.getType() == itemstack.getType())
							&& (item.getDurability() == itemstack
									.getDurability())) {
						if (item.getAmount() == 1)
							inventory.clear(x);
						else
							item.setAmount(item.getAmount() - 1);
						player.updateInventory();
						break;
					}
			}
	}

	public static void damageItem(Player player, ItemStack item, int slot) {
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
		double damageChance = Math.round(Math.random() * level);
		if (damageChance == 0)
			if (item.getDurability() == item.getType().getMaxDurability()) {
				player.getInventory().clear(slot);
				player.getWorld().playSound(player.getLocation(),
						Sound.ITEM_BREAK, 1, 1);
			} else
				item.setDurability((short) (item.getDurability() + 1));
	}

	public static int multiplyByFortune(int level) {
		if (level == 1) {
			double random = Math.round(Math.random() * 2);
			if (random == 0)
				return 2;
		} else if (level == 2) {
			double random = Math.round(Math.random() * 3);
			if (random == 0)
				return 2;
			random = Math.round(Math.random() * 3);
			if (random == 0)
				return 3;
		} else if (level == 2) {
			double random = Math.round(Math.random() * 4);
			if (random == 0)
				return 2;
			random = Math.round(Math.random() * 4);
			if (random == 0)
				return 3;
			random = Math.round(Math.random() * 4);
			if (random == 0)
				return 4;
		}
		return level;
	}

	public static void giveItem(Player player, ItemStack itemDrop) {
		if (spaceInInventory(player.getInventory()))
			player.getInventory().addItem(itemDrop);
		else
			player.getWorld().dropItemNaturally(player.getLocation(), itemDrop);
	}
}
