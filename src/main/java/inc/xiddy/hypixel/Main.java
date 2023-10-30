package inc.xiddy.hypixel;

import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.handlers.MainHandler;
import inc.xiddy.hypixel.logging.Log;
import inc.xiddy.hypixel.plugin.PluginEventRegistrar;
import inc.xiddy.hypixel.utility.HypixelUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	private static JavaPlugin instance;
	private static MainHandler mainHandler;

	public static JavaPlugin getInstance() {
		return Main.instance;
	}

	public static MainHandler getMainHandler() {
		return Main.mainHandler;
	}

	@Override
	public void onEnable() {
		Main.instance = this;

		// Register main handler
		// This is done in 2 steps because during the construction of MainHandler,
		// There are calls to Main.getMainHandler() which will return null
		// As MainHandler has not finished construction and has not been assigned to Main.mainHandler yet
		Main.mainHandler = new MainHandler();
		Main.mainHandler.initMainHandler();

		Log.info("Executing onEnable handlers...");
		try {
			new PluginEventRegistrar().executePluginEventHandlers(this, PluginEventRegistrar.PluginEvent.ON_ENABLE);
		} catch (ReflectiveOperationException e) {
			Log.error("Error while executing onEnable handlers:");
			e.printStackTrace();

			Log.error("Could not load Hypixel plugin.");
			return;
		}

		Log.success("Plugin successfully loaded...");
	}

	@Override
	public void onDisable() {
		// Clean NPC registry
		CitizensAPI.getNPCRegistry().deregisterAll();

		// Destroy all games
		Log.info("Shutting down games...");
		for (HypixelGame game: Main.getMainHandler().getGameHandler().getAllGames()) {
			// Shut down each game
			game.stopGame();
		}

		// Kick everyone off to prevent issues with lobbies
		Log.info("Kicking all players...");
		for (HypixelPlayer player: HypixelUtils.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.GOLD + "Restarting the server! Try joining again.");
		}
	}
}

