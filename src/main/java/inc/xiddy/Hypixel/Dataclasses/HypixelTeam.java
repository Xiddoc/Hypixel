package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.TeamColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class HypixelTeam {
	private final TeamColor color;
	private final Map<HypixelPlayer, GameState> players;
	private final GameMap map;
	private final int teamSize;

	public HypixelTeam(TeamColor color, GameMap map, int teamSize) {
		// Set data to fields
		this.color = color;
		this.map = map;
		this.teamSize = teamSize;
		this.players = new HashMap<>();
	}

	public final boolean isTeamFull() {
		return this.getPlayerCount() == this.getTeamSize();
	}

	public final int getPlayerCount() {
		return this.getPlayers().toArray(new HypixelPlayer[0]).length;
	}

	public final Set<HypixelPlayer> getPlayers() {
		return this.players.keySet();
	}

	public final Map<HypixelPlayer, GameState> getPlayerAliveMap() {
		return this.players;
	}

	public final void setPlayerState(HypixelPlayer player, GameState state) {
		this.getPlayerAliveMap().put(player, state);
	}

	public final void addPlayer(HypixelPlayer player) {
		this.players.put(player, GameState.ALIVE);
	}

	public final boolean isEliminated() {
		// If there are no alive (respawning or alive) players, then they are eliminated
		return this.getAlivePlayers().isEmpty();
	}

	public final Set<HypixelPlayer> getAlivePlayers() {
		// For each player in the map
		// Filter by the .getValue of the map (the value stating if the player is alive)
		Set<HypixelPlayer> players = new HashSet<>();
		for (Map.Entry<HypixelPlayer, GameState> entry : this.getPlayerAliveMap().entrySet()) {
			// If not dead (spectating)
			if (!entry.getValue().equals(GameState.SPECTATING)) {
				players.add(entry.getKey());
			}
		}
		// Return the player set
		return players;
	}

	public final TeamColor getTeamColor() {
		return this.color;
	}

	public final ItemStack getWool(int amount) {
		//noinspection deprecation
		return new ItemStack(Material.WOOL, amount, this.getTeamColor().getDyeColor().getWoolData());
	}

	public final GameMap getMap() {
		return map;
	}

	public final int getTeamSize() {
		return teamSize;
	}
}
