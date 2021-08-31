package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.LobbyData;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LobbyHandler {
	private final List<LobbyData> lobbies;

	public LobbyHandler() throws FileNotFoundException {
		// Make a lobby map
		this.lobbies = new ArrayList<>();
		// Add the lobby data to it
		this.lobbies.add(new LobbyData(Lobby.HUB, GameMode.ADVENTURE, true, "server/hub.json", "hub"));
		this.lobbies.add(new LobbyData(Lobby.SPECTATOR, GameMode.SPECTATOR, true));
		this.lobbies.add(new LobbyData(Lobby.BEDWARS, GameMode.SURVIVAL, false));
		this.lobbies.add(new LobbyData(Lobby.SKYWARS, GameMode.SURVIVAL, false));
		this.lobbies.add(new LobbyData(Lobby.DUELS, GameMode.SURVIVAL, false));
		this.lobbies.add(new LobbyData(Lobby.BRIDGE, GameMode.SURVIVAL, false));
		this.lobbies.add(new LobbyData(Lobby.CATCH, GameMode.SURVIVAL, false));
	}

	public void setPlayer(Player player, Lobby lobby) {
		// Get lobby data
		LobbyData lobbyData = this.getLobbyData(lobby);
		// Update player data
		// Update gamemode
		player.setGameMode(lobbyData.getGamemode());
		// Update flight mode
		player.setAllowFlight(lobbyData.getFlight());
		// Teleport player to location
		if (lobbyData.getLocation() != null) {
			player.teleport(lobbyData.getLocation());
		}
		// For each potion effect
		for (PotionEffect effect : player.getActivePotionEffects()) {
			// Remove it from the player
			player.removePotionEffect(effect.getType());
		}
	}

	private LobbyData getLobbyData(Lobby lobby) {
		// For each lobby
		// If the lobby is equal to the queried lobby
		// Add it to the list
		// Return the LobbyData
		return this.lobbies.stream().filter(
			lobbyData -> lobbyData.getLobby().equals(lobby)
		).collect(Collectors.toList()).get(0);
	}

	public Set<Player> getPlayersInLobby(Lobby lobby) {
		// For each player online
		// Run a filter
		// If the player's lobby is equal to the lobby
		// Add it to the set
		// Return the full set
		return Bukkit.getOnlinePlayers().stream().filter(
			player -> Main.getMainHandler().getPlayerHandler().getPlayerData(player).getLobby().equals(lobby)
		).collect(Collectors.toSet());
	}
}
