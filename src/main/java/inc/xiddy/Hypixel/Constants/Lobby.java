package inc.xiddy.Hypixel.Constants;

import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.Main;
import inc.xiddy.Hypixel.Utility.HypixelUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

public enum Lobby {
	HUB(GameMode.ADVENTURE, true, "server/hub.json", "HUB"),
	SPECTATOR(GameMode.SPECTATOR, true),
	BEDWARS(GameMode.SURVIVAL, false),
	SKYWARS(GameMode.SURVIVAL, false),
	SUMO(GameMode.SURVIVAL, false),
	BRIDGE(GameMode.SURVIVAL, false),
	CATCH(GameMode.SURVIVAL, false);

	private final GameMode gamemode;
	private final boolean allowFlight;
	private Location location;

	Lobby(final GameMode lobbyGamemode, final boolean lobbyAllowFlight, final String locationPath, final String worldName) {
		// Execute constructor
		this.gamemode = lobbyGamemode;
		this.allowFlight = lobbyAllowFlight;
		try {
			// Read location
			this.location = Main.getMainHandler().getDataHandler().read(locationPath, SmallLocation.class).toLocation();
			// Set world
			this.getLocation().setWorld(Bukkit.getWorld(worldName));
		} catch (FileNotFoundException e) {
			// Set location to null which will probably throw an error somewhere else
			this.location = null;
			// File error while reading spawn location
			Main.getMainHandler().getLogger().error("Could not load lobby:");
			e.printStackTrace();
		}
	}

	Lobby(final GameMode lobbyGamemode, final boolean lobbyAllowFlight) {
		// Ignore location
		this.gamemode = lobbyGamemode;
		this.allowFlight = lobbyAllowFlight;
		this.location = null;
	}

	public GameMode getGamemode() {
		return gamemode;
	}

	public boolean getFlight() {
		return allowFlight;
	}

	public Location getLocation() {
		return location;
	}

	public void setPlayer(HypixelPlayer player) {
		// Update player data
		// Update gamemode
		player.setGameMode(this.getGamemode());
		// Update flight mode
		player.setAllowFlight(this.getFlight());
		// Teleport player to location
		if (this.getLocation() != null) {
			player.teleport(this.getLocation());
		}
		// For each potion effect
		for (PotionEffect effect : player.getActivePotionEffects()) {
			// Remove it from the player
			player.removePotionEffect(effect.getType());
		}
	}

	public Set<HypixelPlayer> getPlayersInLobby() {
		// For each player online
		// Run a filter
		// If the player's lobby is equal to the lobby
		// Add it to the set
		// Return the full set
		return HypixelUtils.getOnlinePlayers().stream().filter(
			player -> Main.getMainHandler().getPlayerHandler().getPlayerData(player).getLobby().equals(this)
		).collect(Collectors.toSet());
	}
}
