package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Utility.HypixelUtils;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

public abstract class HypixelRunnable extends BukkitRunnable {
	private GameMap map;
	private final Lobby lobby;
	private final Set<Player> players;
	private final HypixelGame externalGame;
	private HypixelEventHandler eventHandler;
	private boolean gameOver;

	public HypixelRunnable(Set<Player> players, HypixelGame hypixelGame, Lobby lobby) {
		// Set to fields
		this.externalGame = hypixelGame;
		this.lobby = lobby;
		this.players = players;
		this.gameOver = false;
	}

	@Override
	public abstract void run();

	public abstract void stopGame();

	public final Lobby getLobby() {
		return this.lobby;
	}

	public final Set<Player> getPlayers() {
		return this.players;
	}

	public final void generateMap() throws FileNotFoundException {
		// Make a new map using this lobby
		// Then set the map to the generated world
		this.setMap(Main.getMainHandler().getMapHandler().createMap(this.getLobby()));
	}

	public final void destroyMap() {
		// Move all players to lobby
		for (Player player: this.getPlayers()) {
			// Try to (player might have quit the game)
			try {
				// Kick them out of the game
				Main.getMainHandler().getPlayerHandler().getPlayerData(player).setLobby(Lobby.HUB);
			} catch (NullPointerException ignored) {}
		}

		// Unload the world
		Bukkit.unloadWorld(this.getMap().getWorld().getName(), false);

		// Remove world files
		try {
			Main.getMainHandler().getDataHandler().removeFolder(
				Bukkit.getWorldContainer() + "/" + this.getMap().getWorld().getName()
			);
		} catch (IOException e) {
			Main.getMainHandler().getLogger().error("Couldn't delete world:");
			e.printStackTrace();
		}
	}

	public final void gameOver(Set<Player> winners) {
		// Set game over
		this.gameOver = true;

		// Length of game over message
		double gameOverTime = 5.0;

		// For each player
		for (Player player: this.getPlayers()) {
			// If you are a winner
			if (winners.contains(player)) {
				// Show winner message!
				HypixelUtils.sendTitle(
					player,
					ChatColor.GOLD + "" + ChatColor.BOLD + "VICTORY!", "",
					gameOverTime, 0, 0
				);
			} else {
				// Otherwise,
				// Show loser message!
				HypixelUtils.sendTitle(
					player,
					ChatColor.RED + "" + ChatColor.BOLD + "GAME OVER!", "",
					gameOverTime, 0, 0
				);
			}
			// Play sound
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
		}

		// Stop the game in a few seconds
		Main.getMainHandler().getThreadHandler().scheduleSyncTask(this::stopGame, gameOverTime);
	}

	public final void setMap(GameMap map) {
		this.map = map;
	}

	public final GameMap getMap() {
		return this.map;
	}

	public final HypixelGame getGame() {
		return this.externalGame;
	}

	public final void internalStopGame() {
		// Pop task off bukkit manager
		this.cancel();

		// Remove game from ongoing games
		Main.getMainHandler().getGameHandler().removeGame(this.getGame());

		// Stop events
		HandlerList.unregisterAll(this.getEventHandler());
	}

	public final void setEventHandler(HypixelEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public final HypixelEventHandler getEventHandler() {
		return eventHandler;
	}

	public final boolean isGameOver() {
		return gameOver;
	}
}
