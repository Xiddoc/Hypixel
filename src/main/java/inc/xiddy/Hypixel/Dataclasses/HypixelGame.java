package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Main;

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
