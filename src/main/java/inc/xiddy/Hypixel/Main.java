package inc.xiddy.Hypixel;

import inc.xiddy.Hypixel.Dataclasses.HypixelGame;
import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import inc.xiddy.Hypixel.Games.Hub.HubEventHandler;
import inc.xiddy.Hypixel.Games.Hub.SpectatorEventHandler;
import inc.xiddy.Hypixel.Handlers.MainHandler;
import inc.xiddy.Hypixel.Utility.HypixelUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

	// Function that happens when the plugin is enabled
	@Override
	public void onEnable() {
		// Register instance
		System.out.println("Registering main instance...");
		Main.instance = this;

		// Register main handler
		Main.mainHandler = new MainHandler();

		// Initialize main handler
		// This is done in 2 steps because during the construction of MainHandler,
		// There are calls to Main.getMainHandler() which will return null
		// As MainHandler has not finished construction and has not been assigned to Main.mainHandler yet
		Main.mainHandler.initMainHandler();

		// Register events to EventClass
		// Initialization is done in inherited constructor
		Main.getMainHandler().getLogger().warning("Registering all event handlers...");
		new HubEventHandler();
		new SpectatorEventHandler();

		// Register console filter
		Main.getMainHandler().getLogger().warning("Registering console filter...");
		((Logger) LogManager.getRootLogger()).addFilter(new ConsoleFilter());

		// Clean NPC registry
		CitizensAPI.getNPCRegistry().deregisterAll();

		// Success message
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

	// On a command, this function plays
	@Override
	public boolean onCommand(CommandSender sender,
							 Command command,
							 String label,
							 String[] args) {
		// Check if console
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid command caller, must be Player.");
			return true;
		}

		// Execute command
		Main.getMainHandler().getCommandHandler().execute(command, new HypixelPlayer((Player) sender), args);

		// Finish command listener function
		return false;
	}
}

