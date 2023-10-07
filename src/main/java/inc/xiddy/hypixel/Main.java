package inc.xiddy.hypixel;

import inc.xiddy.hypixel.dataclasses.HypixelGame;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.handlers.MainHandler;
import inc.xiddy.hypixel.plugin.OnEnableRegistrar;
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

		// Execute all onEnable handlers
		try {
			new OnEnableRegistrar().executeOnEnableHandlers(this);
		} catch (ReflectiveOperationException e) {
			Main.getMainHandler().getLogger().error("Error while executing onEnable handlers:");
			e.printStackTrace();
			return;
		}

		Main.getMainHandler().getLogger().success("Plugin successfully loaded...");
	}

	// Function that happens when the plugin is disabled
	@Override
	public void onDisable() {
		// Clean NPC registry
		CitizensAPI.getNPCRegistry().deregisterAll();

		// Destroy all games
		Main.getMainHandler().getLogger().warning("Shutting down games...");
		for (HypixelGame game : Main.getMainHandler().getGameHandler().getAllGames()) {
			// Shut down each game
			game.stopGame();
		}

		// Kick everyone off to prevent issues with lobbies
		Main.getMainHandler().getLogger().warning("Kicking all players...");
		for (HypixelPlayer player : HypixelUtils.getOnlinePlayers()) {
			// Unregister
			try {
				Main.getMainHandler().getPlayerHandler().deregister(player);
			} catch (NoClassDefFoundError e) {
				Main.getMainHandler().getLogger().error("Jackson error.. again.. (While saving player info during deregister)");
			}
			// Kick the player
			player.kickPlayer(ChatColor.GOLD + "Restarting the server! Try joining again.");
		}
	}
}

