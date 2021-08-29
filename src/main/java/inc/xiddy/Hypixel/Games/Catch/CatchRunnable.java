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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public class CatchRunnable extends HypixelRunnable {
	private final CatchEventHandler eventHandler;
	private final List<CatchTeam> teams;
	private Location spawnLoc;
	private BukkitRunnable scoreboardTimer;

	public CatchRunnable(Set<Player> players, CatchGame catchGame, Lobby lobby) {
		super(players, catchGame, lobby);

		// Make event handler
		this.eventHandler = new CatchEventHandler(this);

		// Make teams
		// Init list
		this.teams = new ArrayList<>();

		// Make hider team
		CatchTeam hiderTeam = new CatchTeam(TeamColor.RED, null, 1, false);
		// Populate
		// Select hider
		Player hider = HypixelUtils.randomFromArray(players.toArray(new Player[0]));
		// Update state
		hiderTeam.setPlayerState(hider, GameState.ALIVE);
		// Add hider to hider team
		hiderTeam.addPlayer(hider);
		// Add to teams
		this.teams.add(hiderTeam);

		// Make seeker team
		CatchTeam seekerTeam = new CatchTeam(TeamColor.GREEN, null, 1, true);
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
		// Add to teams
		this.teams.add(seekerTeam);
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
			private int time = 180;

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
					stopGame();
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
		StringBuilder str = new StringBuilder();
		// Start by making header
		str.append(YELLOW).append(BOLD).append("CATCH")
			.append(GRAY).append("\n").append(new SimpleDateFormat("MM/dd/yy").format(new Date()))
			.append(DARK_GRAY).append(" m").append(this.getTaskId()).append("E")
			.append(WHITE).append("\n\nGame Over in ").append(GREEN)
			.append(time / 60).append(":").append(String.format("%02d", time % 60))
			.append("\n\n");
		// For each team
		for (CatchTeam team: this.getTeams()) {
			// Get team name
			String teamName;
			if (team.getTeamColor() == TeamColor.GREEN) {
				teamName = "SEEKER";
			} else {
				teamName = "HIDER";
			}
			// Add team name and size to the scoreboard
			str.append(team.getTeamColor().getColorCode())
				.append(teamName).append("S: ")
				.append(WHITE).append(team.getTeamColor().getCapitalizedString()).append(": ")
				.append(team.getAlivePlayers().size());
			// If this is the player's team
			if (team.getPlayers().contains(player)) {
				// Add "YOU" next to team
				str.append(GRAY).append(" YOU");
			}
			// Add newline for next iteration
			str.append("\n");
		}
		// Update the lobby scoreboard
		Main.getMainHandler().getThreadHandler().scheduleSyncTask(
			() -> Main.getMainHandler().getPlayerHandler().getPlayerData(player).setScoreboard(str.toString()),
			1L
		);
	}

	public void stopGame() {
		// Pop task off bukkit manager
		this.cancel();

		// Stop scoreboard
		this.scoreboardTimer.cancel();

		// Stop events
		HandlerList.unregisterAll(this.getEventHandler());

		// Destroy the map
		this.destroyMap();
	}

	public List<CatchTeam> getTeams() {
		return this.teams;
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
}
