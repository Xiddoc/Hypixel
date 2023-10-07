package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.ConsoleFilter;
import inc.xiddy.hypixel.plugin.OnEnableHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class StartConsoleFilter extends OnEnableHandler {
	@Override
	public void onEnable(JavaPlugin plugin) {
		((Logger) LogManager.getRootLogger()).addFilter(new ConsoleFilter());
	}
}
