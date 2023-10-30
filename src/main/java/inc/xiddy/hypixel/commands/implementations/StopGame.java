package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.ChatColor;

import java.util.List;

@CommandInfo(name = "stopgame", minArgCount = 0, permission = "hypixel.admin")
@SuppressWarnings("unused")
public class StopGame extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Get games of this type
		List<HypixelGame> games = Main.getMainHandler().getGameHandler().getAllGames();

		// If there are no games running
		if (games.isEmpty()) {
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "No games running.");
			return;
		}

		try {
			getGameThatContainsPlayer(player, games).stopGame();
		} catch (RuntimeException e) {
			player.sendMessage(ChatColor.DARK_RED + "You are not in a game.");
		}
	}

	private HypixelGame getGameThatContainsPlayer(HypixelPlayer player, List<HypixelGame> games) throws RuntimeException {
		for (HypixelGame game: copyGames(games)) {
			if (doesGameContainPlayer(player, game)) {
				game.stopGame();
			}
		}

		throw new RuntimeException("Player is not in a game.");
	}

	private boolean doesGameContainPlayer(HypixelPlayer player, HypixelGame game) {
		//noinspection SuspiciousMethodCalls
		return game.getRunnableGame().getPlayers().contains(player);
	}

	private HypixelGame[] copyGames(List<HypixelGame> games) {
		// Convert to an array, so that the List does not change (size) during the loop
		return games.toArray(new HypixelGame[0]);
	}
}
