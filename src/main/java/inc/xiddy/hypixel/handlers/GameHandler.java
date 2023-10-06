package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler {
	private final Map<Lobby, List<HypixelGame>> lobbyGameMap;

	public GameHandler() {
		// Initialize map
		this.lobbyGameMap = new HashMap<>();
	}

	public List<HypixelGame> getAllGames() {
		// New list
		List<HypixelGame> allGames = new ArrayList<>();
		// For each game
		for (Map.Entry<Lobby, List<HypixelGame>> entry : this.getGamesList().entrySet()) {
			// Append to new list
			allGames.addAll(entry.getValue());
		}
		// Return new list
		return allGames;
	}

	public List<HypixelGame> getGames(HypixelGame game) {
		return this.getGames(game.getRunnableGame().getLobby());
	}

	public List<HypixelGame> getGames(Lobby lobby) {
		// Get games for that lobby
		List<HypixelGame> retrievedGames = this.getGamesList().get(lobby);
		// If games is null
		if (retrievedGames == null) {
			// Make a new list for the lobby
			this.getGamesList().put(lobby, new ArrayList<>());
			// Get the pointer to the list of games
			return this.getGamesList().get(lobby);
		} else {
			// Otherwise, return the list of games
			return retrievedGames;
		}
	}

	private Map<Lobby, List<HypixelGame>> getGamesList() {
		return this.lobbyGameMap;
	}

	public void addGame(HypixelGame game) {
		// Add game to game list
		this.getGames(game.getRunnableGame().getLobby()).add(game);
		// Run game
		game.startGame();
	}

	public void removeGame(HypixelGame game) {
		// Remove game from the list of that game lobby
		this.getGames(game).remove(game);
	}
}
