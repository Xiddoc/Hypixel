package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.commons.Log;
import inc.xiddy.hypixel.commons.Reflection;
import inc.xiddy.hypixel.plugin.OnPluginEventHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

@SuppressWarnings("unused")
public class RegisterAllCommands implements OnPluginEventHandler {
	private static final String COMMAND_PACKAGE = ".commands.implementations";

	@Override
	public void onHandleEvent(JavaPlugin plugin) {
		try {
			registerCommands(plugin, plugin.getClass().getPackage().getName() + COMMAND_PACKAGE);
		} catch (ReflectiveOperationException e) {
			Log.error("Error while registering commands:");
			e.printStackTrace();
		}
	}

	public static void registerCommands(JavaPlugin plugin, String commandPackage) throws ReflectiveOperationException {
		for (HypixelCommand cmd: getAllCommands(commandPackage)) {
			registerCommand(plugin, cmd);
		}
	}

	private static void registerCommand(JavaPlugin plugin, HypixelCommand cmd) {
		PluginCommand pluginCommand = plugin.getCommand(cmd.getCommandInfo().name());

		if (pluginCommand == null) {
			throw new RuntimeException("No command called /" + cmd.getCommandInfo().name() + " found in plugin.yml");
		}

		pluginCommand.setExecutor(cmd);
	}

	private static Set<HypixelCommand> getAllCommands(String commandPackage) throws ReflectiveOperationException {
		return Reflection.getSubclassInstancesOf(HypixelCommand.class, commandPackage);
	}

}
