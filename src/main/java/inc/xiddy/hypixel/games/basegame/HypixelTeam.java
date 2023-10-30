package inc.xiddy.hypixel.games.basegame;

import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;
import inc.xiddy.hypixel.games.basegame.ingame.state.GameState;
import inc.xiddy.hypixel.games.basegame.maps.GameMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class HypixelTeam {
	private final TeamColor color;
	private final Set<InGamePlayer> players;
	private final GameMap map;
	private final int teamSize;

	public HypixelTeam(TeamColor color, GameMap map, int teamSize) {
		// Set data to fields
		this.color = color;
		this.map = map;
		this.teamSize = teamSize;
		this.players = new HashSet<>();
	}

	public boolean isTeamFull() {
		return this.getPlayerCount() == this.getTeamSize();
	}

	public int getPlayerCount() {
		return this.getPlayers().size();
	}

	public final Set<InGamePlayer> getPlayers() {
		return this.players;
	}

	public void addPlayer(InGamePlayer player) {
		this.players.add(player);
	}

	public boolean contains(InGamePlayer player) {
		for (InGamePlayer teamPlayer : getPlayers()) {
			if (teamPlayer.equals(player)) return true;
		}

		return false;
	}

	public Set<HypixelPlayer> getPlayersOfState(GameState state) {
		return getPlayers()
			.stream()
			.filter(p -> p.getState() == state)
			.collect(Collectors.toSet());
	}

	public void setPlayersStates(GameState state) {
		for (InGamePlayer player : getPlayers()) {
			player.setState(state);
		}
	}

	public TeamColor getTeamColor() {
		return this.color;
	}

	public ItemStack getWool(int amount) {
		//noinspection deprecation
		return new ItemStack(Material.WOOL, amount, this.getTeamColor().getDyeColor().getWoolData());
	}

	public GameMap getMap() {
		return map;
	}

	public int getTeamSize() {
		return teamSize;
	}
}
