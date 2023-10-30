package inc.xiddy.hypixel.plugin;

import inc.xiddy.hypixel.commons.Reflection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class PluginEventRegistrar {

	public void executePluginEventHandlers(JavaPlugin plugin, PluginEvent event) throws ReflectiveOperationException {
		for (OnPluginEventHandler handler: getEventHandlers(event)) {
			handler.onHandleEvent(plugin);
		}
	}

	private Set<OnPluginEventHandler> getEventHandlers(PluginEvent event) throws ReflectiveOperationException {
		return Reflection.getSubclassInstancesOf(OnPluginEventHandler.class, getEventPackage(event));
	}

	private String getEventPackage(PluginEvent event) {
		return this.getClass().getPackage().getName() + "." + event.getPackageName();
	}

	public enum PluginEvent {
		ON_ENABLE("on_enable"),
		ON_DISABLE("on_disable");

		private final String packageName;

		PluginEvent(String packageName) {
			this.packageName = packageName;
		}

		public String getPackageName() {
			return packageName;
		}
	}
}
