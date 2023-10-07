package inc.xiddy.hypixel.plugin;

import inc.xiddy.hypixel.commons.Reflection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class OnEnableRegistrar {
	private static final String ON_ENABLE_PACKAGE = ".on_enable";

	public void executeOnEnableHandlers(JavaPlugin plugin) throws ReflectiveOperationException {
		for (OnEnableHandler handler : getOnEnableHandlers()) {
			handler.onEnable(plugin);
		}
	}

	private Set<OnEnableHandler> getOnEnableHandlers() throws ReflectiveOperationException {
		return Reflection.getSubclassInstancesOf(OnEnableHandler.class, getOnEnablePackage());
	}

	private String getOnEnablePackage() {
		return this.getClass().getPackage().getName() + ON_ENABLE_PACKAGE;
	}

}
