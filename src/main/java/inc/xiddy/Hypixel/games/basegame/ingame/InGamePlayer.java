package inc.xiddy.hypixel.games.basegame.ingame;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.basegame.ingame.state.GameState;
import org.bukkit.entity.Player;

public class InGamePlayer extends HypixelPlayer {
	private GameState state;

	public InGamePlayer(Player player) {
		super(player);
	}

	public void setState(GameState state) {
		this.state.onExitState(this);
		state.onEnterState(this);
		this.state = state;
	}

	public GameState getState() {
		return state;
	}
}
