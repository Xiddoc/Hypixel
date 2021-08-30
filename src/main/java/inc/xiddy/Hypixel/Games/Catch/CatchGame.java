package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.HypixelGame;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;

import java.util.HashSet;

public class CatchGame extends HypixelGame {
	private final CatchRunnable game;

	public CatchGame(Lobby lobby) {
		// Set game and initialize game as runnable
		this.game = new CatchRunnable(new HashSet<>(Bukkit.getOnlinePlayers()), this, lobby);
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
