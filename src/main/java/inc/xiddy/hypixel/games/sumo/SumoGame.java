package inc.xiddy.hypixel.games.sumo;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.utility.HypixelUtils;

import java.util.HashSet;

public class SumoGame extends HypixelGame {
	private final SumoRunnable game;

	public SumoGame(Lobby lobby) {
		// Set game and initialize game as runnable
		this.game = new SumoRunnable(new HashSet<>(HypixelUtils.getOnlinePlayers()), this, lobby);
	}

	@Override
	public void stopGame() {
		// Stop the runnable
		this.getRunnableGame().stopGame();

		// Remove game from ongoing games
		Main.getMainHandler().getGameHandler().removeGame(this);
	}

	@Override
	public SumoRunnable getRunnableGame() {
		return game;
	}
}
