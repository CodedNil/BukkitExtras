package main;

import io.github.elitejynx.BukkitProtect.BukkitProtect;
import io.github.elitejynx.BukkitProtect.ProtectionZone;
import io.github.elitejynx.BukkitProtect.UserType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Compat {
	public static void initialize() {
		BukkitProtect.Plugin.addUserType(new UserType("Farmer",
				"Allows the use of hoes", Material.STAINED_CLAY, 8,
				Material.STAINED_CLAY, 0, 1, false));
		BukkitProtect.Plugin.addUserType(new UserType("Brewer",
				"Allows the use of brewing stands and adjacent chests",
				Material.STAINED_CLAY, 8, Material.STAINED_CLAY, 0, 1, false));
		BukkitProtect.Plugin.addUserType(new UserType("Trader",
				"Allows the use of villagers", Material.STAINED_CLAY, 8,
				Material.STAINED_CLAY, 0, 1, false));
	}

	public static boolean playerCanUse(Player player, String Type,
			Location location) {
		if (BukkitExtras.Plugin.serverHasPlugin("BukkitProtect")) {
			ProtectionZone zone = BukkitProtect.Plugin
					.isInsideProtection(location);
			if (io.github.elitejynx.BukkitProtect.Util.parseUserType(Type) == null)
				return false;
			if (zone == null
					|| zone.userHasType(player.getName(),
							io.github.elitejynx.BukkitProtect.Util
									.parseUserType(Type)))
				return true;
			return false;
		}
		return true;
	}

	public static boolean playerCanBuild(Player player, Location location) {
		return playerCanUse(player, "BuildBlocks", location);
	}

	public static boolean playerCanAccess(Player player, Location location) {
		return playerCanUse(player, "Access", location);
	}

	public static boolean playerCanUseBlocks(Player player, Location location) {
		return playerCanUse(player, "UseBlocks", location);
	}

	public static boolean playerCanFarm(Player player, Location location) {
		return playerCanUse(player, "Farmer", location)
				|| playerCanUse(player, "BuildBlocks", location);
	}

	public static boolean playerCanBrew(Player player, Location location) {
		return playerCanUse(player, "Brewer", location)
				|| playerCanUse(player, "UseBlocks", location);
	}

	public static boolean playerCanTrade(Player player, Location location) {
		return playerCanUse(player, "Trader", location)
				|| playerCanUse(player, "Entities", location);
	}
}