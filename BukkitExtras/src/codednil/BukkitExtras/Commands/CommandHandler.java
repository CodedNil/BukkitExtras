package codednil.BukkitExtras.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import codednil.BukkitExtras.Main.Util;
import codednil.BukkitExtras.Wands.WandHandler;

public class CommandHandler {
	public static List<String> commands = new ArrayList<String>();

	public static void registerCommand(String name) {
		commands.add(name);
	}

	public static Boolean commandInput(CommandSender sender, Command cmd,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("givewand")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length != 1) {
					player.sendMessage("Invalid number of arguments");
					return false;
				} else {
					for (int i = 0; i < WandHandler.wands.size(); i++) {
						ItemStack wand = WandHandler.wands.get(i);
						String wandName = wand.getItemMeta().getDisplayName()
								.replaceAll("Wand of ", "");
						if (args[0].equalsIgnoreCase(wandName)) {
							Util.giveItem(player, wand);
							return true;
						}
					}
					String message = "Wand not found, valid wands are: ";
					for (int i = 0; i < WandHandler.wands.size(); i++) {
						ItemStack wand = WandHandler.wands.get(i);
						String wandName = wand.getItemMeta().getDisplayName()
								.replaceAll("Wand of ", "");
						message = message.concat("§6" + wandName);
						if (i != WandHandler.wands.size() - 1)
							message = message.concat(", ");
					}
					player.sendMessage(message);
				}
				return true;
			}
		}
		return false;
	}
}
