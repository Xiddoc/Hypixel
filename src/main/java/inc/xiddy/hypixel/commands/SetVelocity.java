package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;

public class SetVelocity extends HypixelCommand {

	public SetVelocity(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public boolean execute(HypixelPlayer player, String[] args) {
		// Validate arguments
		if (args.length != 3) {
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
		} else {
			// Set player's velocity
			player.setVelocity(new Vector(
				Integer.parseInt(args[0]),
				Integer.parseInt(args[1]),
				Integer.parseInt(args[2])
			));
		}
		return false;
	}
}
