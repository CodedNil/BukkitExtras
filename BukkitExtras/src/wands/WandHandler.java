package wands;

import java.util.ArrayList;
import java.util.List;

import main.BukkitExtras;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class WandHandler {
	public static List<ItemStack> wands = new ArrayList<ItemStack>();

	public static void registerWand(String name, Material material) {
		ItemStack wand = new ItemStack(Material.STICK);
		ItemMeta itemMeta = wand.getItemMeta();
		itemMeta.setDisplayName("Wand of " + name);
		itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
		wand.setItemMeta(itemMeta);
		wands.add(wand);
		final ShapedRecipe wandRecipe = new ShapedRecipe(wand);
		wandRecipe.shape("  G", " S ", "M  ");
		wandRecipe.setIngredient('G', Material.GLASS);
		wandRecipe.setIngredient('S', Material.STICK);
		wandRecipe.setIngredient('M', material);
		BukkitExtras.Plugin.registerRecipe(wandRecipe);
	}

	public static boolean isWand(ItemStack item) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		if (!item.getItemMeta().hasDisplayName())
			return false;
		if (!item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL))
			return false;
		if (item.getType() != Material.STICK)
			return false;
		return true;
	}

	public static boolean isWandOf(ItemStack item, String name) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		if (!item.getItemMeta().hasDisplayName())
			return false;
		if (!item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL))
			return false;
		if (item.getType() != Material.STICK)
			return false;
		if (!item.getItemMeta().getDisplayName()
				.equalsIgnoreCase("Wand of " + name))
			return false;
		return true;
	}
}