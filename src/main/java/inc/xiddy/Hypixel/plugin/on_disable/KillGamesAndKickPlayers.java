package inc.xiddy.hypixel.plugin.on_disable;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.logging.Log;
import inc.xiddy.hypixel.plugin.OnPluginEventHandler;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class KillGamesAndKickPlayers implements OnPluginEventHandler {
	@Override
	public void onHandleEvent(JavaPlugin plugin) {
		Log.info("Shutting down games...");
		shutDownGames();

		Log.info("Kicking all players...");
		kickAllPlayers();
	}

	private void shutDownGames() {
		for (HypixelGame game: Main.getMainHandler().getGameHandler().getAllGames()) {
			// Shut down each game
			game.stopGame();
		}
	}

	private void kickAllPlayers() {
		for (HypixelPlayer player: HypixelUtils.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.GOLD + "Restarting the server! Try joining again.");
		}
	}
}
