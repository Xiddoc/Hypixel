package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import org.bukkit.ChatColor;

public class Ping extends HypixelCommand {

	public Ping(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Get ping from player connection
		// Send to player
		player.sendMessage(
			ChatColor.GREEN + "Your ping is " +
				ChatColor.GOLD + ChatColor.BOLD + player.getHandle().ping + "ms" +
				ChatColor.GREEN + "!"
		);
	}
}
