package main;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import wands.WandHandler;

public class BukkitExtras extends JavaPlugin implements Listener {
	public static BukkitExtras Plugin;

	@Override
	public void onEnable() {
		Plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
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

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerTeleport(PlayerTeleportEvent event) {
		Events.PlayerTeleport(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerPortal(PlayerPortalEvent event) {
		Events.PlayerPortal(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void EnchantItem(EnchantItemEvent event) {
		EnchantmentHandler.EnchantEvent(event.getEnchanter(), event.getItem(),
				event.getExpLevelCost());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void CreatureSpawn(CreatureSpawnEvent event) {
		Events.CreatureSpawn(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void ShearEntity(PlayerShearEntityEvent event) {
		Events.ShearEntity(event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void BlockBreak(BlockBreakEvent event) {
		Events.BlockBreak(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void BlockPlace(BlockPlaceEvent event) {
		Events.BlockPlace(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerSneak(PlayerToggleSneakEvent event) {
		Events.PlayerSneak(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerMove(PlayerMoveEvent event) {
		Events.PlayerMove(event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void Interact(PlayerInteractEvent event) {
		Events.PlayerInteract(event);
		Shops.PlayerInteract(event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void InventoryClose(InventoryCloseEvent event) {
		Shops.InventoryClose(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void InventoryClick(InventoryClickEvent event) {
		Shops.InventoryClick(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void InventoryDrag(InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Inventory inventory = event.getInventory();
		Set<Integer> slots = event.getRawSlots();
		if (!inventory.getTitle().startsWith("Shop"))
			return;
		for (int i = 0; i < slots.size(); i++)
			if ((int) (slots.toArray()[i]) < 9)
				event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void DropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		InventoryView inventory = player.getOpenInventory();
		if (!inventory.getTitle().startsWith("Shop"))
			return;
		event.setCancelled(true);
		player.updateInventory();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		InventoryView inventory = player.getOpenInventory();
		if (!inventory.getTitle().startsWith("Shop"))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void InteractEntity(PlayerInteractEntityEvent event) {
		Events.PlayerInteractEntity(event);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return Events.onCommand(sender, cmd, args);
	}
}