package codednil.BukkitExtras.Miscellaneous;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import codednil.BukkitExtras.Main.BukkitExtras;
import codednil.BukkitExtras.Main.Compat;
import codednil.BukkitExtras.Main.Util;

public class Elevators implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerSneak(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (!event.isSneaking())
			return;
		if (!event.getPlayer().hasPermission(
				"bukkitextras.modules.misc.elevators"))
			return;
		Block block = player.getLocation().getBlock()
				.getRelative(BlockFace.DOWN);
		if (block.getType() != BukkitExtras.Plugin.elevatorBlock)
			return;
		if (!Compat.playerCanAccess(player, block.getLocation()))
			return;
		Material elevatorBlock = BukkitExtras.Plugin.elevatorBlock;
		Block target = Util.findNearest(block.getLocation(), elevatorBlock, -1,
				true);
		if (target == null)
			return;
		Location playerLoc = player.getLocation();
		float yaw = playerLoc.getYaw();
		float pitch = playerLoc.getPitch();
		Location location = target.getLocation().add(new Vector(0.5, 1, 0.5));
		location.setYaw(yaw);
		location.setPitch(pitch);
		player.teleport(location);
		player.getWorld().playSound(target.getLocation(),
				Sound.ENDERMAN_TELEPORT, (float) 0.5, 1);
		player.getWorld().playEffect(target.getLocation().add(0, 1, 0),
				Effect.ENDER_SIGNAL, 1);
		player.getWorld().playEffect(block.getLocation().add(0, 1, 0),
				Effect.ENDER_SIGNAL, 1);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void PlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location movement = event.getTo().clone().subtract(event.getFrom());
		if (movement.getY() <= 0.4)
			return;
		Block block = player.getLocation().getBlock()
				.getRelative(BlockFace.DOWN);
		if (block.getType() != BukkitExtras.Plugin.elevatorBlock)
			return;
		if (!event.getPlayer().hasPermission("bukkitextras.elevators"))
			return;
		if (!Compat.playerCanAccess(player, block.getLocation()))
			return;
		Material elevatorBlock = BukkitExtras.Plugin.elevatorBlock;
		Block target = Util.findNearest(block.getLocation(), elevatorBlock, 1,
				true);
		if (target == null)
			return;
		Location playerLoc = player.getLocation();
		float yaw = playerLoc.getYaw();
		float pitch = playerLoc.getPitch();
		Location location = target.getLocation().add(new Vector(0.5, 1, 0.5));
		location.setYaw(yaw);
		location.setPitch(pitch);
		player.teleport(location);
		player.getWorld().playSound(target.getLocation(),
				Sound.ENDERMAN_TELEPORT, (float) 0.5, 1);
		player.getWorld().playEffect(target.getLocation().add(0, 1, 0),
				Effect.ENDER_SIGNAL, 1);
		player.getWorld().playEffect(block.getLocation().add(0, 1, 0),
				Effect.ENDER_SIGNAL, 1);
	}

}