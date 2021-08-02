package inc.xiddy.Hypixel.Commands;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.HypixelCommand;
import inc.xiddy.Hypixel.Dataclasses.HypixelGame;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class BedwarsGameStop extends HypixelCommand {

	public BedwarsGameStop(String commandName, Permission permission) {
		super(commandName, permission);
	}

	@Override
	public void execute(Player player, String[] args) {
		List<HypixelGame> games = Main.getMainHandler().getGameHandler().getGames(Lobby.BEDWARS);
		if (games.isEmpty()) {
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "No games running.");
		} else {
			// For each game
			for (HypixelGame game : games.toArray(new HypixelGame[0])) {
				// If OP is in the game
				if (game.getRunnableGame().getPlayers().contains(player)) {
					// Stop the game
					game.stopGame();
				}
			}
		}
	}
}
