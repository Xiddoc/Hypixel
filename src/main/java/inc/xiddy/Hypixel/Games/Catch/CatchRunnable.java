package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.TeamColor;
import inc.xiddy.Hypixel.Dataclasses.GameState;
import inc.xiddy.Hypixel.Dataclasses.HypixelRunnable;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.HypixelUtils;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public class CatchRunnable extends HypixelRunnable {
	private final CatchEventHandler eventHandler;
	private final CatchTeam seekerTeam;
	private final CatchTeam hiderTeam;
	private Location spawnLoc;
	private BukkitRunnable scoreboardTimer;

	public CatchRunnable(Set<Player> players, CatchGame catchGame, Lobby lobby) {
		super(players, catchGame, lobby);

		// Make event handler
		this.eventHandler = new CatchEventHandler(this);

		// Make hider team
		this.hiderTeam = new CatchTeam(TeamColor.RED, null, 1, false);
		// Populate
		// Select hider
		Player hider = HypixelUtils.randomFromArray(players.toArray(new Player[0]));
		// Update state
		hiderTeam.setPlayerState(hider, GameState.ALIVE);
		// Add hider to hider team
		hiderTeam.addPlayer(hider);

		// Make seeker team
		this.seekerTeam = new CatchTeam(TeamColor.GREEN, null, 1, true);
		// Populate
		for (Player player: players) {
			// If the player is not the hider
			if (!player.equals(hider)) {
				// Update state
				seekerTeam.setPlayerState(player, GameState.ALIVE);
				// Add player to seeker team
				seekerTeam.addPlayer(player);
			}
		}
	}

	@Override
	public void run() {
		// Start loading the game
		this.broadcastMessage(ChatColor.GREEN + "Loading Catch game...");

		// Load map
		try {
			// Make a new map
			this.generateMap();

			// Get map spawn
			this.spawnLoc = Main.getMainHandler().getDataHandler().read(this.getMap().getPathToMapGlobals() + "\\spawn.json", SmallLocation.class).toLocation();

			// Update the world
			this.spawnLoc.setWorld(this.getMap().getWorld());
		} catch (FileNotFoundException e) {
			// Print traceback
			e.printStackTrace();
			// Inform of error
			this.broadcastMessage(ChatColor.DARK_RED + "There was an error while creating the game...");
			// Stop game
			this.stopGame();
			return;
		}

		this.broadcastMessage(
			ChatColor.GREEN + "Starting Catch game on map " +
				ChatColor.GOLD + this.getMap().getCapitalizedMapName() +
				ChatColor.GREEN + "..."
		);

		// Start the game sequence
		// For each player
		for (Player player: this.getPlayers()) {
			// Set lobby mode
			// Move player to respawn location
			this.spawn(player);
		}

		// Make scoreboard painter
		this.scoreboardTimer = new BukkitRunnable() {
			private int time = 10;

			@Override
			public void run() {
				// Repaint scoreboard
				repaintScoreboardForAll(this.time);

				// Decrement time
				this.time --;

				// If time hit zero
				if (this.time == 0) {
					// Stop the runnable
					this.cancel();

					// Stop the game
					gameOver(getHiderTeam().getPlayers());
				}
			}
		};
		// Start the painter
		this.scoreboardTimer.runTaskTimerAsynchronously(Main.getInstance(), 10, 20);
	}

	private void spawn(Player player) {
		// Synchronously respawn them
		Main.getMainHandler().getThreadHandler().runSyncTask(() -> {
			// Teleport to respawn location
			player.teleport(this.getSpawnLoc());

			// Set mode
			Main.getMainHandler().getPlayerHandler().getPlayerData(player).setLobby(this.getLobby());
		});
	}

	private void broadcastMessage(String message) {
		// For each player
		for (Player player: this.getPlayers()) {
			// Send the message
			player.sendMessage(message);
		}
	}

	public void repaintScoreboardForAll(int time) {
		// For each player
		for (Player player: this.getPlayers()) {
			// Repaint the board for them
			this.repaintScoreboard(player, time);
		}
	}

	public void repaintScoreboard(Player player, int time) {
		//noinspection StringBufferReplaceableByString
		StringBuilder str = new StringBuilder();
		// Start by making header
		str.append(YELLOW).append(BOLD).append("CATCH")
			.append(GRAY).append("\n").append(new SimpleDateFormat("MM/dd/yy").format(new Date()))
			.append(DARK_GRAY).append(" m").append(this.getTaskId()).append("E")
			.append(WHITE).append("\n\nGame Over in ").append(GREEN)
			.append(time / 60).append(":").append(String.format("%02d", time % 60))
			.append("\n\n");
		// Add team name and size to the scoreboard (Seekers)
		str.append(this.getSeekerTeam().getTeamColor().getColorCode()).append("S ")
			.append(WHITE).append("SEEKERS: ")
			.append(this.getSeekerTeam().getAlivePlayers().size())
			// Add team name and size to the scoreboard (Hiders)
			.append(this.getHiderTeam().getTeamColor().getColorCode()).append("H ")
			.append(WHITE).append("HIDERS: ")
			.append(this.getHiderTeam().getAlivePlayers().size())
			// Get player's team
			.append(GRAY).append("\n\nYou are a ")
			.append(this.getSeekerTeam().getPlayers().contains(player) ?
				this.getSeekerTeam().getTeamColor().getColorCode() + "SEEKER" :
				this.getHiderTeam().getTeamColor().getColorCode() + "HIDER");
		// Add footer
		str.append("\n\n").append(YELLOW).append("www.hypixel.net");

		// Synchronously
		Main.getMainHandler().getThreadHandler().runSyncTask(() ->
			// Update the lobby scoreboard
			Main.getMainHandler().getPlayerHandler().getPlayerData(player).setScoreboard(str.toString())
		);
	}

	@Override
	public void stopGame() {
		// Pop task off bukkit manager
		this.cancel();

		// Stop scoreboard
		this.scoreboardTimer.cancel();

		// Stop events
		HandlerList.unregisterAll(this.getEventHandler());

		// Synchronously destroy the map
		Main.getMainHandler().getThreadHandler().runSyncTask(this::destroyMap);
	}

	public CatchEventHandler getEventHandler() {
		return eventHandler;
	}

	public int getPlayerVoid() {
		return 0;
	}

	public int getBlockVoidMin() {
		return 0;
	}

	public int getBlockVoidMax() {
		return 100;
	}

	public Location getSpawnLoc() {
		return spawnLoc;
	}

	public CatchTeam getSeekerTeam() {
		return seekerTeam;
	}

	public CatchTeam getHiderTeam() {
		return hiderTeam;
	}
}
