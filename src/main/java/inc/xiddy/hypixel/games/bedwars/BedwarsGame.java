package inc.xiddy.hypixel.games.bedwars;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.utility.HypixelUtils;

import java.util.HashSet;

public class BedwarsGame extends HypixelGame {
	private final BedwarsRunnable game;

	public BedwarsGame(Lobby lobby) {
		// Set game and initialize game as runnable
		this.game = new BedwarsRunnable(new HashSet<>(HypixelUtils.getOnlinePlayers()), this, lobby, 1);
	}

	@Override
	public void stopGame() {
		// Stop the runnable
		this.getRunnableGame().stopGame();

		// Remove game from ongoing games
		Main.getMainHandler().getGameHandler().removeGame(this);
	}

	@Override
	public BedwarsRunnable getRunnableGame() {
		return game;
	}
}
