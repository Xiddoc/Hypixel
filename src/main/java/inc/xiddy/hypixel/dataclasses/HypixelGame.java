package inc.xiddy.hypixel.dataclasses;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;

public abstract class HypixelGame {
	@SuppressWarnings("unused")
	private HypixelRunnable game;

	public void startGame() {
		// Start the runnable
		this.getRunnableGame().runTaskAsynchronously(Main.getInstance());
	}

	public void stopGame() {
		// Stop the runnable
		this.getRunnableGame().stopGame();
		// Remove game from ongoing games
		Main.getMainHandler().getGameHandler().removeGame(this);
	}

	public HypixelRunnable getRunnableGame() {
		return game;
	}
}
