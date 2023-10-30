package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.bedwars.BedwarsGame;
import inc.xiddy.hypixel.games.hide_n_seek.CatchGame;
import org.bukkit.ChatColor;

@CommandInfo(name = "startgame", minArgCount = 1)
@SuppressWarnings("unused")
public class StartGame extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		String gameName = args[0].toLowerCase();

		// Check if player already in a game
		if (!player.getLobby().equals(Lobby.HUB)) {
			// You can't do that! Must be in lobby.
			player.sendMessage(
				ChatColor.DARK_RED +
					"Wait until your game is finished to make a new game (or stop the current game).");
			return;
		}

		// Add it to the game list
		try {
			Main.getMainHandler().getGameHandler().addGame(this.getGameByName(gameName));
		} catch (IllegalArgumentException e) {
			player.sendMessage(ChatColor.DARK_RED + e.getMessage());
		}
	}

	public HypixelGame getGameByName(String name) throws IllegalArgumentException {
		switch (name) {
			case "bedwars":
				return new BedwarsGame(Lobby.BEDWARS);
			case "catch":
				return new CatchGame(Lobby.CATCH);
			case "sumo":
				return new BedwarsGame(Lobby.SUMO);
		}

		throw new IllegalArgumentException("Invalid game, could not find game named '" + name + "'.");
	}
}
