package inc.xiddy.Hypixel.Games.Lobby;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Dataclasses.GameEventHandler;
import inc.xiddy.Hypixel.Dataclasses.PlayerData;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

import static org.bukkit.ChatColor.*;

public class LobbyEventHandler extends GameEventHandler {
	public LobbyEventHandler() {
		super(Lobby.HUB);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		// Any time a player clicks
		// Pass the data to the anticheat
		Main.getMainHandler().getAnticheatHandler().onClick(event);
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
			Main.getMainHandler().getPlayerHandler().getPlayerData(event.getPlayer()).setLobby(Lobby.HUB);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		// Register player
		PlayerData data = Main.getMainHandler().getPlayerHandler().register(event.getPlayer());
		// Move player to the lobby
		data.setLobby(Lobby.HUB);
		// Remove join message
		event.setJoinMessage("");
		// For everyone in the lobby
		for (Player player : Main.getMainHandler().getLobbyHandler().getPlayersInLobby(Lobby.HUB)) {
			// Announce join message
			player.sendMessage(
				GRAY + event.getPlayer().getDisplayName() + ChatColor.GOLD + " joined the lobby!"
			);
			// Play sound
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
		}
		// Set the lobby scoreboard
		Main.getMainHandler().getPlayerHandler().getPlayerData(event.getPlayer()).setScoreboard(
			GOLD + "" + BOLD + "BEDWARS" +
				"\n\nLevel: " + GRAY + "48✫" +
				"\n\nProgress: " + AQUA + "1.9k" + GRAY + "/" + GREEN + "5k" +
				"\n" + DARK_GRAY + " [" + AQUA + "■■■■■" + GRAY + "■■■■■" + DARK_GRAY + "]" +
				"\n\nLoot Chests: " + YELLOW + "0" +
				"\n\nCoins: " + GOLD + "64,407" +
				"\n\nTotal Kills: " + GREEN + "3,004" +
				"\nTotal Wins: " + GREEN + "324" +
				"\n\n" + YELLOW + "www.hypixel.net"
		);
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
		Main.getMainHandler().getPlayerHandler().deregister(event.getPlayer());
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

	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent event) {
		// If user has the privilege to execute any command
		if (Main.getMainHandler().getPlayerHandler().getPlayerData(event.getPlayer()).getRole().equals(Permission.OWNER)) {
			// Bypass the permission checks
			return;
		}

		// Don't run default commands
		// Get index of space (if there are arguments)
		int idx = event.getMessage().indexOf(" ");
		String command;
		// If the index is equal to -1
		if (idx == -1) {
			// Then the user didn't input any arguments
			// Remove slash
			command = event.getMessage().substring(1);
		} else {
			// Remove slash
			// Up till first argument
			command = event.getMessage().substring(1, idx);
		}

		// If command is not registered
		if (!Main.getMainHandler().getCommandHandler().isHypixelCommandRegistered(command)) {
			// Send error
			Main.getMainHandler().getCommandHandler().sendInvalidCommand(event.getPlayer());
			// Cancel the command
			event.setCancelled(true);
		}
	}
}
