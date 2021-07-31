package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.HypixelGame;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BedwarsGame extends HypixelGame {
	private final BedwarsRunnable game;

	public BedwarsGame(Lobby lobby) {
		// Set game and initialize game as runnable
		this.game = new BedwarsRunnable(1, Bukkit.getOnlinePlayers().toArray(new Player[0]), this, lobby);
		// Run game
		this.game.runTaskAsynchronously(Main.getInstance());
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
