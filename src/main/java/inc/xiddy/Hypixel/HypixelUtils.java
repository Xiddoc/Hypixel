package inc.xiddy.Hypixel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class HypixelUtils {
	public static void sendTitle(Player player, String title, String subtitle, double stayIn, double fadeIn, double fadeOut) {
		sendTitle(player, title, subtitle, (int) (stayIn * 20), (int) (fadeIn * 20), (int) (fadeOut * 20));
	}

	public static void sendTitle(Player player, String title, String subtitle, int stayInTicks, int fadeInTicks, int fadeOutTicks) {
		// Set animation time
		sendCommand("title " + player.getDisplayName() + " times " + fadeInTicks + " " + stayInTicks + " " + fadeOutTicks);
		// Set title
		sendCommand("title " + player.getDisplayName() + " title \"" + title.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"");
		// Set subtitle
		sendCommand("title " + player.getDisplayName() + " subtitle \"" + subtitle.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"");
	}

	public static void sendCommand(String command) {
		Main.getInstance().getServer().dispatchCommand(
			Main.getInstance().getServer().getConsoleSender(),
			command
		);
	}

	public static String capitalize(String word) {
		return word.substring(0, 1).toUpperCase() + word.toLowerCase().substring(1);
	}

	public static String capitalize(String[] words) {
		// New SB
		StringBuilder result = new StringBuilder();
		// For each word
		for (String word: words) {
			// Capitalize it
			result.append(capitalize(word)).append(" ");
		}
		// Return the string
		return result.substring(0, result.length() - 1);
	}

	public static Player getPlayerGlobal(String player) {
		// Get player
		Player targetOnline = Bukkit.getPlayer(player);
		Player targetOffline = Bukkit.getOfflinePlayer(player).getPlayer();
		return XORReturn(targetOnline, targetOffline);
	}

	public static Player getPlayerGlobal(UUID player) {
		// Get player
		Player targetOnline = Bukkit.getPlayer(player);
		Player targetOffline = Bukkit.getOfflinePlayer(player).getPlayer();
		return XORReturn(targetOnline, targetOffline);
	}

	private static <T> T XORReturn(T targetOnline, T targetOffline) {
		// If player has never logged on
		if (targetOnline == null && targetOffline == null) {
			// Return null
			return null;
		} else {
			// If they are online
			if (targetOnline != null) {
				// Return that player
				return targetOnline;
			} else {
				// Otherwise, return the offline player
				return targetOffline;
			}
		}
	}
}
