package codednil.BukkitExtras.Farming;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import codednil.BukkitExtras.Main.BukkitExtras;

public class AnimalTweaks implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void CreatureSpawn(CreatureSpawnEvent event) {
		if (!BukkitExtras.Plugin.getConfig().getBoolean("MorePigs"))
			return;
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
		if (!event.getPlayer().hasPermission(
				"bukkitextras.modules.animaltweaks"))
			return;
		Entity entity = event.getEntity();
		if (entity instanceof Sheep)
			for (int i = 0; i < Math.random() * 3; i++)
				entity.getWorld().dropItemNaturally(entity.getLocation(),
						new ItemStack(Material.STRING));
	}
}