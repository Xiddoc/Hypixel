package inc.xiddy.hypixel.plugin.on_disable;

import inc.xiddy.hypixel.plugin.OnPluginEventHandler;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ClearCitizensRegistry implements OnPluginEventHandler {
	@Override
	public void onHandleEvent(JavaPlugin plugin) {
		CitizensAPI.getNPCRegistry().deregisterAll();
	}
}
