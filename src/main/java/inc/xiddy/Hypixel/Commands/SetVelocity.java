package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SetVelocity extends HypixelCommand {

	public SetVelocity(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
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
	}
}
