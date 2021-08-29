package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SaveLocation extends HypixelCommand {

	public SaveLocation(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
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
				"other\\" + Main.getMainHandler().getDataHandler().stripSlashes(args[0]),
				loc
			);
		}
	}
}
