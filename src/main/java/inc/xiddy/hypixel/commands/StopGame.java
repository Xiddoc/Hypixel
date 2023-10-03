package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.ChatColor;

import java.util.List;

public class StopGame extends HypixelCommand {

	public StopGame(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public boolean execute(HypixelPlayer player, String[] args) {
		// Get games of this type
		List<HypixelGame> games = Main.getMainHandler().getGameHandler().getAllGames();

		// If there are no games running
		if (games.isEmpty()) {
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "No games running.");
			return true;
		}

		// CONVERT TO ARRAY SO THAT THE LIST OBJECT DOES NOT CHANGE DURING THE LOOP
		for (HypixelGame game: games.toArray(new HypixelGame[0])) {
			// If OP is in the game
			if (game.getRunnableGame().getPlayers().contains(player)) {
				// Stop the game
				game.stopGame();
			}
		}

		return true;
	}
}
