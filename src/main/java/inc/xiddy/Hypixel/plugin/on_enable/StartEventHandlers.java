package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.games.hub.HubEventHandler;
import inc.xiddy.hypixel.games.hub.SpectatorEventHandler;
import inc.xiddy.hypixel.plugin.OnEnableHandler;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class StartEventHandlers extends OnEnableHandler {
	@Override
	public void onEnable(JavaPlugin plugin) {
		new HubEventHandler();
		new SpectatorEventHandler();
	}
}
