package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.HypixelGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler {
	private final Map<Lobby, List<HypixelGame>> games;

	public GameHandler() {
		// Initialize map
		this.games = new HashMap<>();
		// Init lists
		this.games.put(Lobby.BEDWARS, new ArrayList<>());
	}

	public List<HypixelGame> getAllGames() {
		// New list
		List<HypixelGame> allGames = new ArrayList<>();
		// For each game
		for (Map.Entry<Lobby, List<HypixelGame>> entry: this.getGamesList().entrySet()) {
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
		return this.getGamesList().get(lobby);
	}

	private Map<Lobby, List<HypixelGame>> getGamesList() {
		return this.games;
	}

	public void addGame(HypixelGame game) {
		this.getGames(game.getRunnableGame().getLobby()).add(game);
	}

	public void removeGame(HypixelGame game) {
		// Remove game from the list of that game lobby
		this.getGames(game).remove(game);
	}
}
