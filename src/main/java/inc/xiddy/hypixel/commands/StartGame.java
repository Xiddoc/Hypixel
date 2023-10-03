package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.bedwars.BedwarsGame;
import inc.xiddy.hypixel.games.hide_n_seek.CatchGame;
import inc.xiddy.hypixel.Main;
import org.bukkit.ChatColor;

public class StartGame extends HypixelCommand {

	public StartGame(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		if (args.length != 1) {
			player.sendMessage(ChatColor.DARK_RED + "Command requires 1 argument.");
			return;
		}
		String gameName = args[0].toLowerCase();

		// Check if player already in a game
		if (!Main.getMainHandler().getPlayerHandler().getPlayerData(player).getLobby().equals(Lobby.HUB)) {
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
