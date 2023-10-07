package inc.xiddy.hypixel.plugin.on_enable;

import inc.xiddy.hypixel.plugin.OnEnableHandler;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class ClearCitizensRegistry extends OnEnableHandler {
	@Override
	public void onEnable(JavaPlugin plugin) {
		CitizensAPI.getNPCRegistry().deregisterAll();
	}
}
