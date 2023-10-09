package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.PlayerData;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHandler {
	private final Map<UUID, PlayerData> playerDataMap;

	public PlayerHandler() {
		// Make new map on server load
		this.playerDataMap = new HashMap<>();
	}

	public void deregister(HypixelPlayer player) {
		// Get player from map
		PlayerData data = this.getPlayerDataMap().remove(player.getUniqueId());
		// Change online state
		data.setOnline(false);
		// Save data to file
		this.savePlayerData(data);
	}

	public PlayerData register(HypixelPlayer player) {
		// Make a data object
		PlayerData data;

		// Try to...
		try {
			// Read the data from the disk
			data = this.loadPlayerData(player);
		} catch (FileNotFoundException e) {
			// If no profile exists for the player, then generate a new profile for them
			// If the player is OP, give them owner rank, otherwise default rank
			data = new PlayerData(player.getUniqueId());
		}

		// Change their online state to true
		data.setOnline(true);

		// Add the player data to the in-memory map
		this.getPlayerDataMap().put(player.getUniqueId(), data);

		// Return the object
		return data;
	}

	public PlayerData getPlayerData(HypixelPlayer player) {
		return this.getPlayerData(player.getUniqueId());
	}

	public PlayerData getPlayerData(UUID uuid) {
		// If the data is in memory
		if (this.getPlayerDataMap().containsKey(uuid)) {
			// Return that info
			return this.getPlayerDataMap().get(uuid);
		} else {
			// Otherwise
			try {
				// Try to pull it from the disk
				return this.loadPlayerData(uuid);
			} catch (FileNotFoundException ignored) {
				// Player hasn't ever logged in
				(new RuntimeException()).printStackTrace();
				return null;
			}
		}
	}

	private void savePlayerData(PlayerData playerData) {
		Main.getMainHandler().getDataHandler().write(this.getPlayerDataPath(playerData), playerData);
	}

	private PlayerData loadPlayerData(HypixelPlayer player) throws FileNotFoundException {
		return this.loadPlayerData(player.getUniqueId());
	}

	private PlayerData loadPlayerData(UUID playerUUID) throws FileNotFoundException {
		return Main.getMainHandler().getDataHandler().read(this.getPlayerDataPath(playerUUID), PlayerData.class);
	}

	private String getPlayerDataPath(PlayerData playerData) {
		return this.getPlayerDataPath(playerData.getPlayerUUID());
	}

	private String getPlayerDataPath(UUID playerUUID) {
		return "server/players/" + playerUUID.toString() + ".json";
	}

	private Map<UUID, PlayerData> getPlayerDataMap() {
		return playerDataMap;
	}
}
