package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.ConsoleFilter;
import inc.xiddy.hypixel.plugin.OnPluginEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class StartConsoleFilter implements OnPluginEventHandler {
	@Override
	public void onHandleEvent(JavaPlugin plugin) {
		((Logger) LogManager.getRootLogger()).addFilter(new ConsoleFilter());
	}
}
