package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.FileNotFoundException;

public class LobbyData {
	private final Lobby lobby;
	private final GameMode gamemode;
	private final boolean allowFlight;
	private final Location location;

	public LobbyData(Lobby lobbyMode, GameMode lobbyGamemode, boolean lobbyAllowFlight, Location lobbyLocation) {
		this.lobby = lobbyMode;
		this.gamemode = lobbyGamemode;
		this.allowFlight = lobbyAllowFlight;
		this.location = lobbyLocation;
	}

	public LobbyData(Lobby lobbyMode, GameMode lobbyGamemode, boolean lobbyAllowFlight) {
		// Ignore location
		this(lobbyMode, lobbyGamemode, lobbyAllowFlight, (Location) null);
	}

	public LobbyData(Lobby lobbyMode, GameMode lobbyGamemode, boolean lobbyAllowFlight, String locationPath) throws FileNotFoundException {
		// Read location
		this(
			lobbyMode,
			lobbyGamemode,
			lobbyAllowFlight,
			Main.getMainHandler().getDataHandler().read(locationPath, SmallLocation.class).toLocation()
		);
	}

	public LobbyData(Lobby lobbyMode, GameMode lobbyGamemode, boolean lobbyAllowFlight, String locationPath, World world) throws FileNotFoundException {
		// Execute constructor
		this(lobbyMode, lobbyGamemode, lobbyAllowFlight, locationPath);
		// Set world
		this.getLocation().setWorld(world);
	}

	public LobbyData(Lobby lobbyMode, GameMode lobbyGamemode, boolean lobbyAllowFlight, String locationPath, String worldName) throws FileNotFoundException {
		// Get world from bukkit
		this(lobbyMode, lobbyGamemode, lobbyAllowFlight, locationPath, Bukkit.getWorld(worldName));
	}

	public Lobby getLobby() {
		return lobby;
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
}
