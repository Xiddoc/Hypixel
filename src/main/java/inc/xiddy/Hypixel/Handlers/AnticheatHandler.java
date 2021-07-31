package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.text.MessageFormat;

public class AnticheatHandler {
	private final int cpsCap;

	public AnticheatHandler(int cpsCap) {
		this.cpsCap = cpsCap;
	}

	public void onClick(PlayerInteractEvent event) {
		// Get player
		Player player = event.getPlayer();
		// Get action
		Action action = event.getAction();

		// If the player clicked
		switch (action) {
			// Left click
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK: {
				// Pass to player data
				Main.getMainHandler().getPlayerHandler().getPlayerData(player).leftClick(); break;
			}
			// Right click
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK: {
				// Pass to player data
				Main.getMainHandler().getPlayerHandler().getPlayerData(player).rightClick(); break;
			}
		}

		// If clicking too fast
		if (Main.getMainHandler().getPlayerHandler().getPlayerData(player).leftCPS() > this.getCpsCap()) {
			// Punish them
			this.punish(
				MessageFormat.format(
					"\n\n{0}Huh, {1} {0}just clicked really fast... too fast! ({2} Left-CPS)\n\n",
					ChatColor.DARK_RED,
					ChatColor.BOLD + player.getDisplayName(),
					Main.getMainHandler().getPlayerHandler().getPlayerData(player).leftCPS()
				)
			);
		} else if (Main.getMainHandler().getPlayerHandler().getPlayerData(player).rightCPS() > this.getCpsCap()) {
			// Punish them
			this.punish(
				MessageFormat.format(
					"\n\n{0}Huh, {1} {0}just clicked really fast... too fast! ({2} Right-CPS)\n\n",
					ChatColor.DARK_RED,
					ChatColor.BOLD + player.getDisplayName(),
					Main.getMainHandler().getPlayerHandler().getPlayerData(player).rightCPS()
				)
			);
		}
	}

	public void revokeLeftClick(Player player) {
		Main.getMainHandler().getPlayerHandler().getPlayerData(player).revokeLeftClick();
	}

	public void punish(String message) {
		// For each player online
		for (Player onlinePlayer: Main.getInstance().getServer().getOnlinePlayers()) {
			// Alert
			onlinePlayer.sendMessage(message);
		}
	}

	public int getCpsCap() {
		return cpsCap;
	}
}
