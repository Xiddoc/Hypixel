package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import org.bukkit.ChatColor;

@CommandInfo(name = "savelocation", minArgCount = 1, permission = "hypixel.builder")
@SuppressWarnings("unused")
public class SaveLocation extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
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
	}
}
