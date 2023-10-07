package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.commons.Reflection;
import inc.xiddy.hypixel.plugin.OnEnableHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

@SuppressWarnings("unused")
public class RegisterAllCommands extends OnEnableHandler {
	private static final String COMMAND_PACKAGE = ".commands.implementations";

	@Override
	public void onEnable(JavaPlugin plugin) {
		try {
			registerCommands(plugin, plugin.getClass().getPackage().getName() + COMMAND_PACKAGE);
		} catch (ReflectiveOperationException e) {
			Main.getMainHandler().getLogger().error("Error while registering commands:");
			e.printStackTrace();
		}
	}

	public static void registerCommands(JavaPlugin plugin, String commandPackage) throws ReflectiveOperationException {
		for (HypixelCommand cmd: getAllCommands(commandPackage)) {
			registerCommand(plugin, cmd);
		}
	}

	private static void registerCommand(JavaPlugin plugin, HypixelCommand cmd) {
		plugin.getCommand(cmd.getCommandInfo().name()).setExecutor(cmd);
	}

	private static Set<HypixelCommand> getAllCommands(String commandPackage) throws ReflectiveOperationException {
		return Reflection.getSubclassInstancesOf(HypixelCommand.class, commandPackage);
	}

}
