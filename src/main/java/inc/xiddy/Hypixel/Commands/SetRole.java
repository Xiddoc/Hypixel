package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import inc.xiddy.Hypixel.Main;
import inc.xiddy.Hypixel.Utility.HypixelUtils;
import org.bukkit.ChatColor;

import java.util.UUID;

public class SetRole extends HypixelCommand {

	public SetRole(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Make sure argument has 2 parameters
		if (args.length != 2) {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Command requires 2 arguments.");
			return;
		}

		// Initialize targets
		HypixelPlayer target;
		// If user inputted a username
		if (args[0].length() <= 16) {
			// Get player
			target = HypixelUtils.getPlayerGlobal(args[0]);
		} else if (args[0].length() == 36) {
			// Otherwise, if the user inputted a UUID
			// Convert input string to UUID
			UUID uuid = UUID.fromString(args[0]);
			// Get player
			target = HypixelUtils.getPlayerGlobal(uuid);
		} else {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Invalid player name / UUID.");
			return;
		}

		// If they haven't logged on before
		if (target == null) {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Player has never logged on to the server and is not online.");
			return;
		}

		// Get the role
		Permission role = null;
		try {
			role = Permission.fromString(args[1]);
		} catch (IllegalArgumentException ignored) {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Invalid role.");
		}

		// If role is valid
		if (role != null) {
			// Set the role to the player
			Main.getMainHandler().getPlayerHandler().getPlayerData(target).setRole(role);
			// Send success message
			player.sendMessage(
				ChatColor.GREEN + "Role of " + target.getDisplayName() + " was changed to " +
					ChatColor.GOLD + role.getCapitalizedString() + ChatColor.GREEN + "."
			);
			// Send update message to target
			target.sendMessage(
				ChatColor.GREEN + "Your role was updated to " +
					ChatColor.GOLD + role.getCapitalizedString() + ChatColor.GREEN + "."
			);
		} else {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Invalid role / Error occurred.");
		}
	}
}
