package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.GameMap;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class MapHandler {
	private final Map<String, String> gamerules;

	public MapHandler() {
		this.gamerules = new HashMap<>();
		this.gamerules.put("doDaylightCycle", "false");
		this.gamerules.put("doEntityDrops", "false");
		this.gamerules.put("doMobSpawning", "false");
		this.gamerules.put("doFireTick", "false");
		this.gamerules.put("doMobLoot", "false");
		this.gamerules.put("mobGriefing", "false");
		this.gamerules.put("sendCommandFeedback", "false");
		this.gamerules.put("logAdminCommands", "false");
		this.gamerules.put("commandBlockOutput", "false");
	}

	public GameMap createMap(Lobby lobby) throws FileNotFoundException {
		// Get the path
		return this.createMap(lobby, this.getRandomMap(Main.getMainHandler().getDataHandler().getBasepath() + "\\" + GameMap.getPathToGameMaps(lobby)).getName());
	}

	public GameMap createMap(Lobby lobby, String mapName) {
		// Create the game
		GameMap newGameMap = new GameMap(lobby, mapName);
		// Create the map
		this.createMap(newGameMap);
		// Return the path
		return newGameMap;
	}

	public void createMap(GameMap map) {
		// Make new world name
		String worldName = map.getGameAsString().toUpperCase() + "-" + UUID.randomUUID();
		// Move map to world
		Main.getMainHandler().getDataHandler().copyFolderContents(
			new File(Main.getMainHandler().getDataHandler().getBasepath() + "\\" + map.getPathToMapWorld()),
			Bukkit.getWorldContainer().getPath() + "\\" + worldName,
			new String[] {"uid.dat", "session.lock"}
		);
		// Create the world
		try {
			WorldCreator wc = new WorldCreator(worldName);
			wc.createWorld();
		} catch (IllegalStateException ignored) {
			// It will throw an error for asynchronously trying to create a world
			// Although it throws the error, for whatever reason it still creates the world
			// Good enough for me, I guess
		}
		// Set the world to the map object
		// This convoluted code segment asks Bukkit to get you all the available worlds
		// Then get the one that has the same name as our world
		// This is because the world is technically not registered fully in Bukkit, so some methods
		// Like Bukkit#getWorld() do not work... just don't touch it and hopefully everything will be fine
		// Important to mention- the reason we get the world like this is that due to the above error catching,
		// wc#createWorld() will not return a value, so we can't get the World object from the creation method.
		World newWorld = Bukkit.getWorlds().stream()
			.filter(world -> world.getName().equals(worldName))
			.collect(Collectors.toList()).get(0);
		// For each gamerule
		for (Map.Entry<String, String> entry : this.getGamerules().entrySet()) {
			// Set the gamerule
			newWorld.setGameRuleValue(entry.getKey(), entry.getValue());
		}
		// Disable extra saving
		newWorld.setAutoSave(false);
		// For each entity
		for (Entity entity: newWorld.getLivingEntities()) {
			// If the entity is not a player
			if (!(entity instanceof Player)) {
				// Remove it
				entity.remove();
			}
		}
		// Set the world to the map object
		map.setWorld(
			newWorld
		);
	}

	public Map<String, String> getGamerules() {
		return gamerules;
	}

	private File getRandomMap(String path) throws FileNotFoundException {
		// List all the files in the folder
		File[] files = new File(path).listFiles();
		// If there are any files in the directory
		if (files != null) {
			// Get a random index from the randomizer
			// Pull that file index
			return files[(new Random()).nextInt(files.length)];
		} else {
			// Otherwise, throw error
			throw new FileNotFoundException();
		}
	}
}
