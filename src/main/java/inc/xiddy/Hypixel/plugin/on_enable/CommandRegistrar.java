package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.plugin.OnEnableHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class CommandRegistrar extends OnEnableHandler {
	private static final String COMMAND_PACKAGE = ".commands.implementations";

	@Override
	public void onEnable(JavaPlugin plugin) {
		try {
			registerCommands(plugin, plugin.getClass().getPackage().getName() + COMMAND_PACKAGE);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static void registerCommands(JavaPlugin plugin, String commandPackage) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		for (Class<? extends HypixelCommand> clazz: getAllCommands(commandPackage)) {
			registerCommand(plugin, clazz);
		}
	}

	private static void registerCommand(JavaPlugin plugin, Class<? extends HypixelCommand> commandClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		HypixelCommand command = commandClass.getDeclaredConstructor().newInstance();
		plugin.getCommand(command.getCommandInfo().name()).setExecutor(command);
	}

	private static Set<Class<? extends HypixelCommand>> getAllCommands(String commandPackage) {
		return new Reflections(commandPackage).getSubTypesOf(HypixelCommand.class);
	}

}
