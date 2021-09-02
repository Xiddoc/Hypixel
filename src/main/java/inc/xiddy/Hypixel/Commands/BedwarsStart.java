package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import inc.xiddy.Hypixel.Games.Bedwars.BedwarsGame;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;

public class BedwarsStart extends HypixelCommand {

	public BedwarsStart(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Check if player already in a game
		if (!Main.getMainHandler().getPlayerHandler().getPlayerData(player).getLobby().equals(Lobby.HUB)) {
			// You can't do that! Must be in lobby.
			player.sendMessage(ChatColor.DARK_RED + "Wait until your game is finished to make a new game (or stop the current game).");
		} else {
			// Add it to the game list
			Main.getMainHandler().getGameHandler().addGame(new BedwarsGame(Lobby.BEDWARS));
		}
	}
}
