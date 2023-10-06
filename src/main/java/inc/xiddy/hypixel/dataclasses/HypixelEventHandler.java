package inc.xiddy.hypixel.dataclasses;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;
import inc.xiddy.hypixel.Main;
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

@SuppressWarnings("deprecation")
public abstract class HypixelEventHandler implements Listener {
	private final Lobby lobby;
	private final List<Class<? extends Event>> exemptedEvents = Arrays.asList(AsyncPlayerPreLoginEvent.class,
		PlayerJoinEvent.class, PlayerKickEvent.class, PlayerLoginEvent.class, PlayerPreLoginEvent.class,
		PlayerQuitEvent.class);
	private final HypixelRunnable hypixelRunnable;

	public HypixelEventHandler(HypixelRunnable hypixelRunnable) {
		// Set lobby
		this.lobby = hypixelRunnable.getLobby();
		// Set runnable
		this.hypixelRunnable = hypixelRunnable;
		// Register events to server instance
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	public HypixelEventHandler(Lobby lobby) {
		// Set lobby
		this.lobby = lobby;
		// Set runnable
		this.hypixelRunnable = null;
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

	private Lobby getLobby() {
		return this.lobby;
	}

	public final HypixelRunnable getBaseGame() {
		return this.hypixelRunnable;
	}

	public abstract HypixelRunnable getGame();
}
