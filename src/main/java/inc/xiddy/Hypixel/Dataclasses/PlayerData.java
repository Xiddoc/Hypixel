package inc.xiddy.Hypixel.Dataclasses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.Permission;
import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

@JsonIgnoreProperties(value = {"player", "online"})
public class PlayerData {
	private final UUID playerUUID;
	private final List<Long> leftClicks;
	private final List<Long> rightClicks;
	// Non-serialized fields
	private final Player player;
	// Serialized fields
	private Permission role;
	private Lobby lobby;
	private boolean online;

	@JsonCreator
	public PlayerData(@JsonProperty("role") Permission role,
					  @JsonProperty("lobby") Lobby lobby,
					  @JsonProperty("playerUUID") UUID playerUUID,
					  @JsonProperty("online") boolean online,
					  @JsonProperty("leftClicks") List<Long> leftClicks,
					  @JsonProperty("rightClicks") List<Long> rightClicks) {
		// Set fields
		this.role = role;
		this.lobby = lobby;
		this.playerUUID = playerUUID;
		// Get data / defaults
		this.player = this.bukkitGetPlayer();
		this.online = true;
		// Reset clicks
		this.leftClicks = new ArrayList<>();
		this.rightClicks = new ArrayList<>();
	}

	public PlayerData(Permission role, UUID playerUUID) {
		// Set default role
		this.role = role;
		// Set player UUID
		this.playerUUID = playerUUID;
		// Get player by UUID
		this.player = this.bukkitGetPlayer();
		// Default online state
		this.online = true;
		// Reset clicks
		this.leftClicks = new ArrayList<>();
		this.rightClicks = new ArrayList<>();
	}

	public Permission getRole() {
		return this.role;
	}

	public void setRole(Permission role) {
		// Set field
		this.role = role;
	}

	public Lobby getLobby() {
		return this.lobby;
	}

	public void setLobby(Lobby lobby) {
		// Set field
		this.lobby = lobby;
		// Set lobby
		if (this.getOnline()) lobby.setPlayer(this.getPlayer());
		// Heal player
		this.getPlayer().setHealth(20);
		this.getPlayer().setFoodLevel(20);
		// Clear their inventory
		this.getPlayer().getInventory().clear();
		this.getPlayer().getInventory().setArmorContents(new ItemStack[] {
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR),
			new ItemStack(Material.AIR)
		});
		// Update scoreboard
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

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public boolean getOnline() {
		return this.online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	private Player getPlayer() {
		return this.player;
	}

	public void setScoreboard(String string) {
		// Set scoreboard
		Main.getMainHandler().getBoardHandler().setScoreboard(this.getPlayer(), string);
	}

	private Player bukkitGetPlayer() {
		return Bukkit.getPlayer(this.getPlayerUUID());
	}

	public void revokeLeftClick() {
		// Remove last click
		this.leftClicks.remove(this.leftClicks.size() - 1);
	}

	public void leftClick() {
		// Add new click to list
		this.leftClicks.add(System.currentTimeMillis());
		// Clean old clicks
		// If time is more than 1 second (1000 milliseconds) old
		this.leftClicks.removeIf(time -> System.currentTimeMillis() > time + 1000);
	}

	public void rightClick() {
		// Add new click to list
		this.rightClicks.add(System.currentTimeMillis());
		// Clean old clicks
		// If time is more than 1 second (1000 milliseconds) old
		this.rightClicks.removeIf(time -> System.currentTimeMillis() > time + 1000);
	}

	public double leftCPS() {
		return this.leftClicks.size();
	}

	public double rightCPS() {
		return this.rightClicks.size();
	}

	@Override
	public String toString() {
		return "PlayerData{" +
			"role=" + role +
			", lobby=" + lobby +
			", playerUUID=" + playerUUID +
			", online=" + online +
			", player=" + player +
			'}';
	}
}
