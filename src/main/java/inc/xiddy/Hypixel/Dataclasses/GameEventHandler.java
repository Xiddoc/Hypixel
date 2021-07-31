package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Main;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class GameEventHandler implements Listener {
	private final Lobby lobby;

	public GameEventHandler(Lobby lobby) {
		// Set lobby
		this.lobby = lobby;
		// Register events to server instance
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	protected boolean verifyState(Event event) {
		// Null check (spectators)
		if (this.getLobby() == null) {
			// Let spectators do whatever
			return true;
		}

		// Initialize method
		Method getPlayerMethod = null;
		Method getEntityMethod = null;
		// Try to
		try {
			// Get the method from the event class
			getPlayerMethod = event.getClass().getMethod("getPlayer", (Class<?>[]) null);
		} catch (NoSuchMethodException ignored) {
			// If there is no getPlayer method...
			// Then try to
			try {
				// Find a getEntity method from the event class
				getEntityMethod = event.getClass().getMethod("getEntity", (Class<?>[]) null);
			} catch (NoSuchMethodException e) {
				// If no getEntity or getPlayer methods, then the event is not applicable to the game
				return false;
			}
		}

		// Use the method that is valid
		Object entity;
		try {
			if (getEntityMethod != null) {
				entity = getEntityMethod.invoke(event, (Object[]) null);
			} else {
				entity = getPlayerMethod.invoke(event, (Object[]) null);
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			// If an error occurs, then there was probably a problem with the event / method collection
			return false;
		}

		return !((Entity) entity).getWorld().getName().toLowerCase().startsWith(this.getLobby().toString().toLowerCase());

//		// Call the method with the instance set to the inputted event
//		// Check if the player is in this lobby
//		// Preform a NOT (!) operation so that if they are in a different lobby,
//		// Then the return value is true and therefore will return and exit the event
//		return !Main.getMainHandler().getPlayerHandler().getPlayerData((Player) entity).getLobby().equals(this.getLobby());

	}

	private Lobby getLobby() {
		return this.lobby;
	}
}
