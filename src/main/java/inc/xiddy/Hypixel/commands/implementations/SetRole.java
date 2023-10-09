package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.server.Permission;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;

import java.util.UUID;

@CommandInfo(name = "setrole", minArgCount = 2, permission = "hypixel.admin")
@SuppressWarnings("unused")
public class SetRole extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
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
		Permission role;
		try {
			role = Permission.fromString(args[1]);
		} catch (IllegalArgumentException ignored) {
			// Throw error
			player.sendMessage(ChatColor.DARK_RED + "Invalid role.");
			return;
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
