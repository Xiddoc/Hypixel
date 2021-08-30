package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SaveLocation extends HypixelCommand {

	public SaveLocation(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
		// Get ping from player connection
		// Send to player
		player.sendMessage(
			ChatColor.GREEN + "Your ping is " +
				ChatColor.GOLD + ChatColor.BOLD + ((CraftPlayer) player).getHandle().ping + "ms" +
				ChatColor.GREEN + "!"
		);
	}
}
