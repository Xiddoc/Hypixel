package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import inc.xiddy.hypixel.Main;
import org.bukkit.ChatColor;

public class SaveLocation extends HypixelCommand {

	public SaveLocation(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public boolean execute(HypixelPlayer player, String[] args) {
		// Make sure user gave arguments
		if (args.length == 0) {
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "No file name/path specified.");
		} else {
			// Get location
			SmallLocation loc = new SmallLocation(player.getLocation());
			// Center the location
			loc = SmallLocation.center(loc);
			// Write the location to the disk
			Main.getMainHandler().getDataHandler().write(
				"other/" + Main.getMainHandler().getDataHandler().stripSlashes(args[0]),
				loc
			);
		}
		return false;
	}
}
