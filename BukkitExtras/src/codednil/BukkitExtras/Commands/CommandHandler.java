package codednil.BukkitExtras.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {
	public static Boolean commandInput(CommandSender sender, Command cmd,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("givewand")) {
			GiveWand.runCommand(sender, cmd, args);
		}
		return false;
	}
}
