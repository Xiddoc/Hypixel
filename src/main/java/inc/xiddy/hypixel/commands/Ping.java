package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.utility.HypixelUtils;
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
				ChatColor.GOLD + ChatColor.BOLD + HypixelUtils.getPlayerPing(player) + "ms" +
				ChatColor.GREEN + "!"
		);
	}
}
