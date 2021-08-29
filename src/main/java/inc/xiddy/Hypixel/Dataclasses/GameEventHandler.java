package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Main;
import net.citizensnpcs.api.event.NPCEvent;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;

import java.util.Arrays;
import java.util.List;

public abstract class GameEventHandler implements Listener {
	private final Lobby lobby;
	private final List<Class<? extends Event>> exemptedEvents = Arrays.asList(AsyncPlayerPreLoginEvent.class,
		PlayerJoinEvent.class, PlayerKickEvent.class, PlayerLoginEvent.class, PlayerPreLoginEvent.class,
		PlayerQuitEvent.class);

	public GameEventHandler(Lobby lobby) {
		// Set lobby
		this.lobby = lobby;
		// Register events to server instance
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	public final boolean verifyState(Event event) {
		// Wrapper to add logic to private method
		return !this.inverseVerifyState(event);
	}

	private boolean inverseVerifyState(Event event) {
		// Null check (spectators)
		if (this.getLobby() == null) {
			// Let spectators do whatever (Don't run any events)
			return false;
		}

		// If this is a player event
		if (event instanceof PlayerEvent) {
			// Cast the event
			PlayerEvent castedEvent = (PlayerEvent) event;
			// Check if exempted
			if (this.exemptedEvents.contains(event.getClass())) {
				// Return false since invalid event
				return false;
			} else {
				// Otherwise,
				// Check the world
				return this.checkWorld(castedEvent.getPlayer().getWorld());
			}

		} else if (event instanceof BlockEvent) {
			// Cast the event
			BlockEvent castedEvent = (BlockEvent) event;
			// Check null world case
			if (castedEvent.getBlock() == null || castedEvent.getBlock().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getBlock().getWorld());

		} else if (event instanceof InventoryEvent) {
			// Cast the event
			InventoryEvent castedEvent = (InventoryEvent) event;
			// Check null world case
			if (castedEvent.getView().getPlayer() == null || castedEvent.getView().getPlayer().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getView().getPlayer().getWorld());

		} else if (event instanceof EntityEvent) {
			// Cast the event
			EntityEvent castedEvent = (EntityEvent) event;
			// Check null world case
			if (castedEvent.getEntity() == null || castedEvent.getEntity().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getEntity().getWorld());

		} else if (event instanceof HangingEvent) {
			// Cast the event
			HangingEvent castedEvent = (HangingEvent) event;
			// Check null world case
			if (castedEvent.getEntity() == null || castedEvent.getEntity().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getEntity().getWorld());

		} else if (event instanceof VehicleEvent) {
			// Cast the event
			VehicleEvent castedEvent = (VehicleEvent) event;
			// Check null world case
			if (castedEvent.getVehicle() == null || castedEvent.getVehicle().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getVehicle().getWorld());

		} else if (event instanceof WeatherEvent) {
			// Cast the event
			WeatherEvent castedEvent = (WeatherEvent) event;
			// Check null world case
			if (castedEvent.getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getWorld());

		} else if (event instanceof WorldEvent) {
			// Cast the event
			WorldEvent castedEvent = (WorldEvent) event;
			// Check null world case
			if (castedEvent.getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getWorld());

		} else if (event instanceof NPCEvent) {
			// Cast the event
			NPCEvent castedEvent = (NPCEvent) event;
			// Check null world case
			if (castedEvent.getNPC().getEntity().getWorld() == null) return false;
			// Check the world
			return this.checkWorld(castedEvent.getNPC().getEntity().getWorld());

		} else {
			// Otherwise, return false
			return false;
		}
	}

	private boolean checkWorld(World world) {
		// Check if the world starts with the lobby name
		return world.getName().toLowerCase().startsWith(this.getLobby().toString().toLowerCase());
	}

	/*
	private boolean oldVerifyState(Event event) {
		// Null check (spectators)
		if (this.getLobby() == null) {
			// Let spectators do whatever (Don't run any events)
			return true;
		}

		// Initialize method
		Method getPlayerMethod = null;
		Method getEntityMethod = null;
		Method getBlockMethod = null;
		// Try to
		try {
			// Get the method from the event class
			getPlayerMethod = event.getClass().getMethod("getPlayer", (Class<?>[]) null);
		} catch (NoSuchMethodException ignoredPlayer) {
			// If there is no getPlayer method...
			// Then try to
			try {
				// Find a getEntity method from the event class
				getEntityMethod = event.getClass().getMethod("getEntity", (Class<?>[]) null);
			} catch (NoSuchMethodException ignoredEntity) {
				// If there is no getEntity method...
				// Then try to
				try {
					// Find a getBlock method from the event class
					getBlockMethod = event.getClass().getMethod("getBlock", (Class<?>[]) null);
				} catch (NoSuchMethodException ignoredBlock) {
					// If no getEntity OR getPlayer OR getBlock methods, then the event is not applicable to the game
					ignoredBlock.printStackTrace();
					return false;
				}
			}
		}

		// Use the method that is valid
		Object entity = null;
		Object block = null;
		try {
			if (getEntityMethod != null) {
				entity = getEntityMethod.invoke(event, (Object[]) null);
			} else if (getPlayerMethod != null) {
				entity = getPlayerMethod.invoke(event, (Object[]) null);
			} else {
				block = getBlockMethod.invoke(event, (Object[]) null);
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			// If an error occurs, then there was probably a problem with the event / method collection
			e.printStackTrace();
			return false;
		}

		// If the entity is not null
		if (entity != null) {
			return !((Entity) entity).getWorld().getName().toLowerCase().startsWith(this.getLobby().toString().toLowerCase());
		} else if (block != null) {
			// Otherwise, use the block
			return !((Block) block).getLocation().getWorld().getName().toLowerCase().startsWith(this.getLobby().toString().toLowerCase());
		}
		// Otherwise, return true to prevent function from running
		return true;
	}

	 */

	private Lobby getLobby() {
		return this.lobby;
	}
}
