package inc.xiddy.hypixel.games.sumo;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.SmallLocation;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.bukkit.ChatColor.*;

public class SumoRunnable extends HypixelRunnable {
	private final ArrayList<SumoTeam> teams;
	private Location centerLoc;

	public SumoRunnable(Set<HypixelPlayer> players, SumoGame sumoGame, Lobby lobby) {
		super(players, sumoGame, lobby);

		// Make event handler
		this.setEventHandler(new SumoEventHandler(this));

		this.teams = new ArrayList<>();

	}

	@Override
	public void run() {
		// Start loading the game
		this.broadcastMessage(ChatColor.GREEN + "Loading Sumo game...");

		// Load map
		try {
			// Make a new map
			this.generateMap();

			// Get map spawn
			this.centerLoc = Main.getMainHandler().getDataHandler().read(this.getMap().getPathToMapGlobals() + "/spawn.json", SmallLocation.class).toLocation();

			// Update the world
			this.centerLoc.setWorld(this.getMap().getWorld());
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
			ChatColor.GREEN + "Starting Sumo game on map " +
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
//		// For each hider
//		// Move player to respawn location
//		this.getHiderTeam().getPlayers().forEach(player -> this.spawn(player, false, true));
//		// For each seeker
//		// Move player to blind location
//		this.getSeekerTeam().getPlayers().forEach(player -> this.spawn(player, true, false));
	}

	public void spawn(HypixelPlayer player, boolean blindPlayer, boolean hideName) {
		// Synchronously respawn them
		Main.getMainHandler().getThreadHandler().runSyncTask(() -> {
			// If spawn blind
			if (blindPlayer) {
				// Teleport below respawn location
				player.teleport(this.getCenterLoc().clone().add(0, -500, 0));
			} else {
				// Teleport to respawn location
				player.teleport(this.getCenterLoc());
			}

			// If player is a hider
			if (hideName) {
				// Make name tag invisible
				HypixelUtils.hidePlayerName(player);
			}
		});
	}

	public void repaintScoreboardForAll() {
		// For each player
		// Repaint the board for them
		this.getPlayers().forEach(this::repaintScoreboard);
	}

	public void repaintScoreboard(HypixelPlayer player) {
		//noinspection StringBufferReplaceableByString
		StringBuilder str = new StringBuilder();
		// Start by making header
		str.append(YELLOW).append(BOLD).append(this.getLobby().toString().toUpperCase())
			.append(GRAY).append("\n").append(new SimpleDateFormat("MM/dd/yy").format(new Date()))
			.append(DARK_GRAY).append(" m").append(this.getTaskId()).append("E")
			.append("\n\n");
		// Add ping difference
		str.append(GRAY).append("\n\nPing difference is ")
			.append(GOLD).append(BOLD).append(
				this.getPlayers().stream().mapToInt(HypixelUtils::getPlayerPing).sum() / this.getPlayers().size()
			).append("ms!");
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
		// Stop game mechanics
		this.internalStopGame();

		// Synchronously destroy the map
		Main.getMainHandler().getThreadHandler().runSyncTask(this::destroyMap);
	}

	public int getPlayerVoid() {
		return 0;
	}

	public Location getCenterLoc() {
		return centerLoc;
	}

}
