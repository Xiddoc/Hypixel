package inc.xiddy.hypixel.games.basegame.ingame.state;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;

public interface GameState {
	void onEnterState(HypixelPlayer player);
	void onExitState(HypixelPlayer player);
}
