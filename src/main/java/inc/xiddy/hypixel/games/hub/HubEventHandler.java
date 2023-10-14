package inc.xiddy.hypixel.games.hub;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelEventHandler;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.GRAY;

public class HubEventHandler extends HypixelEventHandler {
	public HubEventHandler() {
		super(Lobby.HUB);
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// They aren't allowed to break anything
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (this.verifyState(event)) return;

		// They aren't allowed to place anything
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// They aren't allowed to hurt each other
		event.setCancelled(true);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (this.verifyState(event)) return;

		// If player falling in void
		if (event.getTo().getY() < 0) {
			// Teleport back to spawn
			Main.getMainHandler().getPlayerHandler().getPlayerData(new HypixelPlayer(event.getPlayer())).setLobby(Lobby.HUB);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		// Register player
		// Move player to the lobby
		Main.getMainHandler().getPlayerHandler().register(new HypixelPlayer(event.getPlayer())).setLobby(Lobby.HUB);
		// Remove join message
		event.setJoinMessage("");
		// For everyone in the lobby
		for (HypixelPlayer player : Lobby.HUB.getPlayersInLobby()) {
			// Announce join message
			player.sendMessage(
				GRAY + event.getPlayer().getDisplayName() + ChatColor.GOLD + " joined the lobby!"
			);
			// Play sound
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
		}
	}

	@EventHandler
	public void onChestOpen(InventoryOpenEvent event) {
		if (this.verifyState(event)) return;

		// If player clicked enderchest / chest
		if (event.getInventory().getType().equals(InventoryType.CHEST) ||
			event.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
			// Make chest stay closed
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		// Change the format of the message
		event.setFormat(
			GRAY + event.getPlayer().getDisplayName() + ": %2$s"
		);
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		// Deregister the player
		Main.getMainHandler().getPlayerHandler().deregister(new HypixelPlayer(event.getPlayer()));
		// Remove quitting message
		event.setQuitMessage("");
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		// Never make any player lose hunger
		event.setCancelled(true);
	}

	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		// If changing to weather
		if (event.toWeatherState()) {
			// Stop it
			event.getWorld().setWeatherDuration(0);
			event.getWorld().setStorm(false);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		// Don't let the player go to the nether/end under any circumstance.
		// If mishandled, this could generate a new sub-world for the end/nether in that world.
		event.setCancelled(true);
	}

	@EventHandler
	public void onAchievement(PlayerAchievementAwardedEvent event) {
		// No one cares about your achievements!
		event.setCancelled(true);
	}

	@EventHandler
	public void onStats(PlayerStatisticIncrementEvent event) {
		// Don't save statistics
		event.setCancelled(true);
	}

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		// Don't let players craft
		event.setCancelled(true);
		event.getInventory().setResult(new ItemStack(Material.AIR));
	}

	@EventHandler
	public void onPreCraft(PrepareItemCraftEvent event) {
		// Don't let players craft
		event.getInventory().setResult(new ItemStack(Material.AIR));
	}

	@Override
	public HypixelRunnable getGame() {
		return null;
	}
}
