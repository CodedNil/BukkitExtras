package codednil.BukkitExtras.Main;

import io.github.elitejynx.BukkitProtect.BukkitProtect;
import io.github.elitejynx.BukkitProtect.ProtectionZone;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Compat {
	public static void initialize() {
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
}