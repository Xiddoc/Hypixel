package inc.xiddy.Hypixel.Games.Catch;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CatchRadar {
	private final String[][] font = {
		{
			"   ___   ",
			"  / _ \\  ",
			" | | | | ",
			" | | | | ",
			" | |_| | ",
			"  \\___/  "
		},
		{
			"  __     ",
			" /_ |    ",
			"  | |    ",
			"  | |    ",
			"  | |    ",
			"  |_|    "
		},
		{
			"  ___    ",
			" |__ \\   ",
			"    ) |  ",
			"   / /   ",
			"  / /_   ",
			" |____|  "
		},
		{
			"  ____   ",
			" |___ \\  ",
			"   __) | ",
			"  |__ <  ",
			"  ___) | ",
			" |____/  "
		},
		{
			"  _  _   ",
			" | || |  ",
			" | || |_ ",
			" |__   _|",
			"    | |  ",
			"    |_|  "
		},
		{
			"  _____  ",
			" | ____| ",
			" | |__   ",
			" |___ \\  ",
			"  ___) | ",
			" |____/  "
		},
		{
			"    __   ",
			"   / /   ",
			"  / /_   ",
			" | '_ \\  ",
			" | (_) | ",
			"  \\___/  "
		},
		{
			"  ______ ",
			" |____  |",
			"     / / ",
			"    / /  ",
			"   / /   ",
			"  /_/    "
		},
		{
			"   ___   ",
			"  / _ \\  ",
			" | (_) | ",
			"  > _ <  ",
			" | (_) | ",
			"  \\___/  "
		},
		{
			"   ___   ",
			"  / _ \\  ",
			" | (_) | ",
			"  \\__, | ",
			"    / /  ",
			"   /_/   "
		}
	};

	public CatchRadar() {}

	public String getRadar(int distance) {
		// Init
		StringBuilder radar = new StringBuilder();
		// Get string of distance
		char[] distanceString = String.valueOf(distance).toCharArray();

		// For each row
		for (int row = 0; row < 6; row ++) {
			// For each character
			for (char number: distanceString) {
				// Get the font row for that character
				radar.append(this.font[Integer.parseInt(String.valueOf(number))][row]);
			}
			// Newline
			radar.append("\n");
		}
		// Return
		return radar.toString();
	}

	public void printRadar(Player player, int distance) {
		// Switch case for color
		ChatColor color;
		if (distance > 50) {
			// Set color
			color = ChatColor.WHITE;
		} else if (distance > 15) {
			// Set color
			color = ChatColor.GOLD;
		} else {
			// Set color
			color = ChatColor.RED;
		}

		// Print radar to player
		player.sendMessage(color + this.getRadar(distance));
	}
}
