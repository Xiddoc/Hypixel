package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@CommandInfo(name = "setupbedwars", minArgCount = 2, permission = "hypixel.builder")
@SuppressWarnings({"unused"})
public class BedwarsSetup extends HypixelCommand {
	private final List<String> subcommands = Arrays.asList(
		"setgen",
		"setitemshop",
		"setteamshop",
		"setbed",
		"setrespawn",
		"setdiamondgen1",
		"setdiamondgen2",
		"setdiamondgen3",
		"setdiamondgen4",
		"setemeraldgen1",
		"setemeraldgen2",
		"setemeraldgen3",
		"setemeraldgen4"
	);

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Lowercase the arguments for simplicity
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].toLowerCase();
		}

		// Case for which subcommand to run
		String fileNickname = null;
		if (this.subcommands.contains(args[0])) {
			fileNickname = args[0].substring(3);
		} else {
			player.sendMessage(ChatColor.DARK_RED + "Unregistered subcommand.");
		}

		// If one of the commands was valid
		if (fileNickname != null) {
			// Get player's location
			SmallLocation loc = new SmallLocation(player.getLocation());
			// Round the location
			loc = SmallLocation.center(loc);
			// Save to file
			// If this is a generator
			if (args[1].equals("global")) {
				// Save location
				Main.getMainHandler().getDataHandler().write("bedwars/maps/new_map/global/" + fileNickname + ".json", loc);
				// Update player
				player.sendMessage(ChatColor.GREEN + fileNickname + " [Global] location saved.");
			} else {
				// Otherwise,
				// Assure that the team is valid
				TeamColor team;
				if (!TeamColor.contains(args[1])) {
					player.sendMessage(ChatColor.DARK_RED + "Invalid team color.");
					return;
				} else {
					team = TeamColor.fromString(args[1]);
				}
				// If this is a team location
				Main.getMainHandler().getDataHandler().write("bedwars/maps/new_map/" + team + "/" + fileNickname + ".json", loc);
				// Update player
				player.sendMessage(ChatColor.GREEN + fileNickname + " [" + team.getCapitalizedString() + "] location saved.");
			}
		}
	}
}
