package codednil.BukkitExtras.Main;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import codednil.BukkitExtras.Commands.CommandHandler;
import codednil.BukkitExtras.Farming.AnimalTweaks;
import codednil.BukkitExtras.Farming.BetterHoe;
import codednil.BukkitExtras.Miscellaneous.Elevators;
import codednil.BukkitExtras.Miscellaneous.SilkSpawners;
import codednil.BukkitExtras.Wands.Trading;
import codednil.BukkitExtras.Wands.WandHandler;

public class BukkitExtras extends JavaPlugin implements Listener {
	public static BukkitExtras Plugin;
	public Material elevatorBlock = Material.LAPIS_BLOCK;
	public ItemStack BONE_MEAL = new ItemStack(Material.INK_SACK, 1, (short) 15);

	@Override
	public void onEnable() {
		Plugin = this;

		getServer().getPluginManager().registerEvents(new BetterHoe(), this);
		getServer().getPluginManager().registerEvents(new AnimalTweaks(), this);
		getServer().getPluginManager().registerEvents(new Elevators(), this);
		getServer().getPluginManager().registerEvents(new Trading(), this);
		getServer().getPluginManager().registerEvents(new SilkSpawners(), this);

		final ShapelessRecipe nameTag = new ShapelessRecipe(new ItemStack(
				Material.NAME_TAG, 1));
		nameTag.addIngredient(Material.STRING);
		nameTag.addIngredient(Material.PAPER);
		getServer().addRecipe(nameTag);

		final ShapedRecipe saddle = new ShapedRecipe(new ItemStack(
				Material.SADDLE));
		saddle.shape("LLL", "S S", "I I");
		saddle.setIngredient('L', Material.LEATHER);
		saddle.setIngredient('S', Material.STRING);
		saddle.setIngredient('I', Material.IRON_INGOT);
		getServer().addRecipe(saddle);

		WandHandler.registerWand("Trading", Material.EMERALD);

		this.saveDefaultConfig();

		String elevatorConfig = getConfig().getString("ElevatorBlock")
				.toUpperCase().replace(" ", "_");
		if (Material.valueOf(elevatorConfig) != null) {
			elevatorBlock = Material.valueOf(elevatorConfig);
		}

		new UpdateHandler(this, 69062, this.getFile(),
				UpdateHandler.UpdateType.DEFAULT, false);

		if (serverHasPlugin("BukkitProtect")
				&& getConfig().getBoolean("BukkitProtectCompat"))
			Compat.initialize();
	}

	public boolean serverHasPlugin(String text) {
		if (getServer().getPluginManager().isPluginEnabled(text))
			return true;
		return false;
	}

	public void registerRecipe(Recipe recipe) {
		getServer().addRecipe(recipe);
	}

	public Inventory createInventory(InventoryHolder owner, int size,
			String title) {
		return getServer().createInventory(owner, size, title);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return CommandHandler.commandInput(sender, cmd, args);
	}
}