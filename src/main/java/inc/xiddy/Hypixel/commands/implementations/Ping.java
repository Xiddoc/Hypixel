package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;

@CommandInfo(name = "ping", minArgCount = 0)
public class Ping extends HypixelCommand {
	@Override
	public void execute(HypixelPlayer player, String[] args) {
		player.sendMessage(
			ChatColor.GREEN + "Your ping is " +
				ChatColor.GOLD + ChatColor.BOLD + HypixelUtils.getPlayerPing(player) + "ms" +
				ChatColor.GREEN + "!"
		);
	}
}
