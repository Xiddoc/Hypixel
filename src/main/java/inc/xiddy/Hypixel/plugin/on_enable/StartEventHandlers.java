package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.games.hub.HubEventHandler;
import inc.xiddy.hypixel.plugin.OnPluginEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class StartEventHandlers implements OnPluginEventHandler {
	@Override
	public void onHandleEvent(JavaPlugin plugin) {
		new HubEventHandler();
	}
}
