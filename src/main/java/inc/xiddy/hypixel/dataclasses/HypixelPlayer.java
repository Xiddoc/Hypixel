package inc.xiddy.hypixel.dataclasses;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.constants.Lobby;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public class HypixelPlayer extends CraftPlayer {
	private Lobby lobby;

	public HypixelPlayer(Player player) {
		super((CraftServer) player.getServer(), ((CraftPlayer) player).getHandle());
	}

	@Override
	public boolean teleport(Location location) {
		// Unmount any passengers
		this.eject();
		// Teleport and return
		return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
	}


	public Lobby getLobby() {
		return this.lobby;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
		lobby.setPlayer(this);

		// Heal player
		this.getPlayer().setHealth(20);
		this.getPlayer().setFoodLevel(20);
		this.getPlayer().setFireTicks(0);
		// Clear their inventory
		this.getPlayer().getInventory().clear();
		this.getPlayer().getInventory().setArmorContents(new ItemStack[] {
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR)
		});

		this.setScoreboard(
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

	public void setScoreboard(String string) {
		// Set scoreboard
		Main.getMainHandler().getBoardHandler().setScoreboard(this, string);
	}
}
