package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.utility.HypixelUtils;

import java.util.HashSet;

public class CatchGame extends HypixelGame {
	private final CatchRunnable game;

	public CatchGame(Lobby lobby) {
		// Set game and initialize game as runnable
		this.game = new CatchRunnable(new HashSet<>(HypixelUtils.getOnlinePlayers()), this, lobby);
	}

	@Override
	public void stopGame() {
		// Stop the runnable
		this.getRunnableGame().stopGame();

		// Remove game from ongoing games
		Main.getMainHandler().getGameHandler().removeGame(this);
	}

	@Override
	public CatchRunnable getRunnableGame() {
		return game;
	}
}
