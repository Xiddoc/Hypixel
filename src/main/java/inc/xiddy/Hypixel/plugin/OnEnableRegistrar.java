package inc.xiddy.hypixel.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.Set;

public class OnEnableRegistrar {
	private static final String ON_ENABLE_PACKAGE = ".on_enable";

	public void executeOnEnableHandlers(JavaPlugin plugin) {
		for (Class <? extends OnEnableHandler> onEnableHandler : getOnEnableHandlers()) {
			onEnableHandler.cast(onEnableHandler).onEnable(plugin);
		}
	}

	private Set<Class<? extends OnEnableHandler>> getOnEnableHandlers() {
		return new Reflections(getOnEnablePackage()).getSubTypesOf(OnEnableHandler.class);
	}

	private String getOnEnablePackage() {
		return this.getClass().getPackage().getName() + ON_ENABLE_PACKAGE;
	}

}
