package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.HypixelTimer;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;
import inc.xiddy.hypixel.games.basegame.ingame.GameState;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public class CatchRunnable extends HypixelRunnable {
	private final CatchTeam seekerTeam;
	private final CatchTeam hiderTeam;
	private final CatchRadar radar;
	private Location spawnLoc;
	private HypixelTimer gameTimer;

	public CatchRunnable(Set<HypixelPlayer> players, CatchGame catchGame, Lobby lobby) {
		super(players, catchGame, lobby);

		// Make event handler
		this.setEventHandler(new CatchEventHandler(this));

		// Make hider team
		this.hiderTeam = new CatchTeam(TeamColor.RED, null, 1, false);
		// Select hider
		HypixelPlayer hider = HypixelUtils.randomFromArray(players.toArray(new HypixelPlayer[0]));
		// Update state
		this.hiderTeam.setPlayerState(hider, GameState.ALIVE);
		// Add hider to hider team
		this.hiderTeam.addPlayer(hider);

		// Make seeker team
		this.seekerTeam = new CatchTeam(TeamColor.GREEN, null, 1, true);
		// Populate
		for (HypixelPlayer player: players) {
			// If the player is not the hider
			if (!player.equals(hider)) {
				// Update state
				this.seekerTeam.setPlayerState(player, GameState.ALIVE);
				// Add player to seeker team
				this.seekerTeam.addPlayer(player);
			}
		}

		// Make radar
		this.radar = new CatchRadar();
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
			this.spawnLoc = Main.getMainHandler().getDataHandler().read(this.getMap().getPathToMapGlobals() + "/spawn.json", SmallLocation.class).toLocation();

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
		// Set each player's lobby
		this.getPlayers().forEach(
			player -> Main.getMainHandler().getThreadHandler().runSyncTask(() ->
				Main.getMainHandler().getPlayerHandler().getPlayerData(player).setLobby(this.getLobby())
			)
		);
		// For each hider
		// Move player to respawn location
		this.getHiderTeam().getPlayers().forEach(player -> this.spawn(player, false, true));
		// For each seeker
		// Move player to blind location
		this.getSeekerTeam().getPlayers().forEach(player -> this.spawn(player, true, false));

		// Make scoreboard painter
		this.gameTimer = new HypixelTimer(10) {

			@Override
			public void onLoop() {
				// Get remaining time
				int gameTime = 180;
				int remainingTime = gameTime - this.getElapsedTime();

				// Repaint scoreboard
				repaintScoreboardForAll(remainingTime);

				// If time hit zero
				if (this.getElapsedTime() == gameTime) {
					// Stop the runnable
					this.cancel();

					// Stop the game
					gameOver(getHiderTeam().getPlayers());
				} else if (this.getElapsedTime() < 30 && this.getElapsedTime() % 5 == 0) {
					// If it is still the first 30 seconds of the game
					// And it is a multiple of 5
					// Announce the time
					broadcastMessage(GREEN + "Hiders have " + GOLD + (30 - this.getElapsedTime()) + GREEN + " seconds left to hide...");
				} else if (this.getElapsedTime() == 30) {
					// If it is the 30th second of the game
					// Tell the players that the game has started
					broadcastMessage(GREEN + "The seekers have been released!");
					// For each seeker
					// Move player to respawn location
					getSeekerTeam().getPlayers().forEach(player -> spawn(player, false, false));
				}
			}
		};
	}

	public void spawn(HypixelPlayer player, boolean blindPlayer, boolean hideName) {
		// Synchronously respawn them
		Main.getMainHandler().getThreadHandler().runSyncTask(() -> {
			// If spawn blind
			if (blindPlayer) {
				// Teleport below respawn location
				player.teleport(this.getSpawnLoc().clone().add(0, -500, 0));
			} else {
				// Teleport to respawn location
				player.teleport(this.getSpawnLoc());
			}

			// If player is a hider
			if (hideName) {
				// Make name tag invisible
				HypixelUtils.hidePlayerName(player);
			}
		});
	}

	public void repaintScoreboardForAll(int time) {
		// For each player
		// Repaint the board for them
		this.getPlayers().forEach(player -> this.repaintScoreboard(player, time));
	}

	public void repaintScoreboard(HypixelPlayer player, int time) {
		//noinspection StringBufferReplaceableByString
		StringBuilder str = new StringBuilder();
		// Start by making header
		str.append(YELLOW).append(BOLD).append(this.getLobby().toString().toUpperCase())
			.append(GRAY).append("\n").append(new SimpleDateFormat("MM/dd/yy").format(new Date()))
			.append(DARK_GRAY).append(" m").append(this.getTaskId()).append("E")
			.append(WHITE).append("\n\nGame Over in ").append(GREEN)
			.append(time / 60).append(":").append(String.format("%02d", time % 60))
			.append("\n\n");
		// Add team name and size to the scoreboard (Seekers)
		str.append(this.getSeekerTeam().getTeamColor().getColorCode()).append("S ")
			.append(WHITE).append("Seekers: ")
			.append(YELLOW).append(this.getSeekerTeam().getAlivePlayers().size()).append("\n")
			// Add team name and size to the scoreboard (Hiders)
			.append(this.getHiderTeam().getTeamColor().getColorCode()).append("H ")
			.append(WHITE).append("Hiders: ")
			.append(YELLOW).append(this.getHiderTeam().getAlivePlayers().size())
			// Get player's team
			.append(GRAY).append("\n\nYou are a ")
			.append(this.getSeekerTeam().getPlayers().contains(player) ?
				this.getSeekerTeam().getTeamColor().getColorCode() + "" + BOLD + "SEEKER!" :
				this.getHiderTeam().getTeamColor().getColorCode() + "" + BOLD + "HIDER!");
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
		// Stop scoreboard
		this.getGameTimer().cancel();

		// Stop game mechanics
		this.internalStopGame();

		// Synchronously destroy the map
		Main.getMainHandler().getThreadHandler().runSyncTask(this::destroyMap);
	}

	public int getPlayerVoid() {
		return 0;
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

	public CatchRadar getRadar() {
		return radar;
	}

	public HypixelTimer getGameTimer() {
		return gameTimer;
	}
}
